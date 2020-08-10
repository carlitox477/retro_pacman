package Game.GameCountDown;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

import Game.Character_package.Ghost;

public class CountDownScareGhosts extends Thread{
    private static int TIME=12000; //miliseconds
    private int[][] map;
    private Ghost[] ghosts;
    private boolean frameChanged,ended;
    private CountDownTimer countDownTimer;


    public CountDownScareGhosts(Ghost[]ghosts,int[][] map){
        this.map=map;
        this.ghosts=ghosts;
        this.frameChanged=false;
        this.ended=false;
    }

    public void cancel(){
        this.countDownTimer.cancel();
    }

    public boolean hasEnded(){
        return this.ended;
    }

    private void changeFpm(){
        for (int i=0;i<ghosts.length;i++){
            if(ghosts[i].getState().isFrightened()){
                ghosts[i].setFpm(4);
            }
        }
    }

    private void scareGhosts(){
        int[] ghostPosition;
        int value;
        Log.i("Scare timer","Start Scaring");
        for (int i=0;i<ghosts.length;i++){
            ghostPosition=new int[]{ghosts[i].getPositionMapY(),ghosts[i].getPositionMapX()};
            value=map[ghostPosition[0]][ghostPosition[1]];
            if(value!=10 && value!=99 && !ghosts[i].getState().isRespawning()){
                ghosts[i].setFpm(2);
                ghosts[i].setFrightenedBehaviour();
            }
        }
    }

    public void run(){
        this.scareGhosts();
        Looper.prepare();
        this.countDownTimer=new CountDownTimer(TIME,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(!frameChanged && millisUntilFinished<=3000){
                    frameChanged=true;
                    changeFpm();
                }
            }

            @Override
            public void onFinish() {
                Log.i("Scare timer","End Scaring");
                ended=true;
                for(int i=0;i<ghosts.length;i++){
                    if(ghosts[i].getState().isFrightened()){
                        ghosts[i].reestablishBehavior();
                    }
                }
            }
        };
        this.countDownTimer.start();
        Looper.loop();
    }
}
