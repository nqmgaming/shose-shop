package com.nqmgaming.shoseshop.data.model.main.cart

import com.nqmgaming.shoseshop.data.model.main.product.Product

data class ItemCart(
    val product: String,
    var quantity: Int,
    val size: String,
    val price: Double,
)
