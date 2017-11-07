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

    public List<Group> getItem() {
        List<Group> list = new ArrayList<>();
        try {
            beginTransaction();
            Cursor c = getAll();
            if(c != null) {
                int total = c.getCount();
                if(total > 0) {
                    c.moveToFirst();
                    while(!c.isAfterLast()) {
                        int id = c.getInt(1);
                        String name = c.getString(2);
                        c.moveToNext();
                    }
                }
                c.close();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            endTransaction();
        }
        return list;
    }

    public void setItem(int id, String name) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        insert(TABLE_NAME, values);
    }
}
