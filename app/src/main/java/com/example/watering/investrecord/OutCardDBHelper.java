package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class OutCardDBHelper extends DBHelper {

    public OutCardDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_out_card";
        COLUMNS = new String [] {"id_out INTEGER UNIQUE","id_card INTEGER"};
    }
}
