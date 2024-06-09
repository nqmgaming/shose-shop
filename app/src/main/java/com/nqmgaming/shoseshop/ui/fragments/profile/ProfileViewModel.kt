package com.nqmgaming.shoseshop.ui.fragments.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nqmgaming.shoseshop.data.model.main.user.User
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val shoesRepository: ShoesRepository
) : ViewModel() {
    private suspend fun updateUserImageProfile(
        token: String,
        userId: String,
        image: MultipartBody.Part
    ): Response<User> {
        return shoesRepository.updateUserImageProfile(token, userId, image)
    }

    fun updateUserImageProfile(
        token: String,
        userId: String,
        image: MultipartBody.Part,
        callback: (User?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val user = updateUserImageProfile(token, userId, image)
                Log.d("ProfileViewModel", "User image profile updated ${user.body()}")
                callback(user.body())
            } catch (e: Exception) {
                Log.e(
                    "ProfileViewModel",
                    "Error updating user image profile: ${e.printStackTrace()}"
                )
                callback(null)
            }
        }
    }
}