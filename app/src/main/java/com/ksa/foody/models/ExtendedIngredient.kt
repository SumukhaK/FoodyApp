package com.ksa.foody.models


import android.os.Parcelable
import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ExtendedIngredient(
    @Nullable
    @SerializedName("amount")
    val amount: Double?,
    @Nullable
    @SerializedName("consistency")
    val consistency: String?,
    @Nullable
    @SerializedName("image")
    val image: String?,
    @Nullable
    @SerializedName("name")
    val name: String?,
    @Nullable
    @SerializedName("original")
    val original: String?,
    @Nullable
    @SerializedName("unit")
    val unit: String?
):Parcelable