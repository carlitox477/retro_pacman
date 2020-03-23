package com.example.pacman;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class ChaseAgressive implements ChaseBehaviour {



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] chase(GameView gv, int srcX, int srcY, int currentDirection) {

        int direction = currentDirection;
        float nextX;
        float nextY;

        int[] nextPosition = new int[3];



        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {

            AStar aStar = new AStar(gv, srcX / gv.getBlockSize(),srcY/ gv.getBlockSize());
            List<Node> path = aStar.findPathTo(gv.getxPosPacman() / gv.getBlockSize(),gv.getyPosPacman() / gv.getBlockSize());
            direction = getDirection(path);

        }



        if (direction == 0) {
            srcY += - gv.getBlockSize() / 15;
        } else if (direction == 1) {
            srcX += gv.getBlockSize() / 15;
        } else if (direction == 2) {
            srcY += gv.getBlockSize() / 15;
        } else if (direction == 3) {
            srcX += -gv.getBlockSize() / 15;
        }

        nextPosition[0] = srcX;
        nextPosition[1] = srcY;
        nextPosition[2] = direction;

        return nextPosition;

    }

    private int getDirection(List<Node> path) {

        int direction = 4;

        Node nextNode = path.get(1);
        Node currentNode =  path.get(0);
        int nextX = nextNode.x;
        int nextY = nextNode.y;
        int currentX = currentNode.x;
        int currentY = currentNode.y;

        if(currentX - nextX == -1 && currentY - nextY == 0)
            direction = 1;
        else if(currentX - nextX == 0 && currentY - nextY == -1)
            direction = 2;
        else if(currentX - nextX == 1 && currentY - nextY == 0)
            direction = 3;
        else if(currentX - nextX == 0 && currentY - nextY == 1)
            direction = 0;

        Log.i("info", "getDirection: " + direction);
        return  direction;


    }
}


