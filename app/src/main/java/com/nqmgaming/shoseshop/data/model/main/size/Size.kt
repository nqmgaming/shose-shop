package com.nqmgaming.shoseshop.data.model.main.size

import com.google.gson.annotations.SerializedName

data class Size(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val size: MutableList<SizeDetail>,
    val createdAt: String,
    val updatedAt: String,
) {
    override fun toString(): String {
        return this.name

    }
}

data class SizeDetail(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return this.name
    }
}