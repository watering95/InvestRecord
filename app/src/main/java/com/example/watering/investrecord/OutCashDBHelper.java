package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class OutCashDBHelper extends DBHelper {

    public OutCashDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_out_cash";
        COLUMNS = new String [] {"id_out INTEGER UNIQUE","date_draw TEXT"};
    }
}
