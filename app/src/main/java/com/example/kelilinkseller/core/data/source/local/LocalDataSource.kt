package com.example.kelilinkseller.core.data.source.local

import com.example.kelilinkseller.core.data.source.local.shared_pref.KelilinkPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val pref: KelilinkPreference
) {
    fun setFcmToken(token: String) {
        pref.setFcmToken(token)
    }

    fun getFcmToken() =
        pref.getFcmToken()
}