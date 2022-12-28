package com.kartikasw.kelilinkseller.core.domain.use_case.store

import android.net.Uri
import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.core.domain.model.Menu
import com.kartikasw.kelilinkseller.core.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface StoreUseCase {
    fun getStoreById(storeId: String): Flow<Resource<Store>>
    fun updateMyStore(store: MutableMap<String, Any>, uri: Uri): Flow<Resource<Unit>>
    fun updateMyStore(store: MutableMap<String, Any>): Flow<Resource<Unit>>

    fun openStore(store: MutableMap<String, Any>): Flow<Resource<Unit>>
    fun closeStore(): Flow<Resource<Unit>>

    fun getMyMenu(): Flow<Resource<List<Menu>>>
    fun getMenuById(menuId: String): Flow<Resource<Menu>>
    fun addMenu(menu: Menu, uri: Uri): Flow<Resource<Unit>>
    fun updateMenu(menuId: String, menu: MutableMap<String, Any>, uri: Uri): Flow<Resource<Unit>>
    fun updateMenu(menuId: String, menu: MutableMap<String, Any>): Flow<Resource<Unit>>
    fun updateMenuStock(menuId: String, stock: Boolean): Flow<Resource<Unit>>
    fun deleteMenu(menuId: String): Flow<Resource<Unit>>

    fun logout()
}