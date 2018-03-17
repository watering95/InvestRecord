package com.example.watering.investrecord.DBHelper;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Info_DairyDBHelper extends DBHelper {

    public Info_DairyDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Info_Dairy";
        COLUMNS = new String [] {"date TEXT", "principal INTEGER", "rate REAL",
                "id_account INTEGER"};
    }
}
