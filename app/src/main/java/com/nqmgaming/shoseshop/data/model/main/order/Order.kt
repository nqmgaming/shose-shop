package com.nqmgaming.shoseshop.data.model.main.order

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("_id")
    val id: String,
    @SerializedName("user")
    val userId: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("paymentMethod")
    val paymentMethod: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("products")
    val products: List<OrderProduct>,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
)

data class OrderProduct(
    @SerializedName("_id")
    val id: String,
    @SerializedName("product")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int,
)
