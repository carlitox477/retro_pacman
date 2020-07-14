package com.example.pacman;

import android.os.Build;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class CountdownBonusThread extends Thread {
    private int time;
    private GameView gv;


    public CountdownBonusThread(GameView gv) {
        this.gv = gv;
        this.time = gv.getBonusResetTime();
    }

    public void run() {
        Log.i("info", "Counting bonus started");
        Looper.prepare();
        new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {

            }


            public void onFinish() {
                Log.i("info", "Counting bonus reseted");
                gv.setBonusAvailable();
            }
        }.start();
        Looper.loop();
    }
}
