package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class SpendCashDBHelper extends DBHelper {

    public SpendCashDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_spend_cash";
        COLUMNS = new String [] {"id_spend INTEGER UNIQUE","date_draw TEXT"};
    }
}
