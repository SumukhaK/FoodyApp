package com.ksa.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ksa.foody.adapters.FavouriteRecipesAdapter
import com.ksa.foody.data.db.entities.FavoritesEntity

class FavouriteRecipeBiding {

    companion object{
      /*  @JvmStatic
        @BindingAdapter("viewVisibility","setData",requireAll = false)
        fun setDataAndViewVisibility(view: View,favoritesEntities: List<FavoritesEntity>?,
                                     mAdapter:FavouriteRecipesAdapter?){

            if(favoritesEntities.isNullOrEmpty()){
                when(view){
                    is ImageView ->{
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.GONE
                    }
                }
            }else{
                when(view){
                    is ImageView ->{
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE

                    }
                }
            }
        }*/


        @BindingAdapter("setVisibility","setData",requireAll = false)
        @JvmStatic
        fun setVisibility(view:View,favoritesEntities: List<FavoritesEntity>?,mAdapter:FavouriteRecipesAdapter?){
            when(view){
                is RecyclerView -> {
                    val dataCheck = favoritesEntities.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if(!dataCheck) {
                        favoritesEntities?.let {
                            mAdapter?.setData(it)
                        }
                    }
                }
                else -> view.isVisible = favoritesEntities.isNullOrEmpty()
            }
        }

    }
}