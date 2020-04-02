package com.example.pacman;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class ChasePatrol implements ChaseBehaviour {


    List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] chase(GameView gv, int srcX, int srcY, int currentDirection) {

        int direction = currentDirection;

        int[] nextPosition = new int[3];

        int destX = 0;
        int destY = 0;


        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {

            int pacmanDirection = gv.getPacmanDirection();
            int blockSize = gv.getBlockSize();
            int xPosPacman = gv.getxPosPacman();
            int yPosPacman = gv.getyPosPacman();
            int[][] levelLayout = gv.getLevellayout();


            if (pacmanDirection == 0) {
                destX = xPosPacman + blockSize / 2;
                destY = yPosPacman - 2 * blockSize - blockSize / 2;
            } else if (pacmanDirection == 1) {
                destX = xPosPacman + 3 * blockSize + blockSize / 2;
                destY = yPosPacman + blockSize / 2;
            } else if (pacmanDirection == 2) {
                destX = xPosPacman + blockSize / 2;
                destY = yPosPacman + 3 * blockSize + blockSize / 2;
            } else if (pacmanDirection == 3) {
                destX = xPosPacman - 2 * blockSize - blockSize / 2;
                destY = yPosPacman + blockSize / 2;
            } else if (pacmanDirection == 4) {
                destX = xPosPacman;
                destY = yPosPacman;
            }

            int blinkyX = gv.getGhost(1).getxPos();
            int blinkyY = gv.getGhost(1).getyPos();

            int vectorX = (destX  - blinkyX) * 2;
            int vectorY = (destY - blinkyY) * 2;

            destX += vectorX;
            destY += vectorY;

            AStar aStar = new AStar(gv,srcX / blockSize,srcY / blockSize);
            if (!outOfBounds(destX / blockSize, destY / blockSize)) {
                if(levelLayout[destY / blockSize][destX / blockSize] != 1){
                    path = aStar.findPathTo(destX / blockSize,destY / blockSize);
                }
                else
                    path = aStar.findPathTo(xPosPacman / blockSize ,yPosPacman / blockSize );
            }else
                path = aStar.findPathTo(xPosPacman / blockSize ,yPosPacman/ blockSize );

            if(path.isEmpty())
                path = aStar.findPathTo(xPosPacman/ blockSize ,yPosPacman/ blockSize );

            direction = getDirection(path);
        }


        if (direction == 0) {
            srcY += -gv.getBlockSize() / 15;
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

    private boolean outOfBounds(int x, int y) {
        boolean res = false;
        if (x < 0 || x > 18 || y < 0 || y > 20)
            res = true;

        return res;

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


