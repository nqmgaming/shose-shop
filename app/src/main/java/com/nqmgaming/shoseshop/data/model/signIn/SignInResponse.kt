package com.nqmgaming.shoseshop.data.model.signIn

import com.google.gson.annotations.SerializedName

data class UserSignIn(
    @SerializedName("_id")
    val id: String,
    val email: String,
    val avatar: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val address: String,
    val phoneNumber: String
)

data class SignInResponse(
    val data: UserSignIn,
    val accessToken: String
)