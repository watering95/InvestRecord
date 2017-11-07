package com.example.watering.investrecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by watering on 17. 10. 21.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int db_version = 1;
    private static final String DB_FILE_NAME = "InvestRecord.db";
    protected String TABLE_NAME;
    protected String [] COLUMNS;

    public DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

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

        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (";
        for (int i = 0; i< COLUMNS.length-1; i++) {
            sql += COLUMNS[i] + ", ";
        }
        sql += COLUMNS[COLUMNS.length-1] + ");";
        db.execSQL(sql);
    }

    public void setDelete(String whereClause, String[] whereArgs) {
        delete(TABLE_NAME, whereClause, whereArgs);
    }

    public void setItem(ContentValues values) {
        insert(TABLE_NAME, values);
    }


    public Cursor getAll() throws SQLiteException {
        return getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
    }

    protected void AllDelete(String tableName) throws SQLiteException {
        getWritableDatabase().delete(tableName, null, null);
    }

    protected void beginTransaction() {
        getWritableDatabase().beginTransaction();
    }

    protected void endTransaction() {
        getWritableDatabase().setTransactionSuccessful();
        getWritableDatabase().endTransaction();
    }

    protected void insert(String tableName, ContentValues values) throws SQLiteException {
        getWritableDatabase().insert(tableName, null, values);
    }

    protected void delete(String tableName, String where, String[] whereArgs) throws SQLiteException {
        String whereClause = where + "=?";
        getWritableDatabase().delete(tableName, whereClause, whereArgs);
    }
}
