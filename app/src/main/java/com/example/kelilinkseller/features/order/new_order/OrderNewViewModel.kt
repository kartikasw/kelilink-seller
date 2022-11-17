package com.example.kelilinkseller.features.order.new_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.INVOICE_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.ORDERS_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.STORE_ID_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.TIME_COLUMN
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import com.example.kelilinkseller.features.order.FirestoreQueryLiveData
import com.example.kelilinkseller.features.order.FirestoreQueryLiveDataNested
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderNewViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    fun setInvoiceId(id: String) {
        orderUseCase.setInvoiceId(id)
    }

    fun getAllNewOrderLiveData(): FirestoreQueryLiveData {
        val storeId = Firebase.auth.currentUser!!.uid
        return FirestoreQueryLiveData(
            Firebase.firestore.collection(INVOICE_COLLECTION)
                .whereEqualTo(STORE_ID_COLUMN, storeId)
                .orderBy(TIME_COLUMN)
        )
    }

    fun getAllOrderMenuLiveData(invoiceId: String): FirestoreQueryLiveDataNested {
        return FirestoreQueryLiveDataNested(
            Firebase.firestore.collection(INVOICE_COLLECTION)
                .document(invoiceId)
                .collection(ORDERS_COLLECTION)
        )
    }
//
//    fun getAllNewOrder() =
//        orderUseCase.getAllNewOrder().asLiveData()

    fun updateOrderStatus(invoiceId: String, status: String) =
        orderUseCase.updateOrderStatus(invoiceId, status).asLiveData()

    fun sendFcm(data: Fcm) =
        orderUseCase.sendFcm(data).asLiveData()
}