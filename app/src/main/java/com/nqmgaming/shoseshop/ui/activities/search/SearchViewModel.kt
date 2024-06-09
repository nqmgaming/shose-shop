package com.nqmgaming.shoseshop.ui.activities.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.main.product.Product
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val shoesRepository: ShoesRepository
) : ViewModel() {

    private suspend fun searchProducts(token: String, name: String) =
        shoesRepository.searchProductByName(token, name)

    fun searchProductsSearch(token: String, name: String, callback: (List<Product>?) -> Unit) {
        viewModelScope.launch {
            try {
                val products = searchProducts(token, name)
                callback(products)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error searching products: ${e.message}")
                callback(emptyList())
            }
        }
    }

}