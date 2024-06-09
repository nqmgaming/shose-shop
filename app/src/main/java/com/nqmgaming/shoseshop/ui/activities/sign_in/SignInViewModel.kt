package com.nqmgaming.shoseshop.ui.activities.sign_in

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.auth.sign_in.SignInRequest
import com.nqmgaming.shoseshop.data.model.auth.sign_in.SignInResponse
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: ShoesRepository
) : ViewModel() {
    private suspend fun signIn(signInRequest: SignInRequest): SignInResponse =
        repository.signIn(signInRequest)

    fun signInUser(email: String, password: String, callback: (SignInResponse?) -> Unit) {
        viewModelScope.launch {
            try {
                val signInRequest = SignInRequest(email, password)
                val response = repository.signIn(signInRequest)
                callback(response)
            } catch (e: Exception) {
                Log.e("SignInViewModel", "Error signing in: ${e.message}")
                callback(null)
            }
        }
    }
}