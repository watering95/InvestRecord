package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
class CategoryMainDBHelper extends DBHelper {

    public CategoryMainDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_category_main";
        COLUMNS = new String [] {"name TEXT UNIQUE","kind TEXT"};
    }
}
