package com.example.watering.investrecord.DBHelper;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class CategorySubDBHelper extends DBHelper {

    public CategorySubDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_category_sub";
        COLUMNS = new String [] {"name TEXT","id_main INTEGER"};
    }
}
