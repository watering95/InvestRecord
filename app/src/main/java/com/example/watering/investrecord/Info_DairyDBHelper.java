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

    public List<Info_Dairy> getItem() {
        List<Info_Dairy> list = new ArrayList<>();
        try {
            beginTransaction();
            Cursor c = getAll();
            if(c != null) {
                int total = c.getCount();
                if(total > 0) {
                    c.moveToFirst();
                    while(!c.isAfterLast()) {
                        String date = c.getString(1);
                        int id = c.getInt(2);
                        int principal = c.getInt(3);
                        int evaluation = c.getInt(4);
                        double rate = c.getDouble(5);
                        list.add(new Info_Dairy(date,id,principal,evaluation,rate));
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

    public void setItem(String date, int id, String institute, String discription, String number) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("id", id);
        values.put("inst", institute);
        values.put("disc", discription);
        values.put("num", number);
        insert(TABLE_NAME, values);
    }
}
