package com.example.watering.investrecord.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

/**
 * Created by watering on 17. 10. 21.
 */

open class DBHelper internal constructor(context: Context) : SQLiteOpenHelper(context, DB_FILE_NAME, null, db_version) {
    internal var TABLE_NAME: String? = null
    internal var COLUMNS: Array<String>? = null

    override fun onCreate(db: SQLiteDatabase) {
        val sql: StringBuilder

        sql = StringBuilder("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ")

        for (COLUMN in COLUMNS!!) {
            sql.append(", ").append(COLUMN)
        }
        sql.append(");")
        db.execSQL(sql.toString())
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)

        val sql: StringBuilder

        sql = StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append(" (")
                .append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT")

        for (COLUMN in COLUMNS!!) {
            sql.append(", ").append(COLUMN)
        }
        sql.append(");")
        db.execSQL(sql.toString())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME!!)
        onCreate(db)
    }

    @Throws(SQLiteException::class)
    fun insert(values: ContentValues) {
        writableDatabase.insert(TABLE_NAME, null, values)
    }

    @Throws(SQLiteException::class)
    fun delete(where: String?, whereArgs: Array<String>) {
        val selection: String?

        if (where == null) {
            selection = null
        } else {
            selection = "$where=?"
        }
        writableDatabase.delete(TABLE_NAME, selection, whereArgs)
    }

    @Throws(SQLiteException::class)
    fun update(values: ContentValues, where: String, selectionArgs: Array<String>) {
        writableDatabase.update(TABLE_NAME, values, "$where=?", selectionArgs)
    }

    @Throws(SQLiteException::class)
    fun query(columns: Array<String>?, selection: String?, selectionArgs: Array<String>?, orderBy: String?): Cursor {
        return readableDatabase.query(TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy)
    }

    companion object {

        private val db_version = 7
        private val DB_FILE_NAME = "InvestRecord.db"
    }
}
