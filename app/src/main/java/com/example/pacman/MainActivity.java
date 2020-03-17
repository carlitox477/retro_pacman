package com.example.pacman;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean mIsBound = false;
    private BackgroundMusicService mServ;
    private ServiceConnection serCon;
    private HomeWatcher mHomeWatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setBackgroundMusic();
    }

    public void onClickCreators(View view){
        //View creators info
        Intent intent = new Intent(this, CreatorsActivity.class);
        startActivity(intent);
    }

    public void onClickScores(View view){
        //View scores
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    public void onClickPlay(View view){
        //To start to play
        PreviousPlayFragment fragmentToPlay=new PreviousPlayFragment();
        fragmentToPlay.show(getSupportFragmentManager(),"ToPlay");
    }

    public void doBindService(){
        bindService(new Intent(this,BackgroundMusicService.class),
                serCon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    public void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(serCon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,BackgroundMusicService.class);
        stopService(music);
        this.mHomeWatcher.stopWatch();

    }

    private void setBackgroundMusic(){
        //For music
        this.serCon=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                mServ = ((BackgroundMusicService.ServiceBinder)binder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServ=null;
            }
        };

        doBindService();
        Intent music = new Intent();
        music.setClass(this, BackgroundMusicService.class);
        startService(music);

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (this != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        this.mHomeWatcher.startWatch();
    }
}
