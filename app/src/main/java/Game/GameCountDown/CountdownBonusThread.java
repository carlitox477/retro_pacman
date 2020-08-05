package Game.GameCountDown;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

import Game.GameMap;

public class CountdownBonusThread extends Thread {
    private int time;
    private GameMap gm;


    public CountdownBonusThread(GameMap gm, int time) {
        this.gm = gm;
        this.time = time;
    }

    public void run() {
        Looper.prepare();
        new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                gm.setBonusAvailable();
            }
        }.start();
        Looper.loop();
    }
}
