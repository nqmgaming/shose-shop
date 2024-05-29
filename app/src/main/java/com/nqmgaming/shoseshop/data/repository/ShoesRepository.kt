package com.nqmgaming.shoseshop.data.repository

import com.nqmgaming.shoseshop.data.model.cart.CartRequest
import com.nqmgaming.shoseshop.data.model.cart.CartResponse
import com.nqmgaming.shoseshop.data.model.category.CategoryResponse
import com.nqmgaming.shoseshop.data.model.order.Order
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

    suspend fun getProductsByCategory(token: String, categoryId: String) =
        shoesApi.getProductsByCategoryId(token, categoryId)

    suspend fun updateProductStock(token: String, id: String, stock: Int) =
        shoesApi.updateProductStock(token, id, stock)

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
}