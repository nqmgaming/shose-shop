package com.nqmgaming.shoseshop.data.model.main.user

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("phoneNumber") val phoneNumber: String?
)