package com.ksa.foody.ui.fragments.detailsfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.foody.R
import com.ksa.foody.adapters.IngredientsAdapter
import com.ksa.foody.databinding.FragmentIngredientsBinding
import com.ksa.foody.models.Result
import com.ksa.foody.util.Constants.Companion.RECIPE_BUNDLE



class IngredientsFragment : Fragment() {

    private val mAdapter : IngredientsAdapter by lazy { IngredientsAdapter() }
    private var _binding : FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater,container,false)
        val args = arguments
        val myBundle : Result? = args?.getParcelable(RECIPE_BUNDLE)

        //setupRecyclerView(ingredientView)
        //Log.v("IngredientsFragment",myBundle?.extendedIngredients.toString())
        //ingredientView.recipe_recyclerview.layoutManager = LinearLayoutManager(requireContext())
        //ingredientView.recipe_recyclerview.adapter = mAdapter
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.ingredientsRecyclerview.adapter = mAdapter
        binding.ingredientsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}