package com.example.kelilinkseller.core.data.source.remote.service

import android.net.Uri
import android.util.Log
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseCollection.SELLER_COLLECTION
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.ID_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.TIME_COLUMN
import com.example.kelilinkseller.core.data.helper.Response
import com.example.kelilinkseller.core.data.source.remote.response.SellerResponse
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

abstract class FirebaseService {

    companion object {
        const val TAG = "FirebaseService"
    }

    private val auth = FirebaseAuth.getInstance()

    val user = auth.currentUser

    val firestore = Firebase.firestore

    private val storage = Firebase.storage

    private val storageRef = storage.reference

    fun signOut(): Unit = auth.signOut()

    fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<String>> =
        flow {
            val createUser = auth.createUserWithEmailAndPassword(email, password).await()
            val user = createUser.user
            if (user != null) {
                emit(Response.Success(user.uid))
            } else {
                emit(Response.Empty)
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<String>> =
        flow {
            val createUser = auth.signInWithEmailAndPassword(email, password).await()
            val user = createUser.user
            if (user != null) {
                emit(Response.Success(user.uid))
            } else {
                emit(Response.Empty)
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun reAuthenticate(
        oldPassword: String,
        newPassword: String
    ): Flow<Response<SellerResponse>> =
        flow {
            val email = user!!.email.toString()
            val credential = EmailAuthProvider
                .getCredential(email, oldPassword)

            var isComplete = false

            user.reauthenticate(credential)
                .addOnCompleteListener {
                    isComplete = true
                }.await()

            if(isComplete) {
                emitAll(updatePassword(newPassword))
            } else {
                emit(Response.Empty)
            }

        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    private fun updatePassword(
        newPassword: String
    ): Flow<Response<SellerResponse>> =
        flow {
            val userId = user!!.uid
            user.updatePassword(newPassword).await()

            emitAll(getDocumentById<SellerResponse>(SELLER_COLLECTION, userId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> setDocument(
        collection: String,
        docId: String,
        document: Any
    ): Flow<Response<ResponseType>> =
        flow {
            firestore
                .collection(collection)
                .document(docId)
                .set(document)
                .await()

            emitAll(getDocumentById<ResponseType>(collection, docId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> addDocument(
        collection: String,
        document: Any
    ): Flow<Response<ResponseType>> =
        flow {
            val result = firestore
                .collection(collection)
                .add(document)
                .await()

            val id = result.id

            emitAll(updateDocument<ResponseType>(collection, id, ID_COLUMN, id))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> getDocumentInSubCollection(
        collection: String,
        docId: String,
        subCollection: String
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .document(docId)
                .collection(subCollection)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> getDocumentById(
        collection: String,
        docId: String
    ): Flow<Response<ResponseType>> =
        flow {
            val result = firestore
                .collection(collection)
                .document(docId)
                .get()
                .await()

            if (result.exists()) {
                emit(Response.Success(result.toObject(ResponseType::class.java)!!))
            } else {
                emit(Response.Empty)
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType, FieldType> getDocumentByField(
        collection: String,
        field: String,
        value: FieldType
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .whereEqualTo(field, value)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType, FieldType> getDocumentByFieldAndOrderByTime(
        collection: String,
        field: String,
        value: FieldType
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .whereEqualTo(field, value)
                .orderBy(TIME_COLUMN)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            Log.e(TAG, it.message.toString())
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> getDocumentByArrayValue(
        collection: String,
        field: String,
        value: Any
    ): Flow<Response<List<ResponseType>>> =
        flow {
            val result = firestore
                .collection(collection)
                .whereArrayContains(field, value)
                .get()
                .await()

            if (result.isEmpty) {
                emit(Response.Empty)
            } else {
                emit(Response.Success(result.toObjects(ResponseType::class.java)))
            }
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> updateDocument(
        collection: String,
        docId: String,
        fieldName: String,
        value: Any
    ): Flow<Response<ResponseType>> =
        flow {
            firestore
                .collection(collection)
                .document(docId)
                .update(fieldName,value)
                .await()

            emitAll(getDocumentById<ResponseType>(collection, docId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    inline fun <reified ResponseType> updateDocument(
        collection: String,
        docId: String,
        value: MutableMap<String, Any>
    ): Flow<Response<ResponseType>> =
        flow {
            firestore
                .collection(collection)
                .document(docId)
                .update(value)
                .await()

            emitAll(getDocumentById<ResponseType>(collection, docId))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun uploadPicture(
        reference: String,
        fileName: String,
        pictureURI: Uri
    ): Flow<Response<String>> =
        flow {
            try {
                val filePath = storageRef
                    .child("$reference/$fileName.jpg")

                filePath.putFile(Uri.parse(pictureURI.toString())).await()

                val downloadUrl = filePath.downloadUrl.await()

                emit(Response.Success(downloadUrl.toString()))
            } catch (e: Exception) {
                emit(Response.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)

    fun deleteFile(
        reference: String,
        fileName: String
    ): Flow<Response<Unit>> =
        flow <Response<Unit>>{
            storage.reference
                .child(reference)
                .child("$fileName.jpg")
                .delete()
                .await()

            emit(Response.Success(Unit))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun deleteDocument(
        collection: String,
        docId: String
    ): Flow<Response<Unit>> =
        flow <Response<Unit>>{
            firestore
                .collection(collection)
                .document(docId)
                .delete()
                .await()

            emit(Response.Success(Unit))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun addValueToArrayOfDocument(
        collection: String,
        docId: String,
        fieldName: String,
        value: String
    ): Flow<Response<Unit>> =
        flow <Response<Unit>>{
            firestore
                .collection(collection)
                .document(docId)
                .update(fieldName, FieldValue.arrayUnion(value))
                .await()
            emit(Response.Success(Unit))
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun removeValueFromArrayOfDocument(
        collection: String,
        docId: String,
        fieldName: String,
        value: String
    ): Flow<Response<Unit>> =
        flow <Response<Unit>>{
            firestore.collection(collection)
                .document(docId)
                .update(fieldName, FieldValue.arrayRemove(value))
                .await()
        }.catch {
            emit(Response.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}