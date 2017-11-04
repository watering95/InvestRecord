package com.example.watering.investrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 10. 21.
 */

public class Info_IODBHelper extends DBHelper {

    public Info_IODBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Info_IO";
        COLUMNS = new String [] {"date TEXT INTEGER PRIMARY KEY",
                "input INTEGER","output INTEGER",
                "FOREIGN KEY(id_account) REFERENCES tbl_Account(id_account)"};
    }

    @Override
    public List<Info_IO> getItem() {
        List<Info_IO> list = new ArrayList<>();
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
                        int input = c.getInt(3);
                        int output = c.getInt(4);
                        list.add(new Info_IO(date,id,input,output));
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
    public void setItem(String date, int id, int input, int output) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("id", id);
        values.put("input", input);
        values.put("output", output);
        insert(TABLE_NAME, values);
    }
}
