package com.ksa.foody.data.db

import androidx.room.*
import com.ksa.foody.data.db.entities.FavoritesEntity
import com.ksa.foody.data.db.entities.FoodJokeEntity
import com.ksa.foody.data.db.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavRecipe(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Query("SELECT * FROM fav_recipes ORDER BY id ASC")
    fun readFavouriteRecipes():Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM fav_recipes")
    suspend fun deleteAllFavRecipes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke ORDER BY id ASC")
    fun readFoodJoke():Flow<List<FoodJokeEntity>>
}