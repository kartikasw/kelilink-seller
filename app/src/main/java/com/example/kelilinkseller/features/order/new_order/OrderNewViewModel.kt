package com.example.kelilinkseller.features.order.new_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderNewViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    fun getAllNewOrder() =
        orderUseCase.getAllNewOrder().asLiveData()

    fun updateOrderStatus(invoiceId: String, status: String) =
        orderUseCase.updateOrderStatus(invoiceId, status).asLiveData()

    fun sendFcm(data: Fcm) =
        orderUseCase.sendFcm(data).asLiveData()
}