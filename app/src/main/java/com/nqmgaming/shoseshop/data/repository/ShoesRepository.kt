package com.nqmgaming.shoseshop.data.repository

import com.nqmgaming.shoseshop.data.model.category.CategoryResponse
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
    suspend fun getCategories(token: String): CategoryResponse = shoesApi.getCategories(token)
    suspend fun getCategoryById(token: String, id: String): CategoryResponse =
        shoesApi.getCategoryById(token, id)

    suspend fun getProducts(token: String) = shoesApi.getProducts(token)
    suspend fun getProductById(token: String, id: String) = shoesApi.getProductById(token, id)
    suspend fun searchProductByName(token: String, name: String) =
        shoesApi.searchProductByName(token, name)

    suspend fun updateProductStock(token: String, id: String, stock: Int) =
        shoesApi.updateProductStock(token, id, stock)
}