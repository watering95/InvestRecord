package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class Info_IODBHelper(context: Context) : DBHelper(context) {

    init {

        TABLE_NAME = "tbl_Info_IO"
        COLUMNS = arrayOf("date TEXT", "input INTEGER", "output INTEGER", "evaluation INTEGER", "spend_cash INTEGER", "spend_card INTEGER", "income INTEGER", "id_account INTEGER")
    }
}
