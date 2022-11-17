package com.example.kelilinkseller.features.order.ready

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.data.helper.Constants
import com.example.kelilinkseller.core.domain.use_case.order.OrderUseCase
import com.example.kelilinkseller.features.order.FirestoreQueryLiveData
import com.example.kelilinkseller.features.order.FirestoreQueryLiveDataNested
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderReadyViewModel @Inject constructor(
    private val orderUseCase: OrderUseCase
) : ViewModel() {

    fun setInvoiceId(id: String) {
        orderUseCase.setInvoiceId(id)
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

    fun getAllReadyOrder() =
        orderUseCase.getAllReadyOrder().asLiveData()

    fun updateOrderStatus(invoiceId: String, status: String) =
        orderUseCase.updateOrderStatus(invoiceId, status).asLiveData()

}