package com.example.pacman;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

public class CountdownBonusThread extends Thread {

    private int time;
    private DrawingView dv;

    public CountdownBonusThread(DrawingView dv){
        this.dv = dv;
        this.time  = dv.getBonusResetTime();
    }
    public void run(){
        Log.i("info", "Counting bonus started");
        Looper.prepare();
        new CountDownTimer(5000, 1000) {

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
