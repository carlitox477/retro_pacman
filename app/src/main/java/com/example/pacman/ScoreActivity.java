package com.example.pacman;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    private ListView lvScores;
    private ScoreAdapter scoreAdapter;
    private boolean mIsBound = false;
    private BackgroundMusicService mServ;
    private ServiceConnection serCon;
    private HomeWatcher mHomeWatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        this.setBackgroundMusic();

        lvScores= (ListView) this.findViewById(R.id.lv_scores);
        scoreAdapter=new ScoreAdapter(this,getTestScore());
        lvScores.setAdapter(scoreAdapter);
    }

    public void onClickSearch(View view){
        SearchForNick fragmentToSearch=new SearchForNick();
        fragmentToSearch.show(getSupportFragmentManager(),"ToSearch");
    }

    private ArrayList<Score> getTestScore(){
        ArrayList<Score> test=new ArrayList<Score>();
        test.add(new Score("CARLOS", 10000.0));
        test.add(new Score("PEDRO", 9100.0));
        test.add(new Score("JOSE", 8100.0));
        test.add(new Score("MIA", 7100.0));
        test.add(new Score("MAIA", 7000.0));
        test.add(new Score("LUJAN", 6500.0));
        test.add(new Score("MARIAN", 6200.0));
        test.add(new Score("JUAN", 5600.0));
        test.add(new Score("DIEGO", 5200.0));
        test.add(new Score("JESUS", 5000.0));
        test.add(new Score("YISUS", 4900.0));
        test.add(new Score("BETO", 4800.0));
        test.add(new Score("BETO2", 4500.0));
        test.add(new Score("LOCO24", 4200.0));
        test.add(new Score("PETAKA", 3900.0));
        test.add(new Score("PAJARO", 3500.0));
        test.add(new Score("BUITRE", 3300.0));
        test.add(new Score("KUKA", 3000.0));
        return test;
    }

    public void doBindService(){
        bindService(new Intent(this,BackgroundMusicService.class),
                serCon, Context.BIND_AUTO_CREATE);
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
