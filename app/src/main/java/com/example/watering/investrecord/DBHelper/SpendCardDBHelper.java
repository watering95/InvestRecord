package com.example.watering.investrecord.DBHelper;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class SpendCardDBHelper extends DBHelper {

    public SpendCardDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_spend_card";
        COLUMNS = new String [] {"spend_code TEXT UNIQUE","id_card INTEGER"};
    }
}
