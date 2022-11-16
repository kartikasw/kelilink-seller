package com.example.kelilinkseller.features.store.profile.edit

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    private val _uriImage = MutableLiveData<Uri?>()
    val uriImage: LiveData<Uri?> = _uriImage

    fun setUriImage(uri: Uri?) {
        _uriImage.value = uri
    }

    fun getStoreById(storeId: String): LiveData<Resource<Store>> =
        storeUseCase.getStoreById(storeId).asLiveData()

    fun updateMyStore(store: MutableMap<String, Any>, uri: Uri) =
        storeUseCase.updateMyStore(store, uri).asLiveData()

    fun updateMyStore(store: MutableMap<String, Any>) =
        storeUseCase.updateMyStore(store).asLiveData()

}