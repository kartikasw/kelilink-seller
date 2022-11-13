package com.example.kelilinkseller.core.data.source.remote.service

import android.net.Uri
import android.util.Log
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.SELLER_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.STORE_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.FCM_TOKEN_COLUMN
import com.example.kelilinkseller.core.data.helper.Response
import com.example.kelilinkseller.core.data.source.remote.response.SellerResponse
import com.example.kelilinkseller.core.data.source.remote.response.StoreResponse
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.model.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthService @Inject constructor(): FirebaseService() {
    fun register(
        email: String, password: String, seller: Seller, store: Store, uri: Uri
    ): Flow<Response<StoreResponse>> =
        flow {
            createUserWithEmailAndPassword(email, password).collect {
                when(it) {
                    is Response.Success -> {
                        setDocument<SellerResponse>(
                            SELLER_COLLECTION,
                            it.data,
                            seller.copy(uid = it.data)
                        ).collect { seller ->
                            when(seller) {
                                is Response.Success -> {
                                    uploadPicture(STORE_COLLECTION, seller.data.uid, uri).collect { pictureUrl ->
                                        when(pictureUrl) {
                                            is Response.Success -> {
                                                emitAll(
                                                    setDocument<StoreResponse>(
                                                        STORE_COLLECTION,
                                                        seller.data.uid,
                                                        store.copy(
                                                            id = seller.data.uid,
                                                            image = pictureUrl.data
                                                        )
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
                                    emit(Response.Error(seller.errorMessage))
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

    fun logIn(email: String, password: String, fcmToken: String): Flow<Response<SellerResponse>> =
        flow {
            signInWithEmailAndPassword(email, password).collect {
                when(it) {
                    is Response.Success -> {
                        emitAll(
                            updateFieldInDocument(
                                STORE_COLLECTION,
                                it.data,
                                FCM_TOKEN_COLUMN,
                                fcmToken
                            )
                        )
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
}