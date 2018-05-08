package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class AccountDBHelper(context: Context) : DBHelper(context) {

    init {
        TABLE_NAME = "tbl_Account"
        COLUMNS = arrayOf("institute TEXT", "number TEXT UNIQUE", "description TEXT", "id_group INTEGER")
    }
}
