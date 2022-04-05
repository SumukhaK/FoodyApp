package com.ksa.foody.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ksa.foody.databinding.RecipesBottomSheetBinding
import com.ksa.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.ksa.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.ksa.foody.viewmodel.RecipeViewModel
import java.util.*


class RecipesBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel : RecipeViewModel
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    private var _binding : RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment

        _binding =  RecipesBottomSheetBinding.inflate(inflater, container, false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            mealTypeChipId = value.selectedMealTypeId
            dietTypeChip = value.selectedDietType
            dietTypeChipId = value.selectedDietTypeId

            updateChips(value.selectedMealTypeId,binding.mealtypeChipgroup)
            updateChips(value.selectedDietTypeId,binding.dietChipGroup)
        })

        binding.mealtypeChipgroup.setOnCheckedChangeListener { group, selectedChipId ->

            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        binding.dietChipGroup.setOnCheckedChangeListener { group, checkedId ->

            val chip = group.findViewById<Chip>(checkedId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = checkedId

        }

        binding.applyButton.setOnClickListener {
            recipesViewModel.saveMealAndDietType(mealTypeChip,mealTypeChipId,dietTypeChip,dietTypeChipId)
            // val action = RecipesBottomSheetFragmentDirections.actionRecipesBottomSheetFragmentToRecipeFragment(true)
            val action = RecipesBottomSheetFragmentDirections.actionRecipesBottomSheetFragmentToRecipeFragment(true)
            findNavController().navigate(action)
        }


        return binding.root
    }

    private fun updateChips(chipId: Int, chipGroup: ChipGroup) {

        if(chipId != 0){
            try {
                //chipGroup.findViewById<Chip>(chipId).isChecked = true
                val targetView = chipGroup.findViewById<Chip>(chipId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView, targetView);
            }catch (exception:Exception){
                Log.v("RecipeBottomSheet",exception.message.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}