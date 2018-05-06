package com.example.watering.investrecord.DBHelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class CardDBHelper(context: Context) : DBHelper(context) {

    init {
        TABLE_NAME = "tbl_card"
        COLUMNS = arrayOf("number TEXT UNIQUE", "company TEXT", "name TEXT", "date_draw TEXT", "id_account INTEGER")
    }
}
