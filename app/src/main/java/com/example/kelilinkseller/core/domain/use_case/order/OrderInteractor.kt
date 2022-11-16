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

    override fun getAllNewOrder(): Flow<Resource<List<Invoice>>> =
        orderRepository.getAllNewOrder()

    override fun getAllReadyOrder(): Flow<Resource<List<Invoice>>> =
        orderRepository.getAllReadyOrder()

    override fun getAllDoneOrder(): Flow<Resource<List<Invoice>>> =
        orderRepository.getAllDoneOrder()

    override fun updateOrderStatus(invoiceId: String, status: String): Flow<Resource<Unit>> =
        orderRepository.updateOrderStatus(invoiceId, status)

    override fun sendFcm(data: Fcm): Flow<Resource<Unit>> =
        orderRepository.sendFcm(data)

}