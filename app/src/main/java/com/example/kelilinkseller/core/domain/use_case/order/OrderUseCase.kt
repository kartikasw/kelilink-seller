package com.example.kelilinkseller.core.domain.use_case.order

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.model.Invoice
import kotlinx.coroutines.flow.Flow

interface OrderUseCase {
    fun getAllNewOrder(): Flow<Resource<List<Invoice>>>
    fun getAllReadyOrder(): Flow<Resource<List<Invoice>>>
    fun getAllDoneOrder(): Flow<Resource<List<Invoice>>>

    fun updateOrderStatus(invoiceId: String, status: String): Flow<Resource<Unit>>
    fun sendFcm(data: Fcm): Flow<Resource<Unit>>
}