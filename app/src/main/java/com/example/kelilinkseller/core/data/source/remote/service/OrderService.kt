package com.example.kelilinkseller.core.data.source.remote.service

import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.INVOICE_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.ORDERS_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.STORE_ID_COLUMN
import com.example.kelilinkseller.core.data.helper.Response
import com.example.kelilinkseller.core.data.source.remote.response.InvoiceResponse
import com.example.kelilinkseller.core.data.source.remote.response.OrderResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderService @Inject constructor(): FirebaseService() {
    fun getAllOrder(): Flow<Response<List<InvoiceResponse>>> =
        getDocumentByField(INVOICE_COLLECTION, STORE_ID_COLUMN, user!!.uid)

    fun getOrderMenu(invoiceId: String): Flow<Response<List<OrderResponse>>> =
        getDocumentInSubCollection(INVOICE_COLLECTION, invoiceId, ORDERS_COLLECTION)
}