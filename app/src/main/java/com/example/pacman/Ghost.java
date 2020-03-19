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



    private boolean insideBase = true;
    private boolean frightened = false;
    private boolean scattering = false;
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

        int spriteSize = dv.getScreenWidth() / 17;
        spriteSize = (spriteSize / 5) * 5;

        switch (name){
            case "Blinky":
                scattering = true;
                this.chaseBehaviour = new ChaseAgressive();
                this.scatterBehaviour = new ScatterTopLeftCorner();
                //Añadir bitmap de fantasma
                bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        dv.getContext().getResources(), R.drawable.red_ghost), spriteSize, spriteSize, false);
            xPos = 9 * dv.getBlockSize();
            yPos = 8 * dv.getBlockSize();
            break;
            case "Pinky":
                scattering = true;
                this.chaseBehaviour = new ChaseAmbush();
                this.scatterBehaviour = new ScatterTopRightCorner();
                //Añadir bitmap de fantasma
                bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        dv.getContext().getResources(), R.drawable.pink_ghost), spriteSize, spriteSize, false);
                xPos = 8 * dv.getBlockSize();
                yPos = 8 * dv.getBlockSize();
            break;
            case "Inky":
                scattering = true;
                this.chaseBehaviour = new ChasePatrol();
                this.scatterBehaviour = new ScatterBottomLeftCorner();
                //Añadir bitmap de fantasma
                bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        dv.getContext().getResources(), R.drawable.blue_ghost), spriteSize, spriteSize, false);
                xPos = 7 * dv.getBlockSize();
                yPos = 8 * dv.getBlockSize();
                break;
            case "Clyde":
                scattering = true;
                this.chaseBehaviour = new ChasePatrol();
                this.scatterBehaviour = new ScatterBottomRightCorner();
                //Añadir bitmap de fantasma
                bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        dv.getContext().getResources(), R.drawable.yellow_ghost), spriteSize, spriteSize, false);
                xPos = 7 * dv.getBlockSize();
                yPos = 8 * dv.getBlockSize();
                break;
            default:break;
        }

    }

    public void move() {
        int[] nextPos;

        if(insideBase){
            nextPos = scatterBehaviour.moveOutOfBase(dv,ghostDirection,xPos,yPos);
            if((dv.getLevelData()[yPos/dv.getBlockSize()][xPos / dv.getBlockSize()] & 1024) != 0){
                insideBase = false;
            }
        }else{
            if(frightened){
                nextPos = frightenedBehaviour.runaway(dv,ghostDirection, xPos, yPos);
            }else if(scattering){
                nextPos = scatterBehaviour.scatter(dv,ghostDirection, xPos, yPos);
            }else{
                nextPos = chaseBehaviour.chase(dv,ghostDirection, xPos, yPos);
            }
        }

        xPos = nextPos[0];
        yPos = nextPos[1];
        ghostDirection = nextPos[2];
    }

    public Bitmap getBitmap() {
        return bitmap;
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
    public void setInsideBase(boolean insideBase) {
        this.insideBase = insideBase;
    }

    public void setFrightened(boolean frightened) {
        this.frightened = frightened;
    }

    public void setScattering(boolean scattering) {
        this.scattering = scattering;
    }
    public void resetState(){
        frightened = false;
        scattering = false;
    }

}
