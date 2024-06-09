package com.nqmgaming.shoseshop.ui.activities.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.main.order.Order
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val shoesRepository: ShoesRepository
) : ViewModel() {
    private suspend fun createOrder(token: String, order: Order) =
        shoesRepository.createOrder(token, order)

    private suspend fun getProductStock(token: String, id: String) =
        shoesRepository.getProductById(token, id).stock

    private suspend fun updateProductStock(token: String, id: String, map: Map<String, Int>) =
        shoesRepository.updateProductStock(token, id, map)

    private suspend fun deleteAllCart(token: String, userId: String) =
        shoesRepository.deleteAllCart(token, userId)

    fun createOrderOrder(token: String, order: Order, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val isOrderCreated = createOrder(token, order)
                callback(true)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error creating order: ${e.message}")
                callback(false)
            }
        }
    }

    fun getProductStockOrder(token: String, id: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                val stock = getProductStock(token, id)
                callback(stock)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error getting product stock: ${e.message}")
                callback(0)
            }
        }
    }

    fun updateProductStockOrder(
        token: String,
        id: String,
        stock: Map<String, Int>,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val isStockUpdated = updateProductStock(token, id, stock)
                callback(true)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error updating product stock: ${e.message}")
                callback(false)
            }
        }
    }

    fun deleteAllCartOrder(token: String, userId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val isCartDeleted = deleteAllCart(token, userId)
                callback(true)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error deleting all cart: ${e.message}")
                callback(false)
            }
        }
    }
}