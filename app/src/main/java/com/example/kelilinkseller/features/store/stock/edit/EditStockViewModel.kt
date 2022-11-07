package com.example.kelilinkseller.features.store.stock.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditStockViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {
    fun updateMenuStock(menuId: String, stock: Boolean) =
        storeUseCase.updateMenuStock(menuId, stock).asLiveData()
}