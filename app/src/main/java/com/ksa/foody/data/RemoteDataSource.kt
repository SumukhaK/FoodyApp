package com.ksa.foody.data

import com.ksa.foody.models.FoodJoke
import com.ksa.foody.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodyRecipesAPIs: FoodyRecipesAPIs
){

    suspend fun getRecipes(queries:Map<String,String>): Response<FoodRecipe>{
        return foodyRecipesAPIs.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery:Map<String,String>):Response<FoodRecipe>{
        return foodyRecipesAPIs.getSearchResult(searchQuery)
    }

    suspend fun getFoodJoke(apiKey:String) : Response<FoodJoke>{
        return foodyRecipesAPIs.getRandomFoodJoke(apiKey)
    }
}