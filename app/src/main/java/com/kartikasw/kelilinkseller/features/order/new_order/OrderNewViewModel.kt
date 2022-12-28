package com.kartikasw.kelilinkseller.features.order.new_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilinkseller.core.domain.model.Fcm
import com.kartikasw.kelilinkseller.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderNewViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    fun getAllNewOrder() =
        orderUseCase.getAllLiveOrder()

    fun getAllOrderMenu(invoiceId: String) =
        orderUseCase.getLiveOrderMenu(invoiceId)

    fun setInvoiceId(id: String) {
        orderUseCase.setInvoiceId(id)
    }

    fun acceptOrder(invoiceId: String) =
        orderUseCase.acceptOrder(invoiceId).asLiveData()

    fun declineOrder(invoiceId: String) =
        orderUseCase.declineOrder(invoiceId).asLiveData()

    fun markOrderAsReady(invoiceId: String) =
        orderUseCase.markOrderAsReady(invoiceId).asLiveData()

    fun sendFcm(data: Fcm) =
        orderUseCase.sendFcm(data).asLiveData()
}