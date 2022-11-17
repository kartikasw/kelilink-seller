package com.example.kelilinkseller.core.data.source.remote.service

import android.util.Log
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.INVOICE_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.ORDERS_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.STORE_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.QUEUE_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.QUEUE_ORDER_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.STATUS_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.STORE_ID_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.COOKING
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.READY
import com.example.kelilinkseller.core.data.helper.Response
import com.example.kelilinkseller.core.data.source.remote.response.InvoiceResponse
import com.example.kelilinkseller.core.data.source.remote.response.OrderResponse
import com.example.kelilinkseller.core.data.source.remote.response.StoreResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderService @Inject constructor(): FirebaseService() {
    fun getAllOrder(): Flow<Response<List<InvoiceResponse>>> =
        getDocumentByFieldAndOrderByTime(INVOICE_COLLECTION, STORE_ID_COLUMN, user!!.uid)

    fun getOrderMenu(invoiceId: String): Flow<Response<List<OrderResponse>>> =
        getDocumentInSubCollection(INVOICE_COLLECTION, invoiceId, ORDERS_COLLECTION)

    fun updateOrderStatus(invoiceId: String, status: String): Flow<Response<Unit>> =
        flow {
            val storeId = user!!.uid
            getDocumentById<StoreResponse>(STORE_COLLECTION, storeId).collect { store ->
                when(store) {
                    is Response.Success -> {
                        when (status) {
                            COOKING -> {
                                updateDocument<InvoiceResponse>(
                                    INVOICE_COLLECTION,
                                    invoiceId,
                                    mutableMapOf(
                                        STATUS_COLUMN to status,
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
                                        is Response.Empty -> {

                                        }
                                        is Response.Error -> {
                                            emit(Response.Error(invoice.errorMessage))
                                        }
                                    }
                                }
                            }
                            READY -> {
                                updateDocument<InvoiceResponse>(
                                    INVOICE_COLLECTION,
                                    invoiceId,
                                    mutableMapOf(
                                        STATUS_COLUMN to status,
                                        QUEUE_ORDER_COLUMN to 0
                                    )
                                ).collect { invoice ->
                                    when(invoice) {
                                        is Response.Success -> {
                                            emitAll(removeValueFromArrayOfDocument(
                                                STORE_COLLECTION,
                                                store.data.id,
                                                QUEUE_COLUMN,
                                                invoiceId
                                            ))
                                        }
                                        is Response.Empty -> {

                                        }
                                        is Response.Error -> {
                                            emit(Response.Error(invoice.errorMessage))
                                        }
                                    }
                                }
                            }
                            else -> {
                                emitAll(updateDocumentUnit(
                                    INVOICE_COLLECTION,
                                    invoiceId,
                                    mutableMapOf(
                                        STATUS_COLUMN to status,
                                        QUEUE_ORDER_COLUMN to 0
                                    )
                                ))
                            }
                        }
                    }
                    is Response.Empty -> {

                    }
                    is Response.Error -> {
                        Log.d(TAG, store.errorMessage)
                        emit(Response.Error(store.errorMessage))
                    }
                }
            }
        }


    fun getOrderById(orderId: String): Flow<Response<InvoiceResponse>> =
        getDocumentById(INVOICE_COLLECTION, orderId)
}