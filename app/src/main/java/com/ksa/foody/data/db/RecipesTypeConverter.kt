package com.ksa.foody.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ksa.foody.models.FoodRecipe
import com.ksa.foody.models.Result

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String{
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data : String):FoodRecipe{

        val listType = object : TypeToken<FoodRecipe>(){}.type

        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun resultToFavString(result:Result):String{
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToFavResult(data: String):Result{
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data,listType)
    }
}