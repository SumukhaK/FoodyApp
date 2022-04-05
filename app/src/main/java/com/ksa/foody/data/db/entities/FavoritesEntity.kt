package com.ksa.foody.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ksa.foody.models.Result
import com.ksa.foody.util.Constants.Companion.FAV_RECIPE_TABLE

@Entity(tableName = FAV_RECIPE_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var result:Result)