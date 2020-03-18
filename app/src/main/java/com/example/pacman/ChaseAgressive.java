package com.example.pacman;

import android.util.Log;

import java.util.LinkedList;
import java.util.Stack;

public class ChaseAgressive implements ChaseBehaviour {


    @Override
    public int[] chase(DrawingView dv, int currentGhostDirection, int currentX, int currentY) {

        int xDistance = dv.getxPosPacman() - currentX;
        int yDistance = dv.getyPosPacman() - currentY;

        short ch;

        int xPosGhost = currentX;
        int yPosGhost = currentY;
        int ghostDirection = currentGhostDirection;




        if ((xPosGhost % dv.getBlockSize() == 0) && (yPosGhost % dv.getBlockSize() == 0)) {


            if (xPosGhost >= dv.getBlockSize() * 17) {
                xPosGhost = 0;
            }
            if (xPosGhost < 0) {
                xPosGhost = dv.getBlockSize() * 16;
            }
            ch = dv.getLevelData()[yPosGhost / dv.getBlockSize()][xPosGhost / dv.getBlockSize()];


            //1 izquierda
            //2 arriba
            //4 derecha
            //8 abajo


            if (xDistance >= 0 && yDistance >= 0) { // Move right and down
                if ((ch & 4) == 0 && (ch & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {

                        ghostDirection = 1;
                    } else {
                        ghostDirection = 2;
                    }
                }
                else if ((ch & 4) == 0) {

                    ghostDirection = 1;
                }
                else if ((ch & 8) == 0) {
                    ghostDirection = 2;
                }
                else
                    ghostDirection = 3;
            }
            if (xDistance >= 0 && yDistance <= 0) { // Move right and up
                if ((ch & 4) == 0 && (ch & 2) == 0 ) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 1;
                    } else {
                        ghostDirection = 0;
                    }
                }
                else if ((ch & 4) == 0) {
                    ghostDirection = 1;
                }
                else if ((ch & 2) == 0) {
                    ghostDirection = 0;
                }
                else ghostDirection = 2;
            }
            if (xDistance <= 0 && yDistance >= 0) { // Move left and down
                if ((ch & 1) == 0 && (ch & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 3;
                    } else {
                        ghostDirection = 2;
                    }
                }
                else if ((ch & 1) == 0) {
                    ghostDirection = 3;
                }
                else if ((ch & 8) == 0) {
                    ghostDirection = 2;
                }
                else ghostDirection = 1;
            }
            if (xDistance <= 0 && yDistance <= 0) { // Move left and up
                if ((ch & 1) == 0 && (ch & 2) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 3;
                    } else {
                        ghostDirection = 0;
                    }
                }
                else if ((ch & 1) == 0) {
                    ghostDirection = 3;
                }
                else if ((ch & 2) == 0) {
                    ghostDirection = 0;
                }
                else ghostDirection = 2;
            }



            // Handles wall collisions
            if ((ghostDirection == 3 && (ch & 1) != 0) ||
                    (ghostDirection == 1 && (ch & 4) != 0) ||
                    (ghostDirection == 0 && (ch & 2) != 0) ||
                    (ghostDirection == 2 && (ch & 8) != 0)) {
                ghostDirection = 4;
            }

            //Handles backtrackings
            if(currentGhostDirection == 0 && ghostDirection == 2){
                if((ch & 2) == 0)
                    ghostDirection = 0;
                else{
                    if((ch & 1) == 0)
                        ghostDirection = 3;
                    else if((ch & 4) == 0)
                        ghostDirection = 1;
                }
            }
            else if(currentGhostDirection == 2  && ghostDirection == 0){
                if((ch & 8) == 0){
                    ghostDirection = 2;
                }else{
                    if((ch & 1) == 0)
                        ghostDirection = 3;
                    else if((ch & 4) == 0)
                        ghostDirection = 1;
                }
            }
            else if(currentGhostDirection == 1 && ghostDirection == 3){
                if((ch & 4) == 0){
                    ghostDirection = 1;
                }else{
                    if((ch & 2) == 0)
                        ghostDirection = 0;
                    else if((ch & 8) == 0)
                        ghostDirection = 2;
                }
            }
            else if(currentGhostDirection == 3 && (ch & 1) == 0 && ghostDirection == 1){
                if((ch & 4) == 0){
                    ghostDirection = 3;
                }else{
                    if((ch & 2) == 0)
                        ghostDirection = 0;
                    else if((ch & 8) == 0)
                        ghostDirection = 2;
                }
            }

        }




        if (ghostDirection == 0) {
            yPosGhost += -dv.getBlockSize() / 20;
        } else if (ghostDirection == 1) {
            xPosGhost += dv.getBlockSize() / 20;
        } else if (ghostDirection == 2) {
            yPosGhost += dv.getBlockSize() / 20;
        } else if (ghostDirection == 3) {
            xPosGhost += -dv.getBlockSize() / 20;
        }

        int[] nextPos = {xPosGhost, yPosGhost, ghostDirection};

        return nextPos;
    }

}


