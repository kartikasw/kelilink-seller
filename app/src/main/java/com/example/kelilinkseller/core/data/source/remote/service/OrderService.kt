package com.example.kelilinkseller.core.data.source.remote.service

import android.util.Log
import com.example.kelilinkseller.core.data.helper.Constants
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.INVOICE_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.ORDERS_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.STORE_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.QUEUE_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.QUEUE_ORDER_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.STATUS_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.STORE_ID_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.TIME_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.COOKING
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.DECLINED
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.DONE
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.READY
import com.example.kelilinkseller.core.data.helper.Response
import com.example.kelilinkseller.core.data.source.remote.response.InvoiceResponse
import com.example.kelilinkseller.core.data.source.remote.response.OrderResponse
import com.example.kelilinkseller.core.data.source.remote.response.StoreResponse
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.model.Order
import com.example.kelilinkseller.features.order.FirestoreQueryLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderService @Inject constructor(): FirebaseService() {

//    fun getAllOrder(): Flow<List<InvoiceResponse?>> {
//        val storeId = getUser()!!.uid
//
//        return firestore.collection(INVOICE_COLLECTION)
//            .whereEqualTo(STORE_ID_COLUMN, storeId)
//            .orderBy(TIME_COLUMN)
//            .snapshotFlow()
//            .map { querySnapshot ->
//                querySnapshot.documents.map { it.toObject(InvoiceResponse::class.java) }
//            }
//
//    }

    fun getAllLiveOrder(): FirestoreQueryLiveData<Invoice> {
        val storeId = getUser()!!.uid
        return FirestoreQueryLiveData(
            Firebase.firestore.collection(INVOICE_COLLECTION)
                .whereEqualTo(STORE_ID_COLUMN, storeId)
                .orderBy(TIME_COLUMN),
            Invoice()
        )
    }

    fun getLiveOrderMenu(invoiceId: String): FirestoreQueryLiveData<Order> =
        FirestoreQueryLiveData(
            Firebase.firestore.collection(INVOICE_COLLECTION)
                .document(invoiceId)
                .collection(Constants.DatabaseCollection.ORDERS_COLLECTION),
            Order()
        )


    fun getOrderMenu(invoiceId: String): Flow<Response<List<OrderResponse>>> =
        getDocumentInSubCollection(INVOICE_COLLECTION, invoiceId, ORDERS_COLLECTION)

    fun acceptOrder(invoiceId: String): Flow<Response<Unit>> =
        flow {
            val storeId = getUser()!!.uid
            getDocumentById<StoreResponse>(STORE_COLLECTION, storeId).collect { store ->
                when(store) {
                    is Response.Success -> {
                        updateDocument<InvoiceResponse>(
                            INVOICE_COLLECTION,
                            invoiceId,
                            mutableMapOf(
                                STATUS_COLUMN to COOKING,
                                QUEUE_ORDER_COLUMN to store.data.queue.size + 1
                            )
                        ).collect { invoice ->
                            when(invoice) {
                                is Response.Success -> {
                                    emitAll(addValueToArrayOfDocument(
                                        STORE_COLLECTION,
                                        store.data.id,
                                        QUEUE_COLUMN,
                                        invoiceId
                                    ))
                                }
                                is Response.Error -> {
                                    emit(Response.Error(invoice.errorMessage))
                                }
                                else -> {}
                            }
                        }
                    }
                    is Response.Error -> {
                        Log.d(TAG, store.errorMessage)
                        emit(Response.Error(store.errorMessage))
                    }
                    else -> {}
                }
            }
        }

    fun declineOrder(invoiceId: String): Flow<Response<StoreResponse>> =
        flow {
            val storeId = getUser()!!.uid
            getDocumentById<StoreResponse>(STORE_COLLECTION, storeId).collect {
                when(it) {
                    is Response.Success -> {
                        emitAll(updateDocument(
                            INVOICE_COLLECTION,
                            invoiceId,
                            STATUS_COLUMN,
                            DECLINED
                        ))
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    else -> {}
                }
            }
        }

    fun markOrderAsReady(invoiceId: String): Flow<Response<InvoiceResponse>> =
        flow {
            val storeId = getUser()!!.uid
            removeValueFromArrayOfDocument(
                STORE_COLLECTION, storeId, QUEUE_COLUMN, invoiceId
            ).collect {
                when (it) {
                    is Response.Success -> {
                        emitAll(
                            updateDocument<InvoiceResponse>(
                                INVOICE_COLLECTION,
                                invoiceId,
                                mutableMapOf(
                                    STATUS_COLUMN to READY,
                                    QUEUE_ORDER_COLUMN to 0
                                )
                            )
                        )
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    else -> {}
                }
            }
        }

    fun markOrderAsDone(invoiceId: String): Flow<Response<StoreResponse>> =
        flow {
            val storeId = getUser()!!.uid
            getDocumentById<StoreResponse>(STORE_COLLECTION, storeId).collect {
                when(it) {
                    is Response.Success -> {
                        emitAll(updateDocument(
                            INVOICE_COLLECTION,
                            invoiceId,
                            STATUS_COLUMN,
                            DONE
                        ))
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    else -> {}
                }
            }
        }

    fun getOrderById(orderId: String): Flow<Response<InvoiceResponse>> =
        getDocumentById(INVOICE_COLLECTION, orderId)
}