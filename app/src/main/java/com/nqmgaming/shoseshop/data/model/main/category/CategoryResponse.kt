package com.nqmgaming.shoseshop.data.model.main.category

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    val data: List<Category>
)

data class Category(
    @SerializedName("_id")
    val id: String,
    val name: String,
)