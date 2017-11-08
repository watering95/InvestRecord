package com.example.watering.investrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Info_DairyDBHelper extends DBHelper {

    public Info_DairyDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Info_Dairy";
        COLUMNS = new String [] {"date TEXT INTEGER PRIMARY KEY",
                "principal INTEGER","evaluation INTEGER","rate REAL",
                "id_account INTEGER"};
    }
}
