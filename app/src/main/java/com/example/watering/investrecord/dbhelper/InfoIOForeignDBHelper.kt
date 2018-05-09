package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class InfoIOForeignDBHelper(context: Context) : DBHelper(context) {

    init {

        TABLE_NAME = "tbl_Info_IO_Foreign"
        COLUMNS = arrayOf("date TEXT", "input INTEGER", "input_krw INTEGER", "output INTEGER", "output_krw INTEGER", "evaluation INTEGER", "id_account INTEGER", "id_currency INTEGER")
    }
}
