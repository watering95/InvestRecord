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

public class GroupDBHelper extends DBHelper {

    public GroupDBHelper(Context context) {
        super(context);
        TABLE_NAME = "tbl_Group";
        COLUMNS = new String [] {"id_group INTEGER PRIMARY KEY",
                "name TEXT"};
    }
}
