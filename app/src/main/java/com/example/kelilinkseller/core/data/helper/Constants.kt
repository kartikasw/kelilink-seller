package com.example.kelilinkseller.core.data.helper

object Constants {
    const val DATABASE_NAME = "Kelilink.db"

    object DatabaseCollection {
        const val SELLER_COLLECTION = "seller"
        const val STORE_COLLECTION = "store"
        const val MENU_COLLECTION = "menu"
        const val ORDER_COLLECTION = "order"
        const val INVOICE_COLLECTION = "invoice"
    }

    object DatabaseColumn {
        const val UID_COLUMN = "uid"
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "name"
        const val EMAIL_COLUMN = "email"
        const val PHONE_NUMBER_COLUMN = "phone_number"
        const val CATEGORY_COLUMN = "categories"
        const val ADDRESS_COLUMN = "address"
        const val CLOSE_HOUR_COLUMN =  "close_hour"
        const val OPERATING_STATUS_COLUMN = "operating_status"
        const val INFO_COLUMN = "info"
        const val IN_QUEUE_COLUMN = "in_queue"
        const val LATITUDE_COLUMN = "lat"
        const val LONGITUDE_COLUMN = "lon"
        const val IMAGE_COLUMN = "image"
        const val IS_SELECTED_COLUMN = "is_selected"
        const val AMOUNT_COLUMN = "amount"
        const val PRICE_COLUMN = "price"
        const val PER_PRICE_COLUMN = "per_price"
        const val STORE_ID_COLUMN = "store_id"
        const val PKL_NAME_COLUMN = "pkl_name"
        const val USER_ID_COLUMN = "user_id"
        const val NOTE_COLUMN = "note"
        const val TOTAL_PRICE_COLUMN = "total_price"
        const val AVAILABLE_COLUMN = "available"
        const val STATUS_COLUMN = "status"
        const val UNIT_COLUMN = "unit"
        const val TIMESTAMP_COLUMN = "timestamp"
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