package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class SpendDBHelper(context: Context) : DBHelper(context) {

    init {

        TABLE_NAME = "tbl_spend"
        COLUMNS = arrayOf("spend_code TEXT UNIQUE", "details TEXT", "id_sub INTEGER", "date_use TEXT", "amount INTEGER")
    }
}
