package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class InfoDairyKRWDBHelper(context: Context) : DBHelper(context) {

    init {
        TABLE_NAME = "tbl_Info_Dairy"
        COLUMNS = arrayOf("date TEXT", "principal INTEGER", "rate REAL", "id_account INTEGER")
    }
}
