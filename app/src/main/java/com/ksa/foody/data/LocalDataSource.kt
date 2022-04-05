package com.ksa.foody.data

import com.ksa.foody.data.db.RecipeDao
import com.ksa.foody.data.db.entities.FavoritesEntity
import com.ksa.foody.data.db.entities.FoodJokeEntity
import com.ksa.foody.data.db.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(
    private val recipeDao: RecipeDao
){


    fun readRecipesDatabase(): Flow<List<RecipesEntity>> {
        return recipeDao.readRecipes()
    }

    fun readFavouriteRecipes():Flow<List<FavoritesEntity>>{
        return recipeDao.readFavouriteRecipes()
    }

    suspend fun insertRecipe(recipesEntity: RecipesEntity){
        recipeDao.insertRecipe(recipesEntity)
    }

    suspend fun insertFavouriteRecipes(favoritesEntity: FavoritesEntity){
        recipeDao.insertFavRecipe(favoritesEntity)
    }

    suspend fun deleteFavRecipe(favoritesEntity: FavoritesEntity){
        recipeDao.deleteFavRecipe(favoritesEntity)
    }

    suspend fun deleteAllFavRecipes(){
        recipeDao.deleteAllFavRecipes()
    }

    fun readFoodJokes():Flow<List<FoodJokeEntity>>{
        return recipeDao.readFoodJoke()
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        return recipeDao.insertFoodJoke(foodJokeEntity)
    }
}