package com.example.pacman;

import android.provider.BaseColumns;

public class ScoreContract {

    public static abstract class ScoreEntry implements BaseColumns{
        public static final String TABLE_NAME="score";
        public static final String NICKNAME="nickname";
        public static final String MAX_SCORE="max_score";
    }
}
