package com.ksa.foody.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.ksa.foody.R
import com.ksa.foody.models.Result
import com.ksa.foody.ui.fragments.RecipeFragment
import com.ksa.foody.ui.fragments.RecipeFragmentDirections
import org.jsoup.Jsoup

class RecipesRowBinding {

    companion object{

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromURL(imageView: ImageView,imageUrl:String){

            imageView.load(imageUrl){
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }

        }

      /*  @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView,likes:Int){
            textView.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView,minutes:Int){
            textView.text = minutes.toString()
        }*/

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view:View,isVegan:Boolean){

            if(isVegan){
                when(view){
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }

                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }        }


        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout : ConstraintLayout,result:Result){

            recipeRowLayout.setOnClickListener {
                try{
                    val action = RecipeFragmentDirections.actionRecipeFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                }catch (exception:Exception){
                    Log.v("onRecipeClickListener Excpetion ",exception.toString())
                }
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView,
        description:String?){
            if(description != null){
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }

    }
}