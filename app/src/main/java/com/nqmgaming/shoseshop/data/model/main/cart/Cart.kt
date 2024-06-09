package com.nqmgaming.shoseshop.data.model.main.cart

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("_id")
    val id: String,
    val user: String,
    val items: ItemCart,
)