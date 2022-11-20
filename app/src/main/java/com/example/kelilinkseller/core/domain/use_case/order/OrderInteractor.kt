package com.example.kelilinkseller.core.domain.use_case.order

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.model.Order
import com.example.kelilinkseller.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderInteractor @Inject constructor(
    private val orderRepository: OrderRepository
) : OrderUseCase {

    override fun getAllNewOrder(): Flow<List<Invoice>> =
        orderRepository.getAllNewOrder()

    override fun getAllReadyOrder(): Flow<List<Invoice>> =
        orderRepository.getAllReadyOrder()

    override fun getAllDoneOrder(): Flow<List<Invoice>> =
        orderRepository.getAllDoneOrder()

    override fun getOrderMenu(invoiceId: String): Flow<List<Order>> =
        orderRepository.getOrderMenu(invoiceId)

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