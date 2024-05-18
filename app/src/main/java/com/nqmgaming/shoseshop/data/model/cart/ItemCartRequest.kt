package com.nqmgaming.shoseshop.data.model.cart

import com.nqmgaming.shoseshop.data.model.product.Product

data class ItemCartRequest(
    val product: String,
    val quantity: Int,
    val size: String,
    val price: Double,
)