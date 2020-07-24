package com.example.pacman;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import Behaviour.*;
import Behaviour_Chase.*;


public class Ghost {
    private static Bitmap vulnerableGhostBitmap,respawningGhostBitmap;
    private Bitmap[][] ghostBitmaps;
    private String name;
    private Behaviour currentBehaviour;
    private Bitmap ghostBitmap, currentBitmap;
    private int[] screenSpawnPosition, screenCurrentPosition;
    private GameView gv;
    private int bitmapId;


    private ChaseBehaviour chaseBehaviour; //set bitmap
    private FrightenedBehaviour frightenedBehaviour;
    private ScatterBehaviour scatterBehaviour;
    private RespawningBehaviour respawningBehaviour;
    private int ghostDirection = 4;

    public static void loadCommonBitmaps(Resources resources,int blockSize){
        vulnerableGhostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.vulnerable_ghost), blockSize, blockSize, false);
        respawningGhostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.eyes_ghost), blockSize, blockSize, false);
    }

    public Ghost(GameView gv, String name, int spriteSize,int[]mapSpawnPosition,int[]scatterTarget, ChaseBehaviour chaseBehaviour) {
        this.name = name;
        this.bitmapId=0;
        this.gv = gv;
        this.screenSpawnPosition=new int[]{mapSpawnPosition[0]*spriteSize,mapSpawnPosition[1]*spriteSize};
        this.screenCurrentPosition=new int[]{this.screenSpawnPosition[0],this.screenSpawnPosition[1]};

        this.frightenedBehaviour = new FrightenedBehaviour();
        this.respawningBehaviour = new RespawningBehaviour();
        this.scatterBehaviour=new ScatterBehaviour(scatterTarget);
        this.chaseBehaviour=chaseBehaviour;

        this.screenCurrentPosition[0]=this.screenSpawnPosition[0]; //X screen position
        this.screenCurrentPosition[1]=this.screenSpawnPosition[1]; //Y screen position

        this.ghostBitmaps=new Bitmap[4][2];

        //Pensar como hacerlo solo con el nombre
        //ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.yellow_ghost), spriteSize, spriteSize, false);


        switch (name) {
            case "Blinky":
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        gv.getContext().getResources(), R.drawable.red_ghost), spriteSize, spriteSize, false);

                break;
            case "Pinky":
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.pink_ghost), spriteSize, spriteSize, false);
                break;
            case "Inky":
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.blue_ghost), spriteSize, spriteSize, false);
                break;
            case "Clyde":
                //Añadir bitmap de fantasma
                ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gv.getContext().getResources(), R.drawable.yellow_ghost), spriteSize, spriteSize, false);
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void move() {
        int[] nextPosition; //= new int[3]; //{posX,posY,direction}
        nextPosition=this.currentBehaviour.behave(gv,this.screenCurrentPosition[0], this.screenCurrentPosition[1], ghostDirection);
        this.screenCurrentPosition[0] = nextPosition[0];
        this.screenCurrentPosition[1]= nextPosition[1];
        this.ghostDirection = nextPosition[2];
        this.bitmapId=(this.bitmapId+1)%2;
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = ghostBitmap;
        if (this.currentBehaviour.isFrightened())
            bitmap = vulnerableGhostBitmap;
        else if (this.currentBehaviour.isRespawning())
            bitmap = respawningGhostBitmap;
        return bitmap;
    }

    public void setChaseBehaviour() {
        //Change current bitmap
        this.currentBehaviour=this.chaseBehaviour;
    }

    public void setScatterBehaviour() {
        //Change current bitmap
        this.currentBehaviour=this.scatterBehaviour;
    }

    public void setFrightenedBehaviour() {
        //Change current bitmap
        if (!this.currentBehaviour.isRespawning()) {
            this.currentBehaviour=this.frightenedBehaviour;
        }
    }

    public void setRespawnBehaviour() {
        //Change current bitmap
        this.currentBehaviour=this.respawningBehaviour;
    }

    public Behaviour getState() {
        return this.currentBehaviour;
    }

    public int getxPos() {
        return this.screenCurrentPosition[0];
    }

    public int getyPos() {
        return this.screenCurrentPosition[1];
    }

    private void loadBitmaps(String name,int spriteSize){
        int idBm;
        String packageName = this.gv.getContext().getPackageName();
        Resources res;
        String[] position;

        packageName = this.gv.getContext().getPackageName();
        res = this.gv.getResources();

        position=new String[]{"up","down","left","right"};

        for (int i=0;i<4;i++){
            for (int j=0;j<2;j++){
                idBm=res.getIdentifier("ghost_"+name + "_"+position[i] + j, "drawable", packageName);
                this.ghostBitmaps[i][j]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        gv.getContext().getResources(), idBm), spriteSize, spriteSize, false);
            }
        }
    }


}
