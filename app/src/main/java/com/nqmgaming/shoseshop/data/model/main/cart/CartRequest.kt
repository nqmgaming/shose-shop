package com.nqmgaming.shoseshop.data.model.main.cart

data class CartRequest(
    val user: String,
    val items: ItemCartRequest
)
