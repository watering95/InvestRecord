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

public class AccountDBHelper extends DBHelper {

    public AccountDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Account";
        COLUMNS = new String [] {"id_account INTEGER PRIMARY KEY",
                "inst TEXT","num TEXT","disc TEXT",
                "id_group INTEGER"};
    }

    public List<Account> getItem() {
        List<Account> list = new ArrayList<>();
        try {
            beginTransaction();
            Cursor c = getAll();
            if(c != null) {
                int total = c.getCount();
                if(total > 0) {
                    while(c.moveToNext()) {
                        Account account = new Account();
                        account.setId(c.getInt(0));
                        account.setInstitute(c.getString(1));
                        account.setNumber(c.getString(2));
                        account.setDiscription(c.getString(3));
                        account.setGroup(c.getInt(4));
                        list.add(account);
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

    public void setItem(int id_account, String institute, String number, String discription, int id_group) {
        ContentValues values = new ContentValues();
        values.put("id_account", id_account);
        values.put("inst", institute);
        values.put("num", number);
        values.put("disc", discription);
        values.put("id_group", id_group);
        insert(TABLE_NAME, values);
    }
}
