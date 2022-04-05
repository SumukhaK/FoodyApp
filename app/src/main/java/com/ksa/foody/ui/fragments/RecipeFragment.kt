package com.ksa.foody.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.foody.R
import com.ksa.foody.adapters.RecipesAdapter
import com.ksa.foody.databinding.FragmentRecipeBinding
import com.ksa.foody.util.NetworkListener
import com.ksa.foody.util.NetworkResult
import com.ksa.foody.util.observeOnce
import com.ksa.foody.viewmodel.MainViewModel
import com.ksa.foody.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipeFragment : Fragment() , SearchView.OnQueryTextListener{

    private var recipeBinding : FragmentRecipeBinding ? = null
    private val binding get() = recipeBinding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipeViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private val args by navArgs<RecipeFragmentArgs>()
    private lateinit var networkListener: NetworkListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        recipeBinding =  FragmentRecipeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)
        setupRecyclerView()
        recipeViewModel.readBackOnline.observe(viewLifecycleOwner,{
            recipeViewModel.backOnline = it
        })

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect{status ->
                Log.v("NetworkListener",status.toString())
                recipeViewModel.networkStatus = status
                recipeViewModel.showNetworkStatus()
                readDb()
            }
        }

        binding.recipesFab.setOnClickListener{
            if(recipeViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipeFragment_to_recipesBottomSheetFragment)
            }else{
                recipeViewModel.showNetworkStatus()
            }
        }
        return binding.root
    }

    private fun setupRecyclerView(){
        binding.recipeRecyclerview.adapter = mAdapter
        binding.recipeRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu,menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        if(query != null){
            searchApiResults(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun searchApiResults(searchQuery:String){
        showShimmerEffect()
        mainViewModel.searchRecipes(recipeViewModel.applySearchQueries(searchQuery))
        mainViewModel.searchedRecipeResponse.observe(viewLifecycleOwner,{response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(),
                    response.message.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

            }
        })
    }


    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner,{database ->
                if(database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }

    }

    private fun readDb() {

        mainViewModel.readRecipes.observeOnce(viewLifecycleOwner,{database ->
            if(database.isNotEmpty() && !args.backFromBottomSheet){
                Log.v("READDATA","database.isNotEmpty")
                mAdapter.setData(database[0].foodRecipe)
                hideShimmerEffect()
            }else{
                Log.v("READDATA","database.isEmpty")
                getAPIData()
            }
        })
    }

    private fun showShimmerEffect(){
        binding.recipeRecyclerview.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.recipeRecyclerview.hideShimmer()
    }

    private fun getAPIData(){
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner,{response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipeBinding = null
    }

}