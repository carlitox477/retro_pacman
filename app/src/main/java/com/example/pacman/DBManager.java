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
                + ScoreContract.ScoreEntry.NICKNAME + " TEXT PRIMARY KEY,"
                + ScoreContract.ScoreEntry.MAX_SCORE + " REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAllScores(){
        return getReadableDatabase().query(ScoreContract.ScoreEntry.TABLE_NAME, null, null, null, null, null, ScoreContract.ScoreEntry.MAX_SCORE + " DESC");
    }

    public long updateScore(Score s){
        //We only update the score if the same is higher than once before
        long row;
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        row=sqLiteDatabase.update(ScoreContract.ScoreEntry.TABLE_NAME, s.toContentValues(), ScoreContract.ScoreEntry.NICKNAME+"="+s.getNickname()+" AND "+ScoreContract.ScoreEntry.MAX_SCORE+"<"+s.getScore(),null);
        sqLiteDatabase.close();
        return row;
    }

    public long saveScore(Score s){
        long row;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        row= sqLiteDatabase.insert(ScoreContract.ScoreEntry.TABLE_NAME, null, s.toContentValues());

        sqLiteDatabase.close();
        return row;
    }

    public String getMaxScore(){
        String sal;
        return "";
    }

    public Score getMyPreviousMaxScore(String nickname){
        Score result;
        int iNickname,iMaxScore;
        Cursor scoreQuery;

        return null;
    }

    public Cursor getScoresByNickname(){
        return getReadableDatabase().query(ScoreContract.ScoreEntry.TABLE_NAME, null, null, null, null, null, ScoreContract.ScoreEntry.MAX_SCORE + " DESC");
    }

}
