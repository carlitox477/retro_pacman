package Game.GameCountDown;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

import Game.Character_package.Ghost;

public class CountdownGhostsState extends Thread {
    //Cambiar thread por runnable
    int time;
    int state;
    private Ghost[] ghosts;
    boolean cancel = false;


    public CountdownGhostsState(Ghost[] ghosts, int state){
        this.ghosts = ghosts;
        this.state = state;
        if(state == 1){ // Scattering state
            time = 12000;
            this.scatterGhosts();
        }else if(state == 2){
            time = 10000;
            this.frightenGhosts();
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
                switch (state){
                    case 0:
                        Log.i("CD GS","End Scattering");
                        chaseGhosts();
                        break;
                    case 1:
                        Log.i("CD GS","End Chasing");
                        scatterGhosts();
                        break;
                    case 2:
                        frightenGhosts();
                        break;
                }
            }
        }.start();
        Looper.loop();

    }
    public void cancelTimer(){
        cancel = true;
    }

    private void chaseGhosts() {
        Log.i("CD GS","Chasing");
        for (int i = 0; i < ghosts.length; i++) {
            if(!ghosts[i].getState().isRespawning()){
                ghosts[i].setChaseBehaviour();
            }
        }
    }

    private void frightenGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setFrightenedBehaviour();
        }
    }

    private void scatterGhosts() {
        Log.i("CD GS","Scatter");
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setScatterBehaviour();
        }
    }


}
