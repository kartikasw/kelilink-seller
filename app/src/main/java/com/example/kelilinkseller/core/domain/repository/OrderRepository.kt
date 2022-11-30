package com.example.kelilinkseller.core.domain.repository

import com.example.kelilinkseller.core.data.source.remote.service.FirestoreQueryLiveData
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllLiveOrder(): FirestoreQueryLiveData<Invoice>
    fun getLiveOrderMenu(invoiceId: String): FirestoreQueryLiveData<Order>

    fun getOrderById(orderId: String): Flow<Resource<Invoice>>

    fun acceptOrder(invoiceId: String): Flow<Resource<Unit>>
    fun declineOrder(invoiceId: String): Flow<Resource<Unit>>
    fun markOrderAsReady(invoiceId: String): Flow<Resource<Unit>>
    fun markOrderAsDone(invoiceId: String): Flow<Resource<Unit>>

    fun sendFcm(data: Fcm): Flow<Resource<Unit>>

    fun setInvoiceId(id: String)
    fun getInvoiceId(): String
}