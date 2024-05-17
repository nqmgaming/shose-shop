package com.nqmgaming.shoseshop.data.model.product

import com.google.gson.annotations.SerializedName
import com.nqmgaming.shoseshop.data.model.category.Category
import com.nqmgaming.shoseshop.data.model.size.Size

data class Product(
    @SerializedName("_id")
    val id: String,
    val category: Category,
    val createdAt: String,
    val description: String,
    val imagePreview: String,
    val images: List<String>,
    val name: String,
    val price: Long,
    val rating: Double,
    val size: Size,
    val stock: Int,
    val updatedAt: String
)
