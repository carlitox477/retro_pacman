package com.example.pacman;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class CountdownGhostsState extends Thread {

    int time;
    int state;
    GameView gv;

    boolean cancel = false;

    public Handler mHandler;

    public CountdownGhostsState(GameView gv, int state){
        this.gv = gv;
        this.state = state;
        if(state == 1){ // Scattering state
            time = 12000;
            gv.scatterGhosts();
        }else if(state == 2){
            time = 10000;
            gv.frightenGhosts();
        } // Frightened state

    }

    public void run(){
        Looper.prepare();


        new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                if(cancel){
                    this.cancel();
                }
            }


            public void onFinish() {
               gv.resetGhosts();
            }
        }.start();
        Looper.loop();

    }
    public void cancelTimer(){
        cancel = true;
    }

}
