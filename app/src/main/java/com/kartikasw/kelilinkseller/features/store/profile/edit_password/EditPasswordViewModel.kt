package com.kartikasw.kelilinkseller.features.store.profile.edit_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilinkseller.core.domain.use_case.seller.SellerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EditPasswordViewModel  @Inject constructor(
    private val sellerUseCase: SellerUseCase
): ViewModel() {

    fun updatePassword(oldPassword: String, newPassword: String) =
        sellerUseCase.updatePassword(oldPassword, newPassword).asLiveData()

}