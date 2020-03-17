package com.example.pacman;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.appcompat.app.AppCompatActivity;

public class CreatorsActivity extends ActivityWithBackGroundMusic {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creators);
    }
}
