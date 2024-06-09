package com.nqmgaming.shoseshop.data.model.main.cart

import com.nqmgaming.shoseshop.data.model.main.product.Product

data class ItemCartRequest(
    val product: String,
    val quantity: Int,
    val size: String,
    val price: Double,
)