package com.nqmgaming.shoseshop.data.repository

import com.nqmgaming.shoseshop.data.model.main.cart.CartRequest
import com.nqmgaming.shoseshop.data.model.main.cart.CartResponse
import com.nqmgaming.shoseshop.data.model.main.category.CategoryResponse
import com.nqmgaming.shoseshop.data.model.main.order.Order
import com.nqmgaming.shoseshop.data.model.auth.sign_in.SignInRequest
import com.nqmgaming.shoseshop.data.model.auth.sign_up.SignUpRequest
import com.nqmgaming.shoseshop.data.model.main.user.User
import com.nqmgaming.shoseshop.data.remote.ShoesApi
import okhttp3.MultipartBody
import retrofit2.Response
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

    suspend fun getProductsByCategory(token: String, categoryId: String) =
        shoesApi.getProductsByCategoryId(token, categoryId)

    suspend fun updateProductStock(token: String, id: String, map: Map<String, Int>) =
        shoesApi.updateProductStock(token, id, map)

    suspend fun getAllCarts(token: String, userId: String): CartResponse =
        shoesApi.getCartItemsByUserId(token, userId)


    suspend fun createCart(token: String, cartRequest: CartRequest) =
        shoesApi.addProductToCart(token, cartRequest)

    suspend fun updateCart(token: String, id: String, map: Map<String, Int>) =
        shoesApi.updateCartItemQuantity(token, id, map)

    suspend fun deleteCart(token: String, id: String) = shoesApi.deleteCartItem(token, id)

    suspend fun deleteAllCart(token: String, userId: String) =
        shoesApi.deleteAllCartItemsByUserId(token, userId)

    suspend fun createOrder(token: String, order: Order) = shoesApi.createOrder(token, order)

    suspend fun getOrders(token: String, userId: String) = shoesApi.getOrdersByUserId(token, userId)
    suspend fun getOrderById(token: String, id: String) = shoesApi.getOrder(token, id)

    suspend fun updateUserImageProfile(
        token: String,
        userId: String,
        image: MultipartBody.Part
    ): Response<User> =
        shoesApi.updateAvatar(token, userId, image)
}