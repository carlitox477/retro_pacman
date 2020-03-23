package com.example.pacman;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

public class ScatterTopRightCorner implements ScatterBehaviour {

    private int[] cornerDirections = {1, 1, 2, 2, 3, 3, 3, 0, 0, 1, 1};
    private boolean inCorner = false;
    private int step = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] scatter(GameView gv, int srcX, int srcY, int currentDirection) {


        int direction = currentDirection;
        float nextX;
        float nextY;

        int[] nextPosition = new int[3];


        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {

            if (srcX / gv.getBlockSize() == 15 && srcY / gv.getBlockSize() == 1) {
                inCorner = true;
            }

            if (!inCorner) {
                AStar aStar = new AStar(gv, srcX / gv.getBlockSize(), srcY / gv.getBlockSize());
                List<Node> path = aStar.findPathTo(15, 1);
                direction = getDirection(path);
            } else {
                direction = cornerDirections[step];
                step++;
                if (step == cornerDirections.length - 1)
                    step = 0;
            }


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

    private int getDirection(List<Node> path) {

        int direction = 4;

        int nextX;
        int nextY;
        int currentX;
        int currentY;

        Node nextNode;
        Node currentNode;

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
        }


        return direction;


    }
}
