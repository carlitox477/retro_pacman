package com.example.pacman;

import android.os.Build;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class CountdownBonusThread extends Thread {




    private int time;
    private GameView dv;


    public CountdownBonusThread(GameView dv) {
        this.dv = dv;
        this.time = dv.getBonusResetTime();
    }

    public void run() {
        Log.i("info", "Counting bonus started");
        Looper.prepare();
        new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("info", "Counting bonus ticked");
            }


            public void onFinish() {
                Log.i("info", "Counting bonus reseted");
                dv.setBonusAvailable();


            }
        }.start();
        Looper.loop();
    }
}
