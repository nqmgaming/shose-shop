package com.nqmgaming.shoseshop.ui.activities.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.auth.sign_up.SignUpRequest
import com.nqmgaming.shoseshop.data.model.auth.sign_up.SignUpResponse
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: ShoesRepository
) : ViewModel() {
    private suspend fun signUpUser(signUpRequest: SignUpRequest): SignUpResponse =
        repository.signUp(signUpRequest)

    fun signUpUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        birthDate: String,
        address: String,
        phoneNumber: String,
        callback: (SignUpResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val signUpRequest = SignUpRequest(
                    email,
                    password,
                    firstName,
                    lastName,
                    birthDate,
                    address,
                    phoneNumber
                )
                val response = signUpUser(signUpRequest)
                callback(response)
            } catch (e: Exception) {
                Log.e("SignUpViewModel", "Error signing up: ${e.message}")
                callback(null)
            }
        }
    }
}