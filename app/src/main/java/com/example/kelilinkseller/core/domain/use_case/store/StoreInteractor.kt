package com.example.kelilinkseller.core.domain.use_case.store

import android.net.Uri
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.core.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class StoreInteractor @Inject constructor(
    private val storeRepository: StoreRepository
): StoreUseCase {

    override fun getMyStore(): Flow<Resource<Store>> =
        storeRepository.getMyStore()

    override fun updateMyStore(store: MutableMap<String, Any>, uri: Uri): Flow<Resource<Unit>> =
        storeRepository.updateMyStore(store, uri)

    override fun updateMyStore(store: MutableMap<String, Any>): Flow<Resource<Unit>> =
        storeRepository.updateMyStore(store)

    override fun openStore(store: MutableMap<String, Any>): Flow<Resource<Unit>> =
        storeRepository.openStore(store)

    override fun closeStore(): Flow<Resource<Unit>> =
        storeRepository.closeStore()

    override fun getMyMenu(): Flow<Resource<List<Menu>>> =
        storeRepository.getMyMenu()

    override fun getMenuById(menuId: String): Flow<Resource<Menu>> =
        storeRepository.getMenuById(menuId)

    override fun addMenu(menu: Menu, uri: Uri): Flow<Resource<Unit>> =
        storeRepository.addMenu(menu, uri)

    override fun updateMenu(menuId: String, menu: MutableMap<String, Any>, uri: Uri): Flow<Resource<Unit>> =
        storeRepository.updateMenu(menuId, menu, uri)

    override fun updateMenu(menuId: String, menu: MutableMap<String, Any>): Flow<Resource<Unit>> =
        storeRepository.updateMenu(menuId, menu)

    override fun updateMenuStock(menuId: String, stock: Boolean): Flow<Resource<Unit>> =
        storeRepository.updateMenuStock(menuId, stock)

    override fun deleteMenu(menuId: String): Flow<Resource<Unit>> =
        storeRepository.deleteMenu(menuId)

    override fun logout() =
        storeRepository.logout()

}