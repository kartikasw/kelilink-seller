package com.example.kelilinkseller.core.domain.repository

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllNewOrder(): Flow<Resource<List<Invoice>>>
    fun getAllReadyOrder(): Flow<Resource<List<Invoice>>>
    fun getAllDoneOrder(): Flow<Resource<List<Invoice>>>

    fun getOrderById(orderId: String): Flow<Resource<Invoice>>

    fun updateOrderStatus(invoiceId: String, status: String): Flow<Resource<Unit>>
    fun sendFcm(data: Fcm): Flow<Resource<Unit>>

    fun setInvoiceId(id: String)
    fun getInvoiceId(): String
}