package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class SpendCardDBHelper(context: Context) : DBHelper(context) {

    init {

        TABLE_NAME = "tbl_spend_card"
        COLUMNS = arrayOf("spend_code TEXT UNIQUE", "id_card INTEGER")
    }
}
