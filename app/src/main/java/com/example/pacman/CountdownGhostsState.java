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

        if(state == "Scatter")
            dv.scatterGhosts();
        else if(state == "Frighten")
            dv.frightenGhosts();

        Looper.prepare();
        new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                dv.resetGhosts();
            }
        }.start();
        Looper.loop();

    }
}
