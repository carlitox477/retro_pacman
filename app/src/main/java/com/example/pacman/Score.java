package com.example.pacman;

import android.content.ContentValues;

public class Score {
    private String nickname;
    private double score;

    public  Score (String nickname, double score){
        this.nickname=nickname;
        this.score=score;
    }

    public String getNickname() {
        return nickname;
    }

    public double getScore() {
        return score;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public ContentValues toContentValues() {
        //Creates an instance of ContentValues storing all the information of the score
        ContentValues values = new ContentValues();
        values.put(ScoreContract.ScoreEntry.NICKNAME, nickname);
        values.put(ScoreContract.ScoreEntry.MAX_SCORE, score);
        return values;
    }
}

