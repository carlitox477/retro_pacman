package com.example.pacman;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

public class DBManager extends SQLiteOpenHelper implements Serializable {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pacman_scores.db";
    SQLiteDatabase db = getWritableDatabase();

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Creation","Initialize db");


        //Creates the table upon instatiation of this class.
        db.execSQL(" CREATE TABLE " + ScoreContract.ScoreEntry.TABLE_NAME + " ("
                + ScoreContract.ScoreEntry.NICKNAME + " TEXT PRIMARY KEY AUTOINCREMENT,"
                + ScoreContract.ScoreEntry.MAX_SCORE + " REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAllContacts(){
        return getReadableDatabase().query(ScoreContract.ScoreEntry.TABLE_NAME, null, null, null, null, null, ScoreContract.ScoreEntry.MAX_SCORE + " DESC");
    }

    public long saveContact(Score s){
        long row;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        row= sqLiteDatabase.insert(ScoreContract.ScoreEntry.TABLE_NAME, null, s.toContentValues());

        sqLiteDatabase.close();
        return row;
    }

}
