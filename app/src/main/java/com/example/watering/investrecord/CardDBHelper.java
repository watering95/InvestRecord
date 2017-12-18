package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings({"ALL"})
class CardDBHelper extends DBHelper {

    public CardDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_card";
        COLUMNS = new String [] {"number TEXT UNIQUE","company TEXT","name TEXT","date_draw TEXT"};
    }
}
