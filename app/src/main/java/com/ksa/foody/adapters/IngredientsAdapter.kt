package com.ksa.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ksa.foody.R
import com.ksa.foody.databinding.IngredientsRowBinding
import com.ksa.foody.models.ExtendedIngredient
import com.ksa.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.ksa.foody.util.RecipesDiffUtils


class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>(){

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class IngredientsViewHolder(val binding: IngredientsRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(IngredientsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.binding.ingredientImageview.load(BASE_IMAGE_URL+ingredientsList[position].image){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        //Log.v("ingredient_imageview",BASE_IMAGE_URL+ingredientsList[position].image)
        holder.binding.ingredientTextView.text = ingredientsList[position].name?.capitalize()
        holder.binding.ingredientAmountTextView.text = ingredientsList[position].amount?.toString()
        holder.binding.ingredientUnitTextView.text = ingredientsList[position].unit
        holder.binding.ingredientConsistencyTextView.text = ingredientsList[position].consistency
        holder.binding.ingredientOriginalTextView.text = ingredientsList[position].original
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>){
        val ingredientsDiffUtil = RecipesDiffUtils(ingredientsList,newIngredients)
        val diffUtilResults = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResults.dispatchUpdatesTo(this)

    }
}