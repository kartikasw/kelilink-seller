package com.kartikasw.kelilinkseller.features.store.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    fun getMyMenu() =
        storeUseCase.getMyMenu().asLiveData()

}