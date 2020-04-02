package com.example.pacman;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class ChaseRandom implements ChaseBehaviour {

    private int[] cornerDirections = {2, 2, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 3, 0, 0, 3, 3, 3};
    private boolean inCorner = false;
    private boolean chasing = false;
    private int step = 0;

    private List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)


    public int[] chase(GameView gv, int srcX, int srcY, int currentDirection) {


        int blockSize = gv.getBlockSize();
        int direction = currentDirection;
        float nextX;
        float nextY;

        int[] nextPosition = new int[3];


        if ((srcX % blockSize == 0) && (srcY % blockSize == 0)) {

            int xPacmanPos = gv.getxPosPacman();
            int yPacmanPos = gv.getyPosPacman();

            int xDist = Math.abs(srcX - xPacmanPos);
            int yDist = Math.abs(srcY - yPacmanPos);
            double dist = Math.hypot(xDist, yDist);
            if (dist <= 8 * blockSize) {
                chasing = true;
            } else {
                chasing = false;
            }


            AStar aStar;
            if (chasing) {
                inCorner = false;
                step = 0;
                aStar = new AStar(gv,srcX /blockSize,srcY /blockSize);
                path = aStar.findPathTo(xPacmanPos / blockSize, yPacmanPos / blockSize);
                direction = getDirection(path);
            }
            else{
                if(srcX  / blockSize  == 4 && srcY  / blockSize == 15 ){
                    inCorner = true;
                }
                if(!inCorner) {
                    aStar = new AStar(gv, srcX / blockSize, srcY / blockSize);
                    path = aStar.findPathTo(4, 15);
                    direction = getDirection(path);
                }else{
                    direction = cornerDirections[step];
                    step++;
                    if(step == cornerDirections.length - 1)
                        step = 0;
                }
            }



        }

        if (direction == 0) {
            srcY += -blockSize / 15;
        } else if (direction == 1) {
            srcX += blockSize / 15;
        } else if (direction == 2) {
            srcY += blockSize / 15;
        } else if (direction == 3) {
            srcX += -blockSize / 15;
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

        if (path != null) {
            if (path.size() > 1) {
                nextNode = path.get(1);
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

        }


        return direction;


    }
}