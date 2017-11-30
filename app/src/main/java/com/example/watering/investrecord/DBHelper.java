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

public class DBHelper extends SQLiteOpenHelper {

    private static final int db_version = 1;
    private static final String DB_FILE_NAME = "InvestRecord.db";
    protected String TABLE_NAME;
    protected String [] COLUMNS;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;

        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ";

        for (int i = 0; i< COLUMNS.length; i++) {
            sql += ", " + COLUMNS[i];
        }
        sql += ");";
        db.execSQL(sql);
    }

    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, db_version);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        String sql;

        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ";

        for (int i = 0; i< COLUMNS.length; i++) {
            sql += ", " + COLUMNS[i];
        }
        sql += ");";
        db.execSQL(sql);
    }

    protected void beginTransaction() {
        getWritableDatabase().beginTransaction();
    }

    protected void endTransaction() {
        getWritableDatabase().setTransactionSuccessful();
        getWritableDatabase().endTransaction();
    }

    protected void insert(ContentValues values) throws SQLiteException {
        getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    protected Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) throws SQLiteException {
        return getReadableDatabase().query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    protected void delete(String where, String[] whereArgs) throws SQLiteException {
        String selection;

        if(where == null) {
            selection = null;
        }
        else {
            selection = where + "=?";
        }
        getWritableDatabase().delete(TABLE_NAME, selection, whereArgs);
    }

    protected void update(ContentValues values, String where, String[] selectionArgs) {
        String selection = where + "=?";
        getWritableDatabase().update(TABLE_NAME, values, selection, selectionArgs);
    }
}
