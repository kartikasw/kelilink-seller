package com.example.kelilinkseller.core.data.helper

object Constants {

    const val CONTENT_TYPE = "application/json"
    const val SERVER_KEY = "AAAAOmbSqCc:APA91bFwPtZtHkwI54WxPolQmg5EO8n0a8shIPB0eyRoIfUpjqIew1v9UyOh1oSg17mC1SIQpuBGVdTQ9umgn68k6aYOn7PIL2Hy1StcwDl9-8j_iDA1AfVWUbOz-w9P_PrQBDgVEAmZ"

    const val PREFERENCE_NAME = "KelilinkSeller_Preference"
    const val DATABASE_NAME = "KelilinkSeller.db"

    const val CHANNEL_ID = "KelilinkSeller_Channel"
    const val CHANNEL_NAME = "KelilinkSeller"

    object PreferenceValue {
        const val FCM_TOKEN = "fcm_token"
        const val INVOICE_ID = "invoice_id"
    }

    object EXTRA {
        const val EXTRA_INVOICE_ID = "invoice_id"
    }

    object DatabaseCollection {
        const val SELLER_COLLECTION = "seller"
        const val STORE_COLLECTION = "store"
        const val MENU_COLLECTION = "menu"
        const val ORDER_COLLECTION = "order"
        const val ORDERS_COLLECTION = "orders"
        const val INVOICE_COLLECTION = "invoice"
    }

    object DatabaseColumn {
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "name"
        const val FCM_TOKEN_COLUMN = "fcm_token"
        const val CATEGORY_COLUMN = "categories"
        const val ADDRESS_COLUMN = "address"
        const val OPERATING_STATUS_COLUMN = "operating_status"
        const val LATITUDE_COLUMN = "lat"
        const val LONGITUDE_COLUMN = "lon"
        const val IMAGE_COLUMN = "image"
        const val PRICE_COLUMN = "price"
        const val STORE_ID_COLUMN = "store_id"
        const val AVAILABLE_COLUMN = "available"
        const val STATUS_COLUMN = "status"
        const val UNIT_COLUMN = "unit"
        const val TIME_COLUMN = "time"
        const val QUEUE_ORDER_COLUMN = "queue_order"
        const val QUEUE_COLUMN = "queue"
    }

    object ORDER_STATUS {
        const val WAITING = "waiting"
        const val COOKING = "cooking"
        const val READY = "ready"
        const val DONE = "done"
        const val DECLINED = "declined"
    }

    object Table {
        const val USER_TABLE = "user_table"
        const val MENU_TABLE = "menu_table"
    }
}