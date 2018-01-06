package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
class SpendCashDBHelper extends DBHelper {

    public SpendCashDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_spend_cash";
        COLUMNS = new String [] {"spend_code TEXT UNIQUE","id_account INTEGER"};
    }
}
