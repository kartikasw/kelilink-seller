package com.kartikasw.kelilinkseller.features.order.new_order.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilinkseller.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailOrderNewViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {
    fun getInvoiceId(): String = orderUseCase.getInvoiceId()

    fun getOrderById(orderId: String) =
        orderUseCase.getOrderById(orderId).asLiveData()

    fun markOrderAsReady(invoiceId: String) =
        orderUseCase.markOrderAsReady(invoiceId).asLiveData()
}