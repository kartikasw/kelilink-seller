package com.example.kelilinkseller.core.data.source.remote

import android.net.Uri
import com.example.kelilinkseller.core.data.source.remote.service.AuthService
import com.example.kelilinkseller.core.data.source.remote.service.OrderService
import com.example.kelilinkseller.core.data.source.remote.service.StoreService
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.model.Store
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val auth: AuthService,
    private val order: OrderService,
    private val store: StoreService
) {
    fun register(email: String, password: String, seller: Seller, store: Store, uri: Uri) =
        auth.register(email, password, seller, store, uri)

    fun logIn(email: String, password: String, fcmToken: String) =
        auth.logIn(email, password, fcmToken)

    fun getAllOrder() =
        order.getAllOrder()

    fun getOrderMenu(invoiceId: String) =
        order.getOrderMenu(invoiceId)

    fun getMyStore() =
        store.getMyStore()

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