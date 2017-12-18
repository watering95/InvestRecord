package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class OutDBHelper extends DBHelper {

    public OutDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_out";
        COLUMNS = new String [] {"details TEXT","id_sub INTEGER","date_use TEXT","amount INTEGER"};
    }
}
