package com.example.watering.investrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class UserDBHelper extends DBHelper {

    public UserDBHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        TABLE_NAME = "tbl_User";
        COLUMNS = new String [] {"id_user INTEGER PRIMARY KEY AUTOINCREAMENT",
                "name TEXT"};

        super.onCreate(db);
    }

    @Override
    public List<User> getItem() {
        List<User> list = new ArrayList<>();
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

    @Override
    public void setItem(int id, String name) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        insert(TABLE_NAME, values);
    }
}
