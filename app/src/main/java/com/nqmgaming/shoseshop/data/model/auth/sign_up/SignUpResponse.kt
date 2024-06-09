package com.nqmgaming.shoseshop.data.model.auth.sign_up

import com.google.gson.annotations.SerializedName

data class UserSignUp(
    @SerializedName("_id")
    val id: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val address: String,
    val phoneNumber: String
)

data class SignUpResponse(
    val data: UserSignUp,
    val accessToken: String
)