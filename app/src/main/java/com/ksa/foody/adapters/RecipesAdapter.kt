package com.ksa.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.foody.databinding.ReciepeRowBinding
import com.ksa.foody.models.FoodRecipe
import com.ksa.foody.models.Result
import com.ksa.foody.util.RecipesDiffUtils

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipesList = emptyList<Result>()


    class MyViewHolder(private val rowBinding: ReciepeRowBinding):RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(result: Result){
            rowBinding.result = result
            rowBinding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup) : MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReciepeRowBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipesList[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    fun setData(newData:FoodRecipe){

        val recipesDiffUtils = RecipesDiffUtils(recipesList,newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtils)
        recipesList = newData.results
        diffUtilResult.dispatchUpdatesTo(this)

    }
}