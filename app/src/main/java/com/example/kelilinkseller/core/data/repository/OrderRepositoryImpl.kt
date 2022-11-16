package com.example.kelilinkseller.core.data.repository

import android.util.Log
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.COOKING
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.DONE
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.READY
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.WAITING
import com.example.kelilinkseller.core.data.helper.Response
import com.example.kelilinkseller.core.data.mapper.toListModel
import com.example.kelilinkseller.core.data.source.remote.RemoteDataSource
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
): OrderRepository {

    companion object {
        const val TAG = "OrderRepository"
    }

    override fun getAllNewOrder(): Flow<Resource<List<Invoice>>> =
        flow {
            emit(Resource.Loading())
            when(val invoiceResponse = remote.getAllOrder().first()) {
                is Response.Success -> {
                    val responseModel = invoiceResponse.data.toListModel()
                     for(i in responseModel) {
                         when(val orderResponse = remote.getOrderMenu(i.id).first()) {
                             is Response.Success -> {
                                 Log.d(TAG, orderResponse.data.toString())
                                 responseModel[responseModel.indexOf(i)].orders =
                                     orderResponse.data.toListModel()
                             }
                             else -> {}
                         }
                     }
                    Log.d(TAG, responseModel.toString())
                    emit(Resource.Success(
                        responseModel.filter {
                            it.status == COOKING || it.status == WAITING
                        })
                    )
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(invoiceResponse.errorMessage))
                }
            }
        }

    override fun getAllReadyOrder(): Flow<Resource<List<Invoice>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getAllOrder().first()) {
                is Response.Success -> {
                    Log.d(StoreRepositoryImpl.TAG, response.data.toString())
                    emit(Resource.Success(response.data.toListModel().filter {
                        it.status == READY
                    }))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun getAllDoneOrder(): Flow<Resource<List<Invoice>>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.getAllOrder().first()) {
                is Response.Success -> {
                    Log.d(StoreRepositoryImpl.TAG, response.data.toString())
                    emit(Resource.Success(response.data.toListModel().filter {
                        it.status == DONE
                    }))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun updateOrderStatus(invoiceId: String, status: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.updateOrderStatus(invoiceId, status).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }

    override fun sendFcm(data: Fcm): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.sendFcm(data).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }
}