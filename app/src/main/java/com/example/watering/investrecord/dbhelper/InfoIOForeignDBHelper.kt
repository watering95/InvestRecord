package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class InfoIOForeignDBHelper(context: Context) : DBHelper(context) {

    init {

        TABLE_NAME = "tbl_Info_IO_Foreign"
        COLUMNS = arrayOf("date TEXT", "input REAL", "input_krw INTEGER", "output REAL", "output_krw INTEGER", "evaluation REAL", "id_account INTEGER", "id_currency INTEGER")
    }
}
