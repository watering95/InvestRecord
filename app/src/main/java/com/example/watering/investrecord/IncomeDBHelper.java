package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class IncomeDBHelper extends DBHelper {

    public IncomeDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_income";
        COLUMNS = new String [] {"date TEXT","id_sub INTEGER","amount INTEGER","details TEXT","id_account INTEGER"};
    }
}
