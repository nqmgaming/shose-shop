package com.nqmgaming.shoseshop.ui.activities.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.order.Order
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