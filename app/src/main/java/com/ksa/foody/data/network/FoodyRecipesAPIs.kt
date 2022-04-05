package com.ksa.foody.data

import com.ksa.foody.models.FoodJoke
import com.ksa.foody.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodyRecipesAPIs {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String,String>): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun getSearchResult(@QueryMap searchQueries: Map<String,String>): Response<FoodRecipe>

    @GET("/food/jokes/random")
    suspend fun getRandomFoodJoke(@Query("apiKey") apiKey:String):Response<FoodJoke>


    //https://spoonacular.com/cdn/ingredients_100x100/
    //https://api.spoonacular.com/food/jokes/random
}