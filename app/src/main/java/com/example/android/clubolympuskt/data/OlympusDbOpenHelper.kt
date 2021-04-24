package com.example.android.clubolympuskt.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OlympusDbOpenHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_MEMBERS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_GENDER + " INTEGER NOT NULL,"
                + COLUMN_SPORT + " TEXT" + ")")
        db.execSQL(CREATE_MEMBERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME)
        onCreate(db)
    }
}