package Game.Behaviour;

import Game.GameManager;
import java.util.List;
import Game.Path.Node;

public abstract class Behaviour {
    public abstract int[] behave(GameManager gameManager, int blocksize,int srcX, int srcY, int currentDirection);

    protected int[] getNextPosition(int blocksize, int direction, int srcX, int srcY){
        //¿por que 15?
        int[] nextPosition = new int[3];
        switch(direction){
            case 1:
                //Up
                srcY += - blocksize / 15;
                break;
            case 2:
                //Left
                srcX += blocksize / 15;
                break;
            case 3:
                //Down
                srcY += blocksize / 15;
                break;
            case 4:
                //Right
                srcX += -blocksize / 15;
                break;
            default:
                break;
        }

        nextPosition[0] = srcX;
        nextPosition[1] = srcY;
        nextPosition[2] = direction;
        return nextPosition;

    }
    protected char getDirection(List<Node> path){
        int nextX,nextY, currentX, currentY;
        char direction;
        Node nextNode,currentNode;
        direction = '0';

        if(path!=null &&path.size()>0){
            nextNode = path.get(1);
            currentNode = path.get(0);

            nextX = nextNode.x;
            nextY = nextNode.y;
            currentX = currentNode.x;
            currentY = currentNode.y;

            if (currentX - nextX == -1 && currentY - nextY == 0)
                direction = 'l'; //1
            else if (currentX - nextX == 0 && currentY - nextY == -1)
                direction = 'u'; //2
            else if (currentX - nextX == 1 && currentY - nextY == 0)
                direction = 'r';//3
            else if (currentX - nextX == 0 && currentY - nextY == 1)
                direction = 'd'; //0

            path.remove(0);
        }
        return direction;
    }

    protected boolean outOfBounds(int x, int y) {
        return (x < 0 || x > 18 || y < 0 || y > 20);
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

    public boolean isFrightened(){
        return false;
    }

    public boolean isRespawning(){
        return false;
    }

    public boolean isChasing(){
        return false;
    }

    public boolean isScattering(){
        return false;
    }
}
