package com.example.kelilinkseller.features.store.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    fun logout(): Unit =
        storeUseCase.logout()

    fun getStoreById(storeId: String): LiveData<Resource<Store>> =
        storeUseCase.getStoreById(storeId).asLiveData()

}