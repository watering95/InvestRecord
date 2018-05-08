package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class IncomeDBHelper(context: Context) : DBHelper(context) {

    init {
        TABLE_NAME = "tbl_income"
        COLUMNS = arrayOf("date TEXT", "id_sub INTEGER", "amount INTEGER", "details TEXT", "id_account INTEGER")
    }
}
