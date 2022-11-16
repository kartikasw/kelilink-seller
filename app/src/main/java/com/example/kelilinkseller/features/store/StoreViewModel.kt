package com.example.kelilinkseller.features.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
) : ViewModel() {

    private val _locationState = MutableLiveData<Boolean>()
    val locationState: LiveData<Boolean> = _locationState

    fun updateLocationState(state: Boolean) {
        _locationState.value = state
    }

    fun updateUserInfo(store: MutableMap<String, Any>) =
        storeUseCase.updateMyStore(store).asLiveData()

    fun getStoreById(storeId: String) =
        storeUseCase.getStoreById(storeId).asLiveData()

}