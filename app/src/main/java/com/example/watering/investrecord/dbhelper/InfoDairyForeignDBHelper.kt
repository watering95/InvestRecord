package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class InfoDairyForeignDBHelper(context: Context) : DBHelper(context) {

    init {
        TABLE_NAME = "tbl_Info_Dairy_Foreign"
        COLUMNS = arrayOf("date TEXT", "principal REAL", "principal_krw INTEGER", "rate REAL", "id_account INTEGER", "id_currency INTEGER")
    }
}
