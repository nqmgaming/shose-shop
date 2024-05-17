package com.nqmgaming.shoseshop.data.remote

import com.nqmgaming.shoseshop.data.model.category.CategoryResponse
import com.nqmgaming.shoseshop.data.model.signIn.SignInRequest
import com.nqmgaming.shoseshop.data.model.signIn.SignInResponse
import com.nqmgaming.shoseshop.data.model.signUp.SignUpRequest
import com.nqmgaming.shoseshop.data.model.signUp.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ShoesApi {

    // All requests are have authorization header with token except sign in and sign up

    /***
     * This part is for user authentication
     */

    // GET request to check user exist using email
    @GET("/users/checkUserExits/{email}")
    suspend fun checkUserExist(@Path("email") email: String): Boolean

    // POST request to sign in user
    @POST("/users/signIn")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    // POST request to sign up user
    @POST("/users/signUp")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): SignUpResponse

    /***
     * This part is for category
     */

    // GET request to get all categories
    @GET("/categories")
    suspend fun getCategories(@Header("Authorization") token: String): CategoryResponse

    // GET request to get category by id
    @GET("/categories/{id}")
    suspend fun getCategoryById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): CategoryResponse
}