package com.example.kelilinkseller.features.store.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    val menu = storeUseCase.getMyMenu().asLiveData()

}