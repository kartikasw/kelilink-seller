package com.kartikasw.kelilinkseller.core.data.source.remote

import android.net.Uri
import com.kartikasw.kelilinkseller.core.data.helper.Response
import com.kartikasw.kelilinkseller.core.data.source.remote.response.FcmResponse
import com.kartikasw.kelilinkseller.core.data.source.remote.service.AuthService
import com.kartikasw.kelilinkseller.core.data.source.remote.service.OrderService
import com.kartikasw.kelilinkseller.core.data.source.remote.service.SellerService
import com.kartikasw.kelilinkseller.core.data.source.remote.service.StoreService
import com.kartikasw.kelilinkseller.core.data.source.remote.service.notification.ApiService
import com.kartikasw.kelilinkseller.core.domain.model.Fcm
import com.kartikasw.kelilinkseller.core.domain.model.Menu
import com.kartikasw.kelilinkseller.core.domain.model.Seller
import com.kartikasw.kelilinkseller.core.domain.model.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val api: ApiService,
    private val auth: AuthService,
    private val order: OrderService,
    private val seller: SellerService,
    private val store: StoreService
) {
    fun register(email: String, password: String, seller: Seller, store: Store, uri: Uri) =
        auth.register(email, password, seller, store, uri)

    fun logIn(email: String, password: String, fcmToken: String) =
        auth.logIn(email, password, fcmToken)

    fun resetPassword(email: String) =
        auth.resetPassword(email)

    fun getAllLiveOrder() =
        order.getAllLiveOrder()

    fun getLiveOrderMenu(invoiceId: String) =
        order.getLiveOrderMenu(invoiceId)

    fun getOrderMenu(invoiceId: String) =
        order.getOrderMenu(invoiceId)

    fun acceptOrder(invoiceId: String) =
        order.acceptOrder(invoiceId)

    fun declineOrder(invoiceId: String) =
        order.declineOrder(invoiceId)

    fun markOrderAsReady(invoiceId: String) =
        order.markOrderAsReady(invoiceId)

    fun markOrderAsDone(invoiceId: String) =
        order.markOrderAsDone(invoiceId)

    fun getOrderById(orderId: String) =
        order.getOrderById(orderId)

    suspend fun sendFcm(data: Fcm): Flow<Response<FcmResponse>> =
        flow<Response<FcmResponse>> {
            val response = api.sendFcm(data)
            if(response.success == 1) {
                emit(Response.Success(response))
            } else {
                emit(Response.Error(response.results[0].error))
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getUser() = seller.getUser()

    fun updatePassword(oldPassword: String, newPassword: String) =
        seller.updatePassword(oldPassword, newPassword)

    fun getStoreById(storeId: String) =
        store.getStoreById(storeId)

    fun updateMyStore(data: MutableMap<String, Any>, uri: Uri) =
        store.updateMyStore(data, uri)

    fun updateMyStore(data: MutableMap<String, Any>) =
        store.updateMyStore(data)

    fun openStore(data: MutableMap<String, Any>) =
        store.openStore(data)

    fun closeStore() =
        store.closeStore()

    fun getMyMenu() =
        store.getMyMenu()

    fun getMenuById(menuId: String) =
        store.getMenuById(menuId)

    fun addMenu(menu: Menu, uri: Uri) =
        store.addMenu(menu, uri)

    fun updateMenu(menuId: String, menu: MutableMap<String, Any>, uri: Uri) =
        store.updateMenu(menuId, menu, uri)

    fun updateMenu(menuId: String, menu: MutableMap<String, Any>) =
        store.updateMenu(menuId, menu)

    fun updateMenuStock(menuId: String, stock: Boolean) =
        store.updateMenuStock(menuId, stock)

    fun deleteMenu(menuId: String) =
        store.deleteMenu(menuId)

    fun logout() : Unit =
        store.logout()

}