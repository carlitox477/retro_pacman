package Game.GameCountDown;

import android.os.CountDownTimer;
import android.os.Looper;

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
               resetGhosts();
            }
        }.start();
        Looper.loop();

    }
    public void cancelTimer(){
        cancel = true;
    }

    private void resetGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            if(!ghosts[i].getState().isRespawning())
                ghosts[i].setChaseBehaviour();
        }
    }

    private void frightenGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setFrightenedBehaviour();
        }
    }

    private void scatterGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setScatterBehaviour();
        }
    }


}
