package com.ksa.foody.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.ksa.foody.R
import com.ksa.foody.databinding.FragmentFoodJokeBinding
import com.ksa.foody.util.Constants
import com.ksa.foody.util.NetworkResult
import com.ksa.foody.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private val mainViewModel : MainViewModel by viewModels()
    private var foodJokeBinding : FragmentFoodJokeBinding? = null
    private val binding get() = foodJokeBinding!!
    private var foodJoke = "No Food Joke available"
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        foodJokeBinding = FragmentFoodJokeBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
        mainViewModel.getFoodJokes(Constants.API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner, {response ->
            when(response){
                is NetworkResult.Success -> {
                    binding.jokeTextview.text = response.data?.text
                    if(response.data != null){
                        foodJoke = response.data.text
                    }
                }

                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {

                }
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_share){
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT,foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache(){

        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner, {db ->
                if(!db.isNullOrEmpty()){
                    foodJoke= db[0].foodJoke.text
                    binding.jokeTextview.text = db[0].foodJoke.text
                }
            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        foodJokeBinding = null
    }
}