package com.nqmgaming.shoseshop.ui.activities.product_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.main.cart.Cart
import com.nqmgaming.shoseshop.data.model.main.cart.CartRequest
import com.nqmgaming.shoseshop.data.model.main.product.Product
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ShoesRepository
) : ViewModel() {
    private suspend fun getProductDetail(token: String, id: String) =
        repository.getProductById(token, id)

    private suspend fun getProductsByCategory(token: String, categoryId: String) =
        repository.getProductsByCategory(token, categoryId)

    private suspend fun createCart(token: String, cartRequest: CartRequest) =
        repository.createCart(token, cartRequest)

    fun getProductDetail(token: String, id: String, callback: (Product?) -> Unit) {
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

    fun getProductsByCategory(
        token: String,
        categoryId: String,
        callback: (List<Product>?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val products = getProductsByCategory(token, categoryId)
                callback(products)
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error getting products by category: ${e.message}")
                callback(null)
            }
        }
    }

    fun createCart(token: String, cartRequest: CartRequest, callback: (Cart?) -> Unit) {
        viewModelScope.launch {
            try {
                val isCartCreated = createCart(token, cartRequest)
                callback(isCartCreated)
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error creating cart: ${e.message}")
                callback(null)
            }
        }
    }
}