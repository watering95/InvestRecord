package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class OutScheduleDBHelper extends DBHelper {

    public OutScheduleDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_out_schedule";
        COLUMNS = new String [] {"id_out INTEGER UNIQUE","date_draw TEXT","id_account INTEGER","id_card INTEGER"};
    }
}
