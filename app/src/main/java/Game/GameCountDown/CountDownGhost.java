package Game.GameCountDown;

import android.os.CountDownTimer;
import android.util.Log;

import Game.Character_package.Ghost;

public class CountDownGhost implements Runnable {
    private static int[][] SCATTER_TIMES=new int[][]{
            {7000, 7000, 5000, 5000},   //L1
            {7000, 7000, 5000, 1000},   //L2-L5
            {5000, 5000, 5000, 1000},   //L+5
    };
    private static int[][] CHASE_TIMES=new int[][]{
            {20000, 20000, 20000, 20000},   //L1
            {20000, 20000, 1033000, 20000}, //L2-L5
            {20000, 20000, 1037000, 20000}, //L+5
    };
    private int time,level;
    private char state;
    private Ghost ghost;
    private boolean cancel;

    @Override
    public void run() {
        new CountDownTimer(this.time, 1000) {
            public void onTick(long millisUntilFinished) {
                if(cancel){
                    this.cancel();
                }
            }

            public void onFinish() {
                Log.i("CD GS","End Chasing");

            }
        }.start();
    }

    private void changState(){
        switch (this.state){
            case 'c':
                this.ghost.setChaseBehaviour();
                break;
            case 's':
                this.ghost.setScatterBehaviour();
                break;
            case 'f':
                this.ghost.setFrightenedBehaviour();
                break;
            case 'r':
                this.ghost.respawn();
                break;
            default:
                break;
        }
    }
}
