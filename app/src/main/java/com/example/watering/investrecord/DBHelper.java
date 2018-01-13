package com.example.watering.investrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
class DBHelper extends SQLiteOpenHelper {

    private static final int db_version = 6;
    private static final String DB_FILE_NAME = "InvestRecord.db";
    String TABLE_NAME;
    String [] COLUMNS;

    DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql;

        sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ");

        for (String COLUMN : COLUMNS) {
            sql.append(", ").append(COLUMN);
        }
        sql.append(");");
        db.execSQL(sql.toString());
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        StringBuilder sql;

        sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT");

        for (String COLUMN : COLUMNS) {
            sql.append(", ").append(COLUMN);
        }
        sql.append(");");
        db.execSQL(sql.toString());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    void insert(ContentValues values) throws SQLiteException {
        getWritableDatabase().insert(TABLE_NAME, null, values);
    }
    void delete(String where, String[] whereArgs) throws SQLiteException {
        String selection;

        if(where == null) {
            selection = null;
        }
        else {
            selection = where + "=?";
        }
        getWritableDatabase().delete(TABLE_NAME, selection, whereArgs);
    }
    void update(ContentValues values, String where, String[] selectionArgs) throws SQLiteException {
        String selection = where + "=?";
        getWritableDatabase().update(TABLE_NAME, values, selection, selectionArgs);
    }
    Cursor query(String[] columns, String selection, String[] selectionArgs, String orderBy) throws SQLiteException {
        return getReadableDatabase().query(TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);
    }
}
