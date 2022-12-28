package com.kartikasw.kelilinkseller.features.order.ready

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilinkseller.core.data.source.remote.service.FirestoreQueryLiveData
import com.kartikasw.kelilinkseller.core.domain.model.Invoice
import com.kartikasw.kelilinkseller.core.domain.model.Order
import com.kartikasw.kelilinkseller.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderReadyViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    fun getAllReadyOrderLiveData(): FirestoreQueryLiveData<Invoice> =
        orderUseCase.getAllLiveOrder()

    fun getAllOrderMenuLiveData(invoiceId: String): FirestoreQueryLiveData<Order> =
        orderUseCase.getLiveOrderMenu(invoiceId)

    fun setInvoiceId(id: String) {
        orderUseCase.setInvoiceId(id)
    }

    fun markOrderAsDone(invoiceId: String) =
        orderUseCase.markOrderAsDone(invoiceId).asLiveData()

}