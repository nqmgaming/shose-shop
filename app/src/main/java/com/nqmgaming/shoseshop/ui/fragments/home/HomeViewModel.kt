package com.nqmgaming.shoseshop.ui.fragments.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.main.category.Category
import com.nqmgaming.shoseshop.data.model.main.category.CategoryResponse
import com.nqmgaming.shoseshop.data.model.main.product.Product
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ShoesRepository
) : ViewModel() {
    private suspend fun getCategories(token: String): CategoryResponse =
        repository.getCategories(token)

    private suspend fun getCategoryById(token: String, id: String) =
        repository.getCategoryById(token, id)

    private suspend fun getProducts(token: String) = repository.getProducts(token)

    private suspend fun getProductByCategory(token: String, categoryId: String) =
        repository.getProductsByCategory(token, categoryId)

    fun getCategoriesHome(token: String, callback: (List<Category>?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = getCategories(token)
                val categories = response.data
                callback(categories)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error getting categories: ${e.message}")
                callback(null)
            }
        }
    }

    fun getProductsHome(token: String, callback: (List<Product>?) -> Unit) {
        viewModelScope.launch {
            try {
                val products = getProducts(token)
                callback(products)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error getting products: ${e.message}")
                callback(null)
            }
        }
    }

    fun getProductsByCategoryHome(
        token: String,
        categoryId: String,
        callback: (List<Product>?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val products = getProductByCategory(token, categoryId)
                callback(products)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error getting products by category: ${e.message}")
                callback(null)
            }
        }
    }
}