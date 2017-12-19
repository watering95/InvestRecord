package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class SpendCardDBHelper extends DBHelper {

    public SpendCardDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_spend_card";
        COLUMNS = new String [] {"id_spend INTEGER UNIQUE","id_card INTEGER"};
    }
}
