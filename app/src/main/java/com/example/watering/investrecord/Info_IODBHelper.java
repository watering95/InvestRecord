package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("ALL")
class Info_IODBHelper extends DBHelper {

    public Info_IODBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Info_IO";
        COLUMNS = new String [] { "date TEXT", "input INTEGER","output INTEGER","evaluation INTEGER"
                ,"spend INTEGER", "income INTEGER", "id_account INTEGER"};
    }
}
