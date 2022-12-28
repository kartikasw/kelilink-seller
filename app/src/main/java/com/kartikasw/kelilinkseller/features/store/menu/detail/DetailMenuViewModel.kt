package com.kartikasw.kelilinkseller.features.store.menu.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailMenuViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    fun getMenuById(menuId: String) =
        storeUseCase.getMenuById(menuId).asLiveData()

    fun deleteMenu(menuId: String) =
        storeUseCase.deleteMenu(menuId).asLiveData()

}