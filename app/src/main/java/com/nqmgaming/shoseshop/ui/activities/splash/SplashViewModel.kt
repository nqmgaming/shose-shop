package com.nqmgaming.shoseshop.ui.activities.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: ShoesRepository
) : ViewModel() {
    private suspend fun checkUserExist(email: String) = repository.checkUserExist(email)
    fun checkEmailExits(email: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            Log.d("SplashViewModel", "Checking server connection")
            val isServerConnected = withContext(Dispatchers.IO) {
                try {
                    checkUserExist(email)
                } catch (e: Exception) {
                    Log.e("SplashViewModel", "Error checking server connection: ${e.message}")
                    false
                }
            }
            Log.d("SplashViewModel", "Server connected: $isServerConnected")
            callback(isServerConnected)
        }
    }
}
