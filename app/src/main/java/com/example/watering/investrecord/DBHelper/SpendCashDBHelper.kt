package com.example.watering.investrecord.DBHelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class SpendCashDBHelper(context: Context) : DBHelper(context) {

    init {

        TABLE_NAME = "tbl_spend_cash"
        COLUMNS = arrayOf("spend_code TEXT UNIQUE", "id_account INTEGER")
    }
}
