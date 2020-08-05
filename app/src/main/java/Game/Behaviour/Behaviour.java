package Game.Behaviour;

import Game.Character_package.Ghost;
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

    protected int[] nextDirection(int[]currentMapPosition, int[][]map, int[] target, Ghost ghost){
        int left,up,down,right,xDist,yDist;
        int[][]positions;
        double[] distances;
        char[] direction;
        char opositeDirection=' ';
        double[] minDistance;
        int[] nextPosition;

        //opositeDirection=ghost.getOpositeDirection();
        distances=new double[4];
        direction=new char[1];
        minDistance=new double[1];
        nextPosition=new int[2];

        left=currentMapPosition[1]-1;
        right=currentMapPosition[1]+1;
        up=currentMapPosition[0]-1;
        down=currentMapPosition[0]+1;
        if(left==-1){
            left=map[0].length-1;
        }
        if(right==map[0].length){
            right=0;
        }
        if(up==-1){
            up=map.length-1;
        }
        if(down==map.length){
            right=0;
        }

        positions=new int[][]{
                {up,currentMapPosition[1]}, //up
                {currentMapPosition[0],left}, //left
                {down,currentMapPosition[1]}, //down
                {currentMapPosition[0],right}, //right
        };

        for(int i=0;i<distances.length;i++){
            yDist = Math.abs(positions[i][0]-target[0]);
            xDist= Math.abs(positions[i][1]-target[1]);
            distances[i]=Math.hypot(xDist, yDist);
        }

        minDistance(minDistance,direction,nextPosition,opositeDirection,map,positions[0],'u',distances[0],positions[1],'l',distances[1]);
        minDistance(minDistance,direction,nextPosition,opositeDirection,map,nextPosition,direction[0],minDistance[0],positions[2],'d',distances[2]);
        minDistance(minDistance,direction,nextPosition,opositeDirection,map,nextPosition,direction[0],minDistance[0],positions[3],'r',distances[3]);

        return new int[]{nextPosition[0],nextPosition[1],direction[0]};
    }

    private void minDistance(double[]minDistance,char[]resultDirection,int[]resPosition,char opositeDirection,int[][] map,int[]position1,char direction1,double distance1,int[]position2,char direction2,double distance2){
        //La direccion elegida no puede ser la opuesta a la direccion actual la siguiente
        //posicion debe ser una posición válida (que no sea pared)
        if(distance1<=distance2){
            if(map[position1[0]][position1[1]]!=1 && direction1!=opositeDirection){
                resultDirection[0]=direction1;
                minDistance[0]=distance1;

                resPosition[0]=position1[0];
                resPosition[1]=position1[1];
            }
        }else if(map[position2[0]][position2[1]]!=1 && direction2!=opositeDirection){
            resultDirection[0]=direction2;
            minDistance[0]=distance2;

            resPosition[0]=position2[0];
            resPosition[1]=position2[1];
        }else{
            //0,0 por error, ver si se puede cambiar
            resPosition[0]=0;
            resPosition[1]=0;
            resultDirection[0]=' ';
            minDistance[0]=Double.MAX_VALUE;
        }
    }

    protected abstract int[] getTarget(GameManager gameManager);

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
