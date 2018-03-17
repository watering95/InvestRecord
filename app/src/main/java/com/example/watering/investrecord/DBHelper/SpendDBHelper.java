package com.example.watering.investrecord.DBHelper;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class SpendDBHelper extends DBHelper {

    public SpendDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_spend";
        COLUMNS = new String [] {"spend_code TEXT UNIQUE","details TEXT","id_sub INTEGER","date_use TEXT","amount INTEGER"};
    }
}
