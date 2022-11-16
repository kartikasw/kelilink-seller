package com.example.kelilinkseller.features.order.ready

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderReadyViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    fun getAllReadyOrder() =
        orderUseCase.getAllReadyOrder().asLiveData()

    fun updateOrderStatus(invoiceId: String, status: String) =
        orderUseCase.updateOrderStatus(invoiceId, status).asLiveData()

}