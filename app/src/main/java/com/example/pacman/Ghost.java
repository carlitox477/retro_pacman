package com.example.pacman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class Ghost {

    private String name;


    private Bitmap ghostBitmap;
    private Bitmap vulnerableGhostBitmap;
    private Bitmap respawningGhostBitmap;
    int spriteSize;

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

    private int spawnX;
    private int spawnY;

    private int ghostDirection = 4;

    public Ghost(GameView gv, String name) {

        this.name = name;
        this.gv = gv;
        spriteSize = gv.getScreenWidth() / 18;
        spriteSize = (spriteSize / 9) * 9;

        switch (name) {
            case "Blinky":
                this.chaseBehaviour = new ChaseAgressive();
                this.scatterBehaviour = new ScatterTopRightCorner();
                this.frightenedBehaviour = new FrightenedBehaviour();
                this.respawningBehaviour = new RespawningBehaviour();
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        gv.getContext().getResources(), R.drawable.red_ghost), spriteSize, spriteSize, false);
                spawnX = xPos = 9 * gv.getBlockSize();
                spawnY = yPos =  8 * gv.getBlockSize();
                break;
            case "Pinky":
                this.chaseBehaviour = new ChaseAmbush();
                this.scatterBehaviour = new ScatterTopRightCorner();
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.pink_ghost), spriteSize, spriteSize, false);
                spawnX = 7 * gv.getBlockSize();
                spawnY = 9 * gv.getBlockSize();
                break;
            case "Inky":
                this.chaseBehaviour = new ChasePatrol();
                this.scatterBehaviour = new ScatterBottomLeftCorner();
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.blue_ghost), spriteSize, spriteSize, false);
                spawnX = xPos = 8 * gv.getBlockSize();
                spawnY = yPos =  9 * gv.getBlockSize();
                break;
            case "Clyde":
                this.chaseBehaviour = new ChasePatrol();
                this.scatterBehaviour = new ScatterBottomRightCorner();
                this.frightenedBehaviour = new FrightenedBehaviour();
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.yellow_ghost), spriteSize, spriteSize, false);
                spawnX = xPos = 9 * gv.getBlockSize();
                spawnY = yPos = 9 * gv.getBlockSize();
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
                nextPosition = chaseBehaviour.chase(gv, xPos, yPos, ghostDirection);
                break;
            case 1: // Scattering behaviour
                nextPosition = scatterBehaviour.scatter(gv, xPos, yPos, ghostDirection);
                break;
            case 2: // Frightened behaviour
                nextPosition = frightenedBehaviour.escape(gv, xPos, yPos, ghostDirection);
                break;
            case 3: // Respawning behaviour
                if(xPos == 9 * gv.getBlockSize() && yPos == 9 * gv.getBlockSize()){
                    state = 0;
                    nextPosition = chaseBehaviour.chase(gv, yPos, yPos, ghostDirection);
                    Log.i("info", "at home");
                }
                else
                    nextPosition= respawningBehaviour.respawn(gv,xPos,yPos,ghostDirection);
            default:break;
        }
        this.xPos = nextPosition[0];
        this.yPos = nextPosition[1];
        this.ghostDirection = nextPosition[2];
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = ghostBitmap;
        if(state == 2)
            bitmap = vulnerableGhostBitmap;
        else if(state ==3)
            bitmap = respawningGhostBitmap;
        return bitmap;
    }

    public void setChaseBehaviour(){
        state = 0;
    }
    public void setScatterBehaviour(){
        state = 1;
    }
    public void setFrightenedBehaviour(){
        if(state != 3){
            frightenedBehaviour = new FrightenedBehaviour();
            state = 2;
        }


    }
    public void setRespawnBehaviour(){
        state = 3;
    }
    public int getState(){
        return state;
    }

    public int getGhostDirection() {
        return ghostDirection;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }


}
