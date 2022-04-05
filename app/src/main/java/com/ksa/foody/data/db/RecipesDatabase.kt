package com.ksa.foody.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ksa.foody.data.db.entities.FavoritesEntity
import com.ksa.foody.data.db.entities.FoodJokeEntity
import com.ksa.foody.data.db.entities.RecipesEntity

@Database(entities = [RecipesEntity::class,FavoritesEntity::class,FoodJokeEntity::class],version = 2,exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase :RoomDatabase(){

    abstract fun recipeDao(): RecipeDao
}