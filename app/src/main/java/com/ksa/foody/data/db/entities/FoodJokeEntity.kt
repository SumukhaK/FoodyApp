package com.ksa.foody.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ksa.foody.models.FoodJoke
import com.ksa.foody.util.Constants

@Entity(tableName = Constants.FOOD_JOKE_TABLE)
class FoodJokeEntity(@Embedded var foodJoke: FoodJoke) {

    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}