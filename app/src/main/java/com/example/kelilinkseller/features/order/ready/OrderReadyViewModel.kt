package com.example.kelilinkseller.features.order.ready

import androidx.lifecycle.*
import com.example.kelilinkseller.core.data.helper.Constants
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import com.example.kelilinkseller.features.order.FirestoreQueryLiveData
import com.example.kelilinkseller.features.order.FirestoreQueryLiveDataNested
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderReadyViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    private var _orders = MutableLiveData<List<Invoice>>()
    val orders: LiveData<List<Invoice>> get() = _orders

    init {
        viewModelScope.launch(Dispatchers.Main){
            orderUseCase.getAllNewOrder()
                .collect { items ->
                    for(item in items) {
                        getOrderMenu(item.id).collect {
                            item.orders = item.orders
                        }
                    }
                    _orders.value = items
                }
        }
    }

    fun getAllReadyOrderLiveData(): FirestoreQueryLiveData {
        val storeId = Firebase.auth.currentUser!!.uid
        return FirestoreQueryLiveData(
            Firebase.firestore.collection(Constants.DatabaseCollection.INVOICE_COLLECTION)
                .whereEqualTo(Constants.DatabaseColumn.STORE_ID_COLUMN, storeId)
                .orderBy(Constants.DatabaseColumn.TIME_COLUMN)
        )
    }

    fun getAllOrderMenuLiveData(invoiceId: String): FirestoreQueryLiveDataNested {
        return FirestoreQueryLiveDataNested(
            Firebase.firestore.collection(Constants.DatabaseCollection.INVOICE_COLLECTION)
                .document(invoiceId)
                .collection(Constants.DatabaseCollection.ORDERS_COLLECTION)
        )
    }

    fun setInvoiceId(id: String) {
        orderUseCase.setInvoiceId(id)
    }

    private fun getOrderMenu(invoiceId: String) =
        orderUseCase.getOrderMenu(invoiceId)

    fun markOrderAsDone(invoiceId: String) =
        orderUseCase.markOrderAsDone(invoiceId).asLiveData()

}