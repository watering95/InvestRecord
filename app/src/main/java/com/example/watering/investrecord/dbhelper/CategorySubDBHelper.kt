package com.example.watering.investrecord.dbhelper

import android.content.Context

/**
 * Created by watering on 17. 10. 21.
 */

class CategorySubDBHelper(context: Context) : DBHelper(context) {

    init {
        TABLE_NAME = "tbl_category_sub"
        COLUMNS = arrayOf("name TEXT", "id_main INTEGER")
    }
}
