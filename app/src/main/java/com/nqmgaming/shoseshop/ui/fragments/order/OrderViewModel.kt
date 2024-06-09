package com.nqmgaming.shoseshop.ui.fragments.order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.main.order.Order
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val shoesRepository: ShoesRepository,
    application: Application
) : AndroidViewModel(application) {
    private val token =
        SharedPrefUtils.getString(application.applicationContext, "accessToken", "") ?: ""
    private val bearerToken = "Bearer $token"
    private val userId = SharedPrefUtils.getString(application.applicationContext, "id", "") ?: ""
    fun getOrders(status: String, callback: (List<Order>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = shoesRepository.getOrders(bearerToken, userId)
                val filteredOrders = response.filter { it.status == status }
                callback(filteredOrders)
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error getting orders: ${e.message}")
                callback(emptyList())
            }
        }
    }
}