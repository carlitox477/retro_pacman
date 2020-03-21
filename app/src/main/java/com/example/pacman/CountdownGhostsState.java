package com.example.pacman;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

public class CountdownGhostsState extends Thread {

    int time;
    String state;
    GameView dv;
    public CountdownGhostsState(GameView dv, String state){
        this.dv = dv;
        this.state = state;
        switch (state){
            case "Scatter": time = 15000;
            break;
            case "Frighten": time = 10000;
            break;
        }
    }

    public void run(){


    }
}
