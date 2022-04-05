package com.ksa.foody.models


import android.os.Parcelable
import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Result(
    @Nullable
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int,
    @Nullable
    @SerializedName("author")
    val author: String?,
    @Nullable
    @SerializedName("cheap")
    val cheap: Boolean,
    @Nullable
    @SerializedName("dairyFree")
    val dairyFree: Boolean,
    @Nullable
    @SerializedName("extendedIngredients")
    val extendedIngredients: @RawValue List<ExtendedIngredient>,
    @Nullable
    @SerializedName("glutenFree")
    val glutenFree: Boolean,
    @Nullable
    @SerializedName("id")
    val id: Int,
    @Nullable
    @SerializedName("image")
    val image: String,
    @Nullable
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @Nullable
    @SerializedName("sourceName")
    val sourceName: String?,
    @Nullable
    @SerializedName("sourceUrl")
    val sourceUrl: String,
    @Nullable
    @SerializedName("summary")
    val summary: String,
    @Nullable
    @SerializedName("title")
    val title: String,
    @Nullable
    @SerializedName("vegan")
    val vegan: Boolean,
    @Nullable
    @SerializedName("vegetarian")
    val vegetarian: Boolean,
    @Nullable
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean,

    ) : Parcelable