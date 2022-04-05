package com.ksa.foody.adapters

import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ksa.foody.R
import com.ksa.foody.data.db.entities.FavoritesEntity
import com.ksa.foody.databinding.FavouriteRecipesRowBinding
import com.ksa.foody.ui.fragments.FavRecipeFragmentDirections
import com.ksa.foody.util.RecipesDiffUtils
import com.ksa.foody.viewmodel.MainViewModel


class FavouriteRecipesAdapter (private val requireActivity : FragmentActivity,private val mainViewModel: MainViewModel):
        RecyclerView.Adapter<FavouriteRecipesAdapter.FavouriteRecipesViewHolder>(),ActionMode.Callback {

    private var multiSelection = false
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<FavouriteRecipesViewHolder>()
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class FavouriteRecipesViewHolder(val binding:FavouriteRecipesRowBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(favoritesEntity: FavoritesEntity){
            binding.favouritesEntity = favoritesEntity
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):FavouriteRecipesViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavouriteRecipesRowBinding.inflate(layoutInflater,parent,false)
                return FavouriteRecipesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteRecipesViewHolder {
        return FavouriteRecipesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavouriteRecipesViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

        /**
         * Single Click Listener
         * */
        holder.binding.favRecipeRowlayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                        FavRecipeFragmentDirections.actionFavRecipeFragmentToDetailsActivity(
                                currentRecipe.result
                        )
                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long Click Listener
         * */
        holder.binding.favRecipeRowlayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(newFavouritesList:List<FavoritesEntity>){
        val recipesDiffUtils = RecipesDiffUtils(favoriteRecipes,newFavouritesList)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtils)
        favoriteRecipes = newFavouritesList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favourites_contextual_menu,menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_fav_item){
            selectedRecipes.forEach {
                mainViewModel.deleteFavouriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed")
            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
        }
        multiSelection = false
        //Log.v("multiSelectionFalse","#109 "+multiSelection)
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    fun applyStatusBarColor(color: Int){
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity,color)
    }

    fun applySelection(holder: FavouriteRecipesViewHolder,currentRecipe:FavoritesEntity){
        if(selectedRecipes.contains(currentRecipe)){
            //Log.v("applySelection","selectedRecipes.contains")
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
            applyActionModeTitle()
        }else{
            //Log.v("applySelection","!selectedRecipes.contains" )
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundLightColor,R.color.colorPrimary)
            applyActionModeTitle()
            //changeRecipeStyle(holder,R.color.cardBackgroundLightColor,R.color.green)
        }
    }

    fun changeRecipeStyle(myViewHolder: FavouriteRecipesViewHolder,backgroundColor:Int,
                          strokeColor:Int){
        //Log.v("applySelection","!selectedRecipes.contains "+ContextCompat.getColor(requireActivity,backgroundColor)+" strokeColor "+ContextCompat.getColor(requireActivity,strokeColor))
        myViewHolder.binding.favRecipeRowlayout.setBackgroundColor(ContextCompat.getColor(requireActivity,backgroundColor))
        myViewHolder.binding.favRowCardview.strokeColor = (ContextCompat.getColor(requireActivity,strokeColor))
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
                rootView,
                message,
                Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
                .show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

}