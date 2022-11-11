package com.example.kelilinkseller.features.order.new_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderNewViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    fun getAllNewOrder() =
        orderUseCase.getAllNewOrder().asLiveData()

}