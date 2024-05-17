package com.nqmgaming.shoseshop.data.remote

import com.nqmgaming.shoseshop.data.model.category.CategoryResponse
import com.nqmgaming.shoseshop.data.model.product.Product
import com.nqmgaming.shoseshop.data.model.signIn.SignInRequest
import com.nqmgaming.shoseshop.data.model.signIn.SignInResponse
import com.nqmgaming.shoseshop.data.model.signUp.SignUpRequest
import com.nqmgaming.shoseshop.data.model.signUp.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    /***
     * This part is for product
     */

    // GET request to get all products
    @GET("/products")
    suspend fun getProducts(@Header("Authorization") token: String): List<Product>

    // GET request to get product by id
    @GET("/products/{id}")
    suspend fun getProductById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Product

    // GET request to search product by name
    @GET("/products/search")
    suspend fun searchProductByName(
        @Header("Authorization") token: String,
        @Query("name") name: String
    ): List<Product>

    // PATCH request to update product stock
    @PATCH("/products/{id}")
    suspend fun updateProductStock(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body stock: Int
    ): Product
}