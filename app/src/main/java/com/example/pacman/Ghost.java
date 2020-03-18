package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Ghost{

    private String name;



    private Bitmap bitmap;

    private DrawingView dv;

    private ChaseBehaviour chaseBehaviour;
    private FrightenedBehaviour frightenedBehaviour;
    private ScatterBehaviour scatterBehaviour;



    private int xPos = 0;
    private int yPos = 0;



    private int ghostDirection = 0;

    public Ghost(DrawingView dv, String name){

        this.name = name;
        this.dv = dv;
        this.chaseBehaviour = new ChaseAgressive();

        int spriteSize = dv.getScreenWidth() / 17;        //fantasmas
        spriteSize = (spriteSize / 5) * 5;
        //Añadir bitmap de fantasma
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                dv.getContext().getResources(), R.drawable.red_ghost), spriteSize, spriteSize, false);

    }

    public void move() {
        int[] nextPos = chaseBehaviour.chase(dv,ghostDirection, xPos, yPos);
        xPos = nextPos[0];
        yPos = nextPos[1];
        ghostDirection = nextPos[2];
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public int getxPos() {
        return xPos;
    }
    public int getyPos() {
        return yPos;
    }



}
