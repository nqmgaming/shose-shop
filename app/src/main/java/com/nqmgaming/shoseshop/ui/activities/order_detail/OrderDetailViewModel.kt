package com.nqmgaming.shoseshop.ui.activities.order_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.main.order.Order
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val repository: ShoesRepository
) : ViewModel() {
    private suspend fun getOrderById(token: String, id: String) = repository.getOrderById(token, id)

    fun getOrderByIdDetail(token: String, id: String, callback: (Order?) -> Unit) {
        viewModelScope.launch {
            try {
                val order = getOrderById(token, id)
                callback(order)
            } catch (e: Exception) {
                Log.e("OrderDetailViewModel", "Error getting order: ${e.message}")
                callback(null)
            }
        }
    }
}