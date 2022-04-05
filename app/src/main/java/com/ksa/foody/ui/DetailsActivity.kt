package com.ksa.foody.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.ksa.foody.R
import com.ksa.foody.adapters.PagerAdapter
import com.ksa.foody.data.db.entities.FavoritesEntity
import com.ksa.foody.databinding.ActivityDetailsBinding
import com.ksa.foody.ui.fragments.detailsfragments.IngredientsFragment
import com.ksa.foody.ui.fragments.detailsfragments.InstructionsFragment
import com.ksa.foody.ui.fragments.detailsfragments.OverviewFragment
import com.ksa.foody.util.Constants
import com.ksa.foody.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel : MainViewModel by viewModels()
    private var recipeSaved = false
    private var savedRecipeId = 0

    private lateinit var binding : ActivityDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.detailsToolbar)
        binding.detailsToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(Constants.RECIPE_BUNDLE,args.result)

        val pagerAdapter2 = PagerAdapter(resultBundle,fragments,this)

        binding.detailsViewpager2.apply {
            adapter = pagerAdapter2
        }
        TabLayoutMediator(binding.detailsTablayout,binding.detailsViewpager2){tab,position ->
            tab.text =titles[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }else if(item.itemId == R.id.save_to_favourites_menu&& !recipeSaved){
            Log.v("DetailsActivity","!recipeSaved")
            saveToFavourites(item)
        }else if(item.itemId == R.id.save_to_favourites_menu && recipeSaved){
            Log.v("DetailsActivity","recipeSaved")
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(0,args.result)
        mainViewModel.insertFavouriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.yellow)
        showSnackBar("Recipe Added to Favourites!!..")
        recipeSaved = true
    }

    private fun removeFromFavourites(item:MenuItem){
        val favoritesEntity = FavoritesEntity(savedRecipeId,args.result)
        mainViewModel.deleteFavouriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.white)
        showSnackBar("Removed From Favourites..")
        recipeSaved = false
    }


    private fun showSnackBar(s: String) {
        Snackbar.make(binding.detailsTablayout,
                s,
                Snackbar.LENGTH_SHORT).setAction("Okay"){}
                .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem = menu?.findItem(R.id.save_to_favourites_menu)
        checkSavedFavRecipe(menuItem!!)
        return true
    }

    private fun checkSavedFavRecipe(menuItem: MenuItem) {

        mainViewModel.readFavRecipes.observe(this,{favEntity ->
            try{
                for(savedRecipe in favEntity){
                    if(savedRecipe.result.id == args.result.id){
                        changeMenuItemColor(menuItem,R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                        break
                    }else{
                        changeMenuItemColor(menuItem,R.color.white)
                    }
                }
            }catch (e: Exception){
                Log.v("DetailsActivity",e.message.toString())
            }
        })
    }
}