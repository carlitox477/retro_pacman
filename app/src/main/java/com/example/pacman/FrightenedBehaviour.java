package com.example.pacman;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class FrightenedBehaviour {


    private int escapePointX = 0;
    private int escapePointY = 0;
    private List<Node> path;

    private int step = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)

    public int[] escape(GameView gv, int srcX, int srcY, int currentDirection) {


        int direction = currentDirection;
        float nextX;
        float nextY;

        int[] nextPosition = new int[3];




        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {

            if(escapePointX == 0 && escapePointY == 0 || (escapePointX * gv.getBlockSize() == srcX && escapePointY * gv.getBlockSize() == srcY  )){
                int[] randomEscapePoint = gv.generateMapSpawn();
                escapePointY =  randomEscapePoint[0];
                escapePointX = randomEscapePoint[1];
                AStar astar = new AStar(gv,srcX / gv.getBlockSize(),srcY / gv.getBlockSize());
                path = astar.findPathTo(escapePointX,escapePointY);
            }

            direction = getDirection(path);
        }

        if (direction == 0) {
            srcY += -gv.getBlockSize() / 18;
        } else if (direction == 1) {
            srcX += gv.getBlockSize() / 18;
        } else if (direction == 2) {
            srcY += gv.getBlockSize() / 18;
        } else if (direction == 3) {
            srcX += -gv.getBlockSize() / 18;
        }

        nextPosition[0] = srcX;
        nextPosition[1] = srcY;
        nextPosition[2] = direction;

        return nextPosition;
    }

    private int getDirection(List<Node> path) {

        int direction = 4;

        int nextX;
        int nextY;
        int currentX;
        int currentY;

        Node nextNode;
        Node currentNode;

        if(path.size() > 1){
            nextNode =  path.get(1);
            currentNode = path.get(0);

            nextX = nextNode.x;
            nextY = nextNode.y;
            currentX = currentNode.x;
            currentY = currentNode.y;

            if (currentX - nextX == -1 && currentY - nextY == 0)
                direction = 1;
            else if (currentX - nextX == 0 && currentY - nextY == -1)
                direction = 2;
            else if (currentX - nextX == 1 && currentY - nextY == 0)
                direction = 3;
            else if (currentX - nextX == 0 && currentY - nextY == 1)
                direction = 0;

            path.remove(0);
        }


        return direction;


    }
}