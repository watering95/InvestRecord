package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
class CategorySubDBHelper extends DBHelper {

    public CategorySubDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_category_sub";
        COLUMNS = new String [] {"name TEXT UNIQUE","id_main INTEGER"};
    }
}
