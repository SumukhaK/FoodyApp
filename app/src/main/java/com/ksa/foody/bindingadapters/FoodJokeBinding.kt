package com.ksa.foody.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import com.ksa.foody.data.db.entities.FoodJokeEntity
import com.ksa.foody.models.FoodJoke
import com.ksa.foody.util.NetworkResult

class FoodJokeBinding {

    companion object{

        @BindingAdapter("readJokeApiResponse","readFoodJokeDatabase",requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(view: View,
                                         readApiResponse: NetworkResult<FoodJoke>?,
                                         database: List<FoodJokeEntity>?){
            when(readApiResponse){
                is NetworkResult.Loading -> {
                    when(view){
                        is ProgressBar -> {
                            view.visibility = View.VISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.INVISIBLE
                        }
                    }
                }

                is NetworkResult.Error -> {
                    when(view){
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if(database != null){
                                if(database.isEmpty()){
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }

                is NetworkResult.Success -> {
                    when(view){
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }


            }
        }

        @BindingAdapter("readJokeApiResponse2","readFoodJokeDatabase2",requireAll = true)
        @JvmStatic
        fun setErrorViews(view: View,apiResponse:NetworkResult<FoodJoke>?,
                          database: List<FoodJokeEntity>?){
            if(database != null){
                if(!database?.isEmpty()){
                    view.visibility = View.VISIBLE
                    if(view is TextView){
                        if(apiResponse != null){
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }
            if(apiResponse is NetworkResult.Success || apiResponse is NetworkResult.Loading){
                view.visibility = View.INVISIBLE
            }
        }
    }
}