package Game.GameCountDown;

import android.os.CountDownTimer;
import android.util.Log;

import Game.Character_package.Ghost;

public class CountDownScareGhosts implements Runnable{
    private static int TIME=6000; //miliseconds
    private Ghost[] ghosts;
    private boolean frameChanged, cancel, levelEnded;

    public CountDownScareGhosts(Ghost[]ghosts,boolean levelEnded){
        this.ghosts=ghosts;
        this.cancel=false;
        this.frameChanged=false;
        this.levelEnded=levelEnded;
    }

    @Override
    public void run() {
        //try to scare ghosts

        new CountDownTimer(TIME, 1000) {
            public void onTick(long millisUntilFinished) {
                if(cancel){
                    this.cancel();
                    if(levelEnded){
                        Log.i("Scare timer","Level ended");
                    }else{
                        Log.i("Scare timer","New Super Pallet've been eaten");
                        new Thread(new CountDownScareGhosts(ghosts,false)).start();
                    }
                }else if(millisUntilFinished>=TIME/2 && frameChanged){
                    //Change frame of ghosts
                }
            }

            public void onFinish() {
                Log.i("Scare timer","End Scaring");

            }
        }.start();
    }
}
