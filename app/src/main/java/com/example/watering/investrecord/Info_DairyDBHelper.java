package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

public class Info_DairyDBHelper extends DBHelper {

    public Info_DairyDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Info_Dairy";
        COLUMNS = new String [] {"date TEXT INTEGER PRIMARY KEY",
                "principal INTEGER","rate REAL",
                "id_account INTEGER"};
    }
}
