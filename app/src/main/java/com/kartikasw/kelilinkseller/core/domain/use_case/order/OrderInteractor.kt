package com.kartikasw.kelilinkseller.core.domain.use_case.order

import com.kartikasw.kelilinkseller.core.data.source.remote.service.FirestoreQueryLiveData
import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.core.domain.model.Fcm
import com.kartikasw.kelilinkseller.core.domain.model.Invoice
import com.kartikasw.kelilinkseller.core.domain.model.Order
import com.kartikasw.kelilinkseller.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderInteractor @Inject constructor(
    private val orderRepository: OrderRepository
) : OrderUseCase {
    override fun getAllLiveOrder(): FirestoreQueryLiveData<Invoice> =
        orderRepository.getAllLiveOrder()

    override fun getLiveOrderMenu(invoiceId: String): FirestoreQueryLiveData<Order> =
        orderRepository.getLiveOrderMenu(invoiceId)

    override fun getOrderById(orderId: String): Flow<Resource<Invoice>> =
        orderRepository.getOrderById(orderId)

    override fun acceptOrder(invoiceId: String): Flow<Resource<Unit>> =
        orderRepository.acceptOrder(invoiceId)

    override fun declineOrder(invoiceId: String): Flow<Resource<Unit>> =
        orderRepository.declineOrder(invoiceId)

    override fun markOrderAsReady(invoiceId: String): Flow<Resource<Unit>> =
        orderRepository.markOrderAsReady(invoiceId)

    override fun markOrderAsDone(invoiceId: String): Flow<Resource<Unit>> =
        orderRepository.markOrderAsDone(invoiceId)

    override fun sendFcm(data: Fcm): Flow<Resource<Unit>> =
        orderRepository.sendFcm(data)

    override fun setInvoiceId(id: String) =
        orderRepository.setInvoiceId(id)

    override fun getInvoiceId(): String =
        orderRepository.getInvoiceId()

}