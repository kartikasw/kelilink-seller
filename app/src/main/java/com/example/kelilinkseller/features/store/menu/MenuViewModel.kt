package com.example.kelilinkseller.features.store.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    fun getMyMenu() =
        storeUseCase.getMyMenu().asLiveData()

}