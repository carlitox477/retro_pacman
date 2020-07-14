package Behaviour;

import com.example.pacman.GameView;

import java.util.List;

import path.Node;

public abstract class Behaviour {
    public static int[] getNextPosition(GameView gv, int direction, int srcX, int srcY){
        int[] nextPosition = new int[3];
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
    public static int getDirection(List<Node> path){
        int nextX,nextY, currentX, currentY,direction;
        Node nextNode,currentNode;
        direction = 4;

        if(path!=null &&path.size()>0){
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
        return direction;
    }

    public static boolean outOfBounds(int x, int y) {
        boolean res = false;
        if (x < 0 || x > 18 || y < 0 || y > 20)
            res = true;

        return res;

    }

    public static void getPacmanNextPosition(int pacmanDirection,int[]destPacman, int blockSize, int xPosPacman,int yPosPacman){
        //destPacman[0]= X position
        //destPacman[1]= Y position
        switch (pacmanDirection){
            case 0:
                destPacman[0] = xPosPacman + blockSize / 2;
                destPacman[1] = yPosPacman - 2 * blockSize - blockSize / 2;
                break;
            case 1:
                destPacman[0]  = xPosPacman + 3 * blockSize + blockSize / 2;
                destPacman[1]  = yPosPacman + blockSize / 2;
                break;
            case 2:
                destPacman[0] = xPosPacman + blockSize / 2;
                destPacman[1]  = yPosPacman + 3 * blockSize + blockSize / 2;
                break;
            case 3:
                destPacman[0] = xPosPacman - 2 * blockSize - blockSize / 2;
                destPacman[1]  = yPosPacman + blockSize / 2;
                break;
            case 4:
                destPacman[0] = xPosPacman;
                destPacman[1]  = yPosPacman;
                break;
            default:
                break;
        }

    }
}
