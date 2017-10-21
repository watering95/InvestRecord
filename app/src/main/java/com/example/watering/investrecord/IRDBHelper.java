package com.example.watering.investrecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by watering on 17. 10. 21.
 */

public class IRDBHelper extends SQLiteOpenHelper {
    public IRDBHelper(Context context) {
        super(context, "InvestRecord.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tblAsset (asset_id INTEGER PRIMARY KEY AUTOINCREAMENT, " +
                "inst TEXT, code TEXT, disc TEXT);");
        db.execSQL("CREATE TABLE tblIoinfo (date TEXT, input INTEGER, output INTEGER," +
                "FOREIGN KEY(asset_id) REFERENCES tblAsset(asset_id));");
        db.execSQL("CREATE TABLE tblDailyinfo (date TEXT, principal INTEGER, evaluation INTEGER" +
                "ratio REAL);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tblAsset");
        db.execSQL("DROP TABLE IF EXISTS tblIoinfo");
        db.execSQL("DROP TABLE IF EXISTS tblDailyinfo");
        onCreate(db);
    }
}
