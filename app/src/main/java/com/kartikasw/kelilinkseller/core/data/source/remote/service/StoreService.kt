package com.kartikasw.kelilinkseller.core.data.source.remote.service

import android.net.Uri
import com.kartikasw.kelilinkseller.core.data.helper.Constants.DatabaseCollection.MENU_COLLECTION
import com.kartikasw.kelilinkseller.core.data.helper.Constants.DatabaseCollection.STORE_COLLECTION
import com.kartikasw.kelilinkseller.core.data.helper.Constants.DatabaseColumn.AVAILABLE_COLUMN
import com.kartikasw.kelilinkseller.core.data.helper.Constants.DatabaseColumn.IMAGE_COLUMN
import com.kartikasw.kelilinkseller.core.data.helper.Constants.DatabaseColumn.OPERATING_STATUS_COLUMN
import com.kartikasw.kelilinkseller.core.data.helper.Constants.DatabaseColumn.STORE_ID_COLUMN
import com.kartikasw.kelilinkseller.core.data.helper.Response
import com.kartikasw.kelilinkseller.core.data.source.remote.response.MenuResponse
import com.kartikasw.kelilinkseller.core.data.source.remote.response.StoreResponse
import com.kartikasw.kelilinkseller.core.domain.model.Menu
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreService @Inject constructor(): FirebaseService() {

    fun getStoreById(storeId: String): Flow<Response<StoreResponse>> =
        getDocumentById(STORE_COLLECTION, storeId)

    fun updateMyStore(store: MutableMap<String, Any>, uri: Uri): Flow<Response<StoreResponse>> =
        flow {
            val storeId = getUser()!!.uid
            deleteFile(STORE_COLLECTION, storeId).collect {
                when(it) {
                    is Response.Success -> {
                        uploadPicture(STORE_COLLECTION, storeId, uri).collect { pictureUrl ->
                            store[IMAGE_COLUMN] = pictureUrl
                            when (pictureUrl) {
                                is Response.Success -> {
                                    store[IMAGE_COLUMN] = pictureUrl.data
                                    emitAll(
                                        updateDocument<StoreResponse>(
                                            STORE_COLLECTION,
                                            storeId,
                                            store
                                        )
                                    )
                                }
                                is Response.Error -> {
                                    emit(Response.Error(pictureUrl.errorMessage))
                                }
                                is Response.Empty -> {
                                    emit(Response.Empty)
                                }
                            }
                        }
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    is Response.Empty -> {
                        emit(Response.Empty)
                    }
                }
            }
        }

    fun updateMyStore(store: MutableMap<String, Any>): Flow<Response<StoreResponse>> =
        flow {
            val storeId = getUser()!!.uid
            emitAll(updateDocument(STORE_COLLECTION, storeId, store))
        }

    fun openStore(store: MutableMap<String, Any>): Flow<Response<StoreResponse>> =
        flow {
            val storeId = getUser()!!.uid
            emitAll(updateDocument(STORE_COLLECTION, storeId, store))
        }

    fun closeStore(): Flow<Response<StoreResponse>> =
        flow {
            val storeId = getUser()!!.uid
            emitAll(
                updateDocument(STORE_COLLECTION, storeId, OPERATING_STATUS_COLUMN, false)
            )
        }

    fun getMyMenu(): Flow<Response<List<MenuResponse>>> =
        flow {
            val storeId = getUser()!!.uid
            emitAll(getDocumentByField(MENU_COLLECTION, STORE_ID_COLUMN, storeId))
        }

    fun getMenuById(menuId: String): Flow<Response<MenuResponse>> =
        getDocumentById(MENU_COLLECTION, menuId)

    fun addMenu(menu: Menu, uri: Uri): Flow<Response<MenuResponse>> =
        flow {
            val storeId = getUser()!!.uid
            addDocument<MenuResponse>(
                MENU_COLLECTION,
                menu.copy(store_id = storeId) as Any
            ).collect { menu ->
                when(menu) {
                    is Response.Success -> {
                        uploadPicture(MENU_COLLECTION, menu.data.id, uri).collect { pictureUrl ->
                            when (pictureUrl) {
                                is Response.Success -> {
                                    emitAll(
                                        updateDocument<MenuResponse>(
                                            MENU_COLLECTION,
                                            menu.data.id,
                                            IMAGE_COLUMN,
                                            pictureUrl.data
                                        )
                                    )
                                }
                                is Response.Error -> {
                                    emit(Response.Error(pictureUrl.errorMessage))
                                }
                                is Response.Empty -> {
                                    emit(Response.Empty)
                                }
                            }
                        }
                    }
                    is Response.Error -> {
                        emit(Response.Error(menu.errorMessage))
                    }
                    is Response.Empty -> {
                        emit(Response.Empty)
                    }
                }
            }
        }

    fun updateMenu(menuId: String, menu: MutableMap<String, Any>, uri: Uri): Flow<Response<MenuResponse>> =
        flow {
            deleteFile(MENU_COLLECTION, menuId).collect {
                when(it) {
                    is Response.Success -> {
                        uploadPicture(MENU_COLLECTION, menuId, uri).collect { pictureUrl ->
                            when (pictureUrl) {
                                is Response.Success -> {
                                    menu[IMAGE_COLUMN] = pictureUrl.data
                                    emitAll(
                                        updateDocument<MenuResponse>(
                                            MENU_COLLECTION,
                                            menuId,
                                            menu
                                        )
                                    )
                                }
                                is Response.Error -> {
                                    emit(Response.Error(pictureUrl.errorMessage))
                                }
                                is Response.Empty -> {
                                    emit(Response.Empty)
                                }
                            }
                        }
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    is Response.Empty -> {
                        emit(Response.Empty)
                    }
                }
            }
        }

    fun updateMenu(menuId: String, menu: MutableMap<String, Any>): Flow<Response<MenuResponse>> =
        updateDocument<MenuResponse>(MENU_COLLECTION, menuId, menu)

    fun updateMenuStock(menuId: String, stock: Boolean): Flow<Response<MenuResponse>> =
        updateDocument<MenuResponse>(MENU_COLLECTION, menuId, AVAILABLE_COLUMN, stock)

    fun deleteMenu(menuId: String): Flow<Response<Unit>> =
        flow {
            deleteDocument(MENU_COLLECTION, menuId).collect {
                when(it) {
                    is Response.Success -> {
                        emitAll(deleteFile(MENU_COLLECTION, menuId))
                    }
                    is Response.Error -> {
                        emit(Response.Error(it.errorMessage))
                    }
                    is Response.Empty -> {
                        emit(Response.Empty)
                    }
                }
            }
        }

    fun logout() : Unit =
        signOut()

}