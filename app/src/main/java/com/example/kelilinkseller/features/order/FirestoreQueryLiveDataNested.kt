package com.example.kelilinkseller.features.order

import androidx.lifecycle.LiveData
import com.example.kelilinkseller.core.domain.model.Order
import com.google.firebase.firestore.*

class FirestoreQueryLiveDataNested(
    private val query: Query
): LiveData<List<Order>>(), EventListener<QuerySnapshot> {

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
        val items = ArrayList<Order>()
        for (doc in value!!) {
            doc.toObject(Order::class.java).let {
                items.add(it)
            }
        }
        postValue(items)
    }
}