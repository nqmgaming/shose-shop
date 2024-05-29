package com.nqmgaming.shoseshop.ui.fragments.order

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
    private suspend fun getOrders(token: String, userId: String) =
        shoesRepository.getOrders(token, userId)

    fun getOrdersOrder(token: String, userId: String, callback: (List<Order>?) -> Unit) {
        viewModelScope.launch {
            try {
                val orders = getOrders(token, userId)
                callback(orders)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error getting orders: ${e.message}")
                callback(null)
            }
        }
    }
}