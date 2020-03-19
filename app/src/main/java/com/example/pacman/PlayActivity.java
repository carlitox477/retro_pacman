package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {
    private DrawingView drawingView;
    private String playerNickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //We get player nickname sent previously by the fragment
        this.playerNickname=getIntent().getExtras().getString("playerNickname");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        drawingView = new DrawingView(this);
        setContentView(drawingView);

    }
    protected void onResume(){
        super.onResume();
        drawingView.resume();
    }
    protected void onPause(){
        super.onPause();
        drawingView.pause();
    }



}
