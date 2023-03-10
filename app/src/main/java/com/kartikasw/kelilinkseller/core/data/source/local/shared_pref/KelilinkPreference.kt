package com.kartikasw.kelilinkseller.core.data.source.local.shared_pref

import android.content.Context
import android.content.SharedPreferences
import com.kartikasw.kelilinkseller.core.data.helper.Constants.PREFERENCE_NAME
import com.kartikasw.kelilinkseller.core.data.helper.Constants.PreferenceValue.FCM_TOKEN
import com.kartikasw.kelilinkseller.core.data.helper.Constants.PreferenceValue.FIRST_RUN
import com.kartikasw.kelilinkseller.core.data.helper.Constants.PreferenceValue.INVOICE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KelilinkPreference @Inject constructor(@ApplicationContext context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val editor = pref.edit()

    fun setFirstRun(status: Boolean) {
        editor.putBoolean(FIRST_RUN, status)
        editor.apply()
    }

    fun getFirstRun(): Boolean =
        pref.getBoolean(FIRST_RUN, true)

    fun setFcmToken(token: String) {
        editor.putString(FCM_TOKEN, token)
        editor.apply()
    }

    fun getFcmToken(): String? =
        pref.getString(FCM_TOKEN, "")

    fun setInvoiceId(id: String) {
        editor.putString(INVOICE_ID, id)
        editor.apply()
    }

    fun getInvoiceId(): String? =
        pref.getString(INVOICE_ID, "")

}