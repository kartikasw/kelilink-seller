package com.example.kelilinkseller.features.order.new_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.model.Order
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import com.example.kelilinkseller.features.order.FirestoreQueryLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderNewViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    fun getAllNewOrder(): FirestoreQueryLiveData<Invoice> =
        orderUseCase.getAllLiveOrder()

    fun getAllOrderMenu(invoiceId: String): FirestoreQueryLiveData<Order> =
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