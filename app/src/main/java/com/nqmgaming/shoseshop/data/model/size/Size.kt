package com.nqmgaming.shoseshop.data.model.size

import com.google.gson.annotations.SerializedName

data class Size(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val size: List<SizeDetail>,
    val createdAt: String,
    val updatedAt: String,
)
data class SizeDetail(
    val id: Int,
    val name: String
)