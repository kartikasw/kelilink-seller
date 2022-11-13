package com.example.kelilinkseller.core.domain.use_case.order

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderUseCase {
    fun getAllNewOrder(): Flow<Resource<List<Invoice>>>
    fun getAllReadyOrder(): Flow<Resource<List<Invoice>>>
    fun getAllDoneOrder(): Flow<Resource<List<Invoice>>>

    fun getOrderMenu(): Flow<Resource<List<Order>>>
}