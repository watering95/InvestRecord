package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class SpendScheduleDBHelper extends DBHelper {

    public SpendScheduleDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_spend_schedule";
        COLUMNS = new String [] {"spend_code TEXT UNIQUE","date_draw TEXT","id_account INTEGER","id_card INTEGER"};
    }
}
