package com.nqmgaming.shoseshop.ui.activities.checkout

import androidx.lifecycle.ViewModel
import com.nqmgaming.shoseshop.data.repository.ShoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val shoesRepository: ShoesRepository
):ViewModel(){

}