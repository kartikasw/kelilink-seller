package com.example.kelilinkseller.core.domain.repository

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllNewOrder(): Flow<Resource<List<Invoice>>>
    fun getAllReadyOrder(): Flow<Resource<List<Invoice>>>
    fun getAllDoneOrder(): Flow<Resource<List<Invoice>>>

    fun getOrderMenu(): Flow<Resource<List<Order>>>
}