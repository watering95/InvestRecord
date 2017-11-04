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

public class AccountDBHelper extends DBHelper {

    public AccountDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Account";
        COLUMNS = new String [] {"id_account INTEGER PRIMARY KEY AUTOINCREAMENT",
                "inst TEXT","code TEXT","disc TEXT",
                "FOREIGN KEY(id_user) REFERENCES tbl_User(id_user)"};
    }

    @Override
    public List<Account> getItem() {
        List<Account> list = new ArrayList<>();
        try {
            beginTransaction();
            Cursor c = getAll();
            if(c != null) {
                int total = c.getCount();
                if(total > 0) {
                    c.moveToFirst();
                    while(!c.isAfterLast()) {
                        int id_user = c.getInt(1);
                        int id = c.getInt(2);
                        String institute = c.getString(3);
                        String discription = c.getString(4);
                        String number = c.getString(5);
                        list.add(new Account(id,id_user,institute,discription,number));
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
    public void setItem(int user, int id, String institute, String discription, String number) {
        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("id", id);
        values.put("inst", institute);
        values.put("disc", discription);
        values.put("num", number);
        insert(TABLE_NAME, values);
    }
}
