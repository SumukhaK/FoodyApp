package com.ksa.foody.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ksa.foody.models.FoodRecipe
import com.ksa.foody.util.Constants

@Entity(tableName = Constants.RECIPE_TABLE)
class RecipesEntity (var foodRecipe: FoodRecipe) {

    @PrimaryKey(autoGenerate = false)
    var id:Int = 0


}