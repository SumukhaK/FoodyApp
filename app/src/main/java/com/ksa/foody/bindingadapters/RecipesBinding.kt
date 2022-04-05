package com.ksa.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.ksa.foody.data.db.entities.RecipesEntity
import com.ksa.foody.models.FoodRecipe
import com.ksa.foody.util.NetworkResult

class RecipesBinding {

    companion object{

        @BindingAdapter("readApiResponse","readDb",requireAll = true)
        @JvmStatic
        fun handleReadDataErrors(view: View,
                                 apiResponse: NetworkResult<FoodRecipe>?,
                                 database: List<RecipesEntity>?){

            when(view){
                is ImageView->{
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }

                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }

        }

    }
}