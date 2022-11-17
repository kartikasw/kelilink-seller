package com.example.kelilinkseller.features.order.done.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailOrderDoneViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {
    fun getInvoiceId(): String = orderUseCase.getInvoiceId()

    fun getOrderById(orderId: String) =
        orderUseCase.getOrderById(orderId).asLiveData()
}