package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

public class GroupDBHelper extends DBHelper {

    public GroupDBHelper(Context context) {
        super(context);
        TABLE_NAME = "tbl_Group";
        COLUMNS = new String [] {"name TEXT UNIQUE"};
    }
}


