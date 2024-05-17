package com.nqmgaming.shoseshop.data.repository

import com.nqmgaming.shoseshop.data.model.signIn.SignInRequest
import com.nqmgaming.shoseshop.data.model.signUp.SignUpRequest
import com.nqmgaming.shoseshop.data.remote.ShoesApi
import javax.inject.Inject

class ShoesRepository @Inject constructor(
    private val shoesApi: ShoesApi
) {
    suspend fun checkUserExist(email: String) = shoesApi.checkUserExist(email)
    suspend fun signIn(signInRequest: SignInRequest) = shoesApi.signIn(signInRequest)
    suspend fun signUp(signUpRequest: SignUpRequest) = shoesApi.signUp(signUpRequest)
}