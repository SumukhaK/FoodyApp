package com.ksa.foody.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ksa.foody.R
import com.ksa.foody.adapters.FavouriteRecipesAdapter
import com.ksa.foody.databinding.FragmentFavRecipeBinding
import com.ksa.foody.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavRecipeFragment : Fragment() {

    private val mainViewModel : MainViewModel by viewModels()
    private val favouritesAdapter by lazy {   FavouriteRecipesAdapter(requireActivity(),mainViewModel) }
    private var _binding : FragmentFavRecipeBinding? = null
    private val binding  get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavRecipeBinding.inflate(layoutInflater,container,false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = favouritesAdapter
        setHasOptionsMenu(true)
        //val favView = inflater.inflate(R.layout.fragment_fav_recipe, container, false)
        setupRecyclerView(binding.favRecipesRecyclerview)
        /*mainViewModel.readFavRecipes.observe(viewLifecycleOwner,{favEntity ->
            favouritesAdapter.setData(favEntity)
        })*/
        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.adapter = favouritesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_recipe_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.deleteAll_fav_recipes){
            mainViewModel.deleteAllFavouriteRecipes()
            showSnackBar("All Recipes deleted Successfully !!..")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction(message) {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        favouritesAdapter.clearContextualActionMode()
    }
}