package com.example.kelilinkseller.features.auth.register

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.core.domain.use_case.auth.AuthUseCase
import com.example.kelilinkseller.core.domain.use_case.seller.SellerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val sellerUseCase: SellerUseCase
): ViewModel() {

    private val _uriImage = MutableLiveData<Uri?>()
    val uriImage: LiveData<Uri?> = _uriImage

    fun setUriImage(uri: Uri?) {
        _uriImage.value = uri
    }

    fun register(email: String, password: String, seller: Seller, store: Store, uri: Uri) =
        authUseCase.register(email, password, seller, store, uri).asLiveData()

    fun getFcmToken() =
        sellerUseCase.getFcmToken()

}