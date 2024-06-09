package com.nqmgaming.shoseshop.ui.fragments.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.main.cart.Cart
import com.nqmgaming.shoseshop.data.model.main.product.Product
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ShoesRepository
) : ViewModel() {
    private suspend fun getAllCarts(token: String, userId: String) =
        repository.getAllCarts(token, userId)

    private suspend fun updateQuantity(token: String, id: String, map: Map<String, Int>) =
        repository.updateCart(token, id, map)

    private suspend fun getProductDetail(token: String, id: String) =
        repository.getProductById(token, id)
    private suspend fun deleteCart(token: String, id: String) = repository.deleteCart(token, id)

    fun getAllCartsMain(token: String, userId: String, callback: (List<Cart>?) -> Unit) {
        viewModelScope.launch {
            try {
                val carts = getAllCarts(token, userId)
                callback(carts.data)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error getting all carts: ${e.message}")
                callback(null)
            }
        }
    }

    fun updateQuantityMain(
        token: String,
        id: String,
        quantity: Map<String, Int>,
        callback: (Cart?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val isQuantityUpdated = updateQuantity(token, id, quantity)
                callback(isQuantityUpdated)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error updating quantity: ${e.message}")
                callback(null)
            }
        }
    }

    fun getProductDetailMain(token: String, id: String, callback: (Product?) -> Unit) {
        viewModelScope.launch {
            try {
                val productDetail = getProductDetail(token, id)
                callback(productDetail)
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error getting product detail: ${e.message}")
                callback(null)
            }
        }
    }

    fun deleteCartMain(token: String, id: String, callback: () -> Unit) {
        viewModelScope.launch {
            try {
                val isDeleted = deleteCart(token, id)
                callback()
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error deleting cart: ${e.message}")
                callback()
            }
        }
    }
}