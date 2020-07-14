package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import Behaviour.FrightenedBehaviour;
import Behaviour.RespawningBehaviour;
import Behaviour_Chase.ChaseAgressive;
import Behaviour_Chase.ChaseAmbush;
import Behaviour_Chase.ChaseBehaviour;
import Behaviour_Chase.ChasePatrol;
import Behaviour_Chase.ChaseRandom;
import Behaviour_Scatter.ScatterBehaviour;
import Behaviour_Scatter.ScatterBottomLeftCorner;
import Behaviour_Scatter.ScatterBottomRightCorner;
import Behaviour_Scatter.ScatterTopLeftCorner;
import Behaviour_Scatter.ScatterTopRightCorner;

public class Ghost {
    private String name;
    private Bitmap ghostBitmap,vulnerableGhostBitmap,respawningGhostBitmap;
    private int[] spawnPosition, currentPosition;
    private int spriteSize;

    private GameView gv;


    //state determine how is going to behave
    // 0 chasing
    // 1 scattering
    // 2 frightened
    private int state;

    private ChaseBehaviour chaseBehaviour;
    private FrightenedBehaviour frightenedBehaviour;
    private ScatterBehaviour scatterBehaviour;
    private RespawningBehaviour respawningBehaviour;


    private int xPos = 0;
    private int yPos = 0;


    private int ghostDirection = 4;

    public Ghost(GameView gv, String name, int spriteSize,int[]spawnPosition) {

        this.name = name;
        this.gv = gv;
        this.currentPosition=new int[2];
        this.spawnPosition=spawnPosition;
        this.spriteSize = spriteSize;

        this.spawnPosition[0]=this.spawnPosition[0]*spriteSize;
        this.spawnPosition[1]=this.spawnPosition[1]*spriteSize;
        this.currentPosition[0]=this.spawnPosition[0];
        this.currentPosition[1]=this.spawnPosition[1];

        switch (name) {
            case "Blinky":
                this.chaseBehaviour = new ChaseAgressive();
                this.scatterBehaviour = new ScatterTopRightCorner();
                this.frightenedBehaviour = new FrightenedBehaviour();
                this.respawningBehaviour = new RespawningBehaviour();
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        gv.getContext().getResources(), R.drawable.red_ghost), spriteSize, spriteSize, false);

                break;
            case "Pinky":
                this.chaseBehaviour = new ChaseAmbush();
                this.scatterBehaviour = new ScatterTopLeftCorner();
                this.frightenedBehaviour = new FrightenedBehaviour();
                this.respawningBehaviour = new RespawningBehaviour();
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.pink_ghost), spriteSize, spriteSize, false);
                break;
            case "Inky":
                this.chaseBehaviour = new ChasePatrol();
                this.scatterBehaviour = new ScatterBottomLeftCorner();
                this.frightenedBehaviour = new FrightenedBehaviour();
                this.respawningBehaviour = new RespawningBehaviour();
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.blue_ghost), spriteSize, spriteSize, false);
                break;
            case "Clyde":
                this.chaseBehaviour = new ChaseRandom();
                this.scatterBehaviour = new ScatterBottomRightCorner();
                this.frightenedBehaviour = new FrightenedBehaviour();
                this.respawningBehaviour = new RespawningBehaviour();
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.yellow_ghost), spriteSize, spriteSize, false);
                break;
            default:
                break;
        }
        vulnerableGhostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.vulnerable_ghost), spriteSize, spriteSize, false);
        respawningGhostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.eyes_ghost), spriteSize, spriteSize, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void move() {

        int[] nextPosition = new int[2];
        switch (state) {
            case 0: //Chase behavior
                nextPosition = chaseBehaviour.chase(gv, this.currentPosition[1], this.currentPosition[0], ghostDirection);
                break;
            case 1: // Scattering behaviour
                nextPosition = scatterBehaviour.scatter(gv, this.currentPosition[1], this.currentPosition[0], ghostDirection);
                break;
            case 2: // Frightened behaviour
                nextPosition = frightenedBehaviour.escape(gv, this.currentPosition[1], this.currentPosition[0], ghostDirection);
                break;
            case 3: // Respawning behaviour
                if (xPos == 9 * gv.getBlockSize() && yPos == 9 * gv.getBlockSize()) {
                    state = 0;
                    nextPosition = chaseBehaviour.chase(gv,this.currentPosition[1] , yPos, ghostDirection);
                    Log.i("info", "at home");
                } else
                    nextPosition = respawningBehaviour.respawn(gv, xPos, yPos, ghostDirection);
            default:
                break;
        }
        this.xPos = nextPosition[0];
        this.yPos = nextPosition[1];
        this.ghostDirection = nextPosition[2];
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = ghostBitmap;
        if (state == 2)
            bitmap = vulnerableGhostBitmap;
        else if (state == 3)
            bitmap = respawningGhostBitmap;
        return bitmap;
    }

    public void setChaseBehaviour() {
        state = 0;
    }

    public void setScatterBehaviour() {
        state = 1;
    }

    public void setFrightenedBehaviour() {
        if (state != 3) {
            frightenedBehaviour = new FrightenedBehaviour();
            state = 2;
        }


    }

    public void setRespawnBehaviour() {
        state = 3;
    }

    public int getState() {
        return state;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }


}
