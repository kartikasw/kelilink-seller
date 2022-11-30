package com.example.kelilinkseller.features.order.ready

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.model.Order
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import com.example.kelilinkseller.features.order.FirestoreQueryLiveData
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