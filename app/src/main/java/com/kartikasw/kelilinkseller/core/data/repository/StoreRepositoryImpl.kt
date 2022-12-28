package com.kartikasw.kelilinkseller.core.data.repository

import android.net.Uri
import android.util.Log
import com.kartikasw.kelilinkseller.core.data.helper.Response
import com.kartikasw.kelilinkseller.core.data.mapper.toListModel
import com.kartikasw.kelilinkseller.core.data.mapper.toModel
import com.kartikasw.kelilinkseller.core.data.source.remote.RemoteDataSource
import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.core.domain.model.Menu
import com.kartikasw.kelilinkseller.core.domain.model.Store
import com.kartikasw.kelilinkseller.core.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
): StoreRepository {

    companion object {
        const val TAG = "StoreRepositoryImpl"
    }

    override fun getStoreById(storeId: String): Flow<Resource<Store>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getStoreById(storeId).first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toModel()))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun updateMyStore(store: MutableMap<String, Any>, uri: Uri): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.updateMyStore(store, uri).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun updateMyStore(store: MutableMap<String, Any>): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.updateMyStore(store).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun openStore(store: MutableMap<String, Any>): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.openStore(store).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun closeStore(): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.closeStore().first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun getMyMenu(): Flow<Resource<List<Menu>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getMyMenu().first()) {
                is Response.Success -> {
                    Log.d(TAG, response.data.toString())
                    emit(Resource.Success(response.data.toListModel()))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun getMenuById(menuId: String): Flow<Resource<Menu>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getMenuById(menuId).first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toModel()))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun addMenu(menu: Menu, uri: Uri): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.addMenu(menu, uri).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun updateMenu(menuId: String, menu: MutableMap<String, Any>, uri: Uri): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.updateMenu(menuId, menu, uri).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun updateMenu(menuId: String, menu: MutableMap<String, Any>): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.updateMenu(menuId, menu).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun updateMenuStock(menuId: String, stock: Boolean): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.updateMenuStock(menuId, stock).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun deleteMenu(menuId: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.deleteMenu(menuId).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun logout() = remote.logout()

}