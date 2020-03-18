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

public class ScoreActivity extends ActivityWithBackGroundMusic {
    private ListView lvScores;
    private ScoreAdapter scoreAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

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

}
