package com.nqmgaming.shoseshop.data.remote

import com.nqmgaming.shoseshop.data.model.main.cart.Cart
import com.nqmgaming.shoseshop.data.model.main.cart.CartRequest
import com.nqmgaming.shoseshop.data.model.main.cart.CartResponse
import com.nqmgaming.shoseshop.data.model.main.category.CategoryResponse
import com.nqmgaming.shoseshop.data.model.main.order.Order
import com.nqmgaming.shoseshop.data.model.main.product.Product
import com.nqmgaming.shoseshop.data.model.auth.sign_in.SignInRequest
import com.nqmgaming.shoseshop.data.model.auth.sign_in.SignInResponse
import com.nqmgaming.shoseshop.data.model.auth.sign_up.SignUpRequest
import com.nqmgaming.shoseshop.data.model.auth.sign_up.SignUpResponse
import com.nqmgaming.shoseshop.data.model.main.user.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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

    // GET request to get products by category id
    @GET("/products/category/{categoryId}")
    suspend fun getProductsByCategoryId(
        @Header("Authorization") token: String,
        @Path("categoryId") categoryId: String
    ): List<Product>

    // PATCH request to update product stock
    @PATCH("/products/{id}")
    suspend fun updateProductStock(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body map: Map<String, Int> // map of stock
    ): Product

    /***
     * This part is for cart
     */

    // GET request to get all cart items by user id
    @GET("/carts/user/{id}")
    suspend fun getCartItemsByUserId(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): CartResponse

    // POST request to add product to cart
    @POST("/carts")
    suspend fun addProductToCart(
        @Header("Authorization") token: String,
        @Body cart: CartRequest
    ): Cart

    // PATCH request to update cart item quantity
    @PATCH("/carts/{id}")
    suspend fun updateCartItemQuantity(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body quantity: Map<String, Int>
    ): Cart

    // DELETE request to delete cart item
    @DELETE("/carts/{id}")
    suspend fun deleteCartItem(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )

    // DELETE request to delete all cart items by user id
    @DELETE("/carts/user/{userId}")
    suspend fun deleteAllCartItemsByUserId(
        @Header("Authorization") token: String,
        @Path("userId") id: String
    )

    /**
     * This part is for order
     */

    // GET request to get all orders by user id
    @GET("/orders/user/{userId}")
    suspend fun getOrdersByUserId(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): List<Order>

    @GET("/orders/{id}")
    suspend fun getOrder(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Order

    // POST request to create order
    @POST("/orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body order: Order
    ): Order

    // PATCH request to update order status
    @PATCH("/orders/{id}")
    suspend fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body status: String
    ): Order

    // DELETE request to delete order
    @DELETE("/orders/{id}")
    suspend fun deleteOrder(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )

    /**
     * This part is for user
     */

    @Multipart
    @PATCH("users/updateUser/{id}")
    suspend fun updateAvatar(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part avatar: MultipartBody.Part
    ): Response<User>

}