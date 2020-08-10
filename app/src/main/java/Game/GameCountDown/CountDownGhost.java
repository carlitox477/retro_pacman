package Game.GameCountDown;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Semaphore;

import Game.Character_package.Ghost;

public class CountDownGhost extends Thread {
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
    private CountDownTimer countDownTimer;
    private int stage,time,level;
    private char state;
    private Ghost ghost;
    private boolean started;

    public CountDownGhost(long millisInFuture,Ghost ghost,int level,int stage,char state) {
        this.ghost=ghost;
        this.level=level;
        this.stage=stage;
        this.state=state;
        this.started=false;
        this.time=(int)millisInFuture;
    }

    public CountDownGhost(Ghost ghost,int level,int stage, char state) {
        this.ghost=ghost;
        this.level=level;
        this.stage=stage;
        this.state=state;
        this.time=getTime(state,level,stage);
    }

    public void cancel(){
        try {
            if(this.countDownTimer!=null){
                this.countDownTimer.cancel();
            }
        }catch (Exception e){}
    }

    public void pause(){
        //The new countDown will be used by the ghost to reestablish the behavior
        this.ghost.setCountdownGhost(new CountDownGhost(this.time,this.ghost,this.level,this.stage,this.state),false);
        this.cancel();
    }

    public void run(){
        this.started=true;
        this.ghost.setFpm(2);
        this.changeState();
        if(this.state!=' '){
            Looper.prepare();
            this.countDownTimer=new CountDownTimer(this.time,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    time=(int)millisUntilFinished;
                    //
                }

                @Override
                public void onFinish() {
                    if(stage<=3){
                        if(state=='s'){
                            stage++;
                        }
                        switch (state){
                            case 's':
                                state='c';
                                break;
                            case 'c':
                                state='s';
                                break;
                            default:
                                break;
                        }
                        try {
                            ghost.setCountdownGhost(new CountDownGhost(ghost,level,stage,state),true);
                        }catch (Exception e){}

                    }
                }
            };
            this.countDownTimer.start();
            Looper.loop();
        }else {

        }

    }


    private void changeState(){
        this.ghost.setFpm(2);
        switch (this.state){
            case 'c':
                this.ghost.setChaseBehaviour();
                break;
            case 's':
                this.ghost.setScatterBehaviour();
                break;
            default:
                this.state=' ';
                break;
        }
    }

    private static int getTime(char state, int level, int stage){
        int[][] times;
        int[] times2;
        if(state=='c'){
            times=CHASE_TIMES;
        }else{
            times=SCATTER_TIMES;
        }
        if(level==1){
            times2=times[0];
        }else if(level>2&& level<=5){
            times2=times[1];
        }else {
            times2=times[2];
        }
        return times2[stage-1];
    }


}
