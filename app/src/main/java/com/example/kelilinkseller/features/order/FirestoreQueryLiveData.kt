package com.example.kelilinkseller.features.order

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

class FirestoreQueryLiveData<ResponseType>(
    private val query: Query,
    private val responseType: ResponseType
): LiveData<List<ResponseType>>(), EventListener<QuerySnapshot> {

    private lateinit var registration: ListenerRegistration

    override fun onActive() {
        super.onActive()
        registration = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        registration.remove()
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        val items = ArrayList<ResponseType>()
        for (doc in value!!) {
            doc.toObject(responseType!!::class.java).let {
                items.add(it)
            }
        }
        postValue(items)
    }
}