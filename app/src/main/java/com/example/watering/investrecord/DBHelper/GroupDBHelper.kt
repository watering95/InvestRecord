package com.example.watering.investrecord.DBHelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class GroupDBHelper(context: Context) : DBHelper(context) {

    init {
        TABLE_NAME = "tbl_Group"
        COLUMNS = arrayOf("name TEXT UNIQUE")
    }
}


