package Game.Behavior;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import Game.Character_package.Ghost;
import Game.Character_package.Pacman;
import Game.Path.Path;
import Game.Path.PathNode;

public abstract class Behavior {
    protected int[]defaultTarget;
    protected int movementFluency;

    protected Behavior (int movementFluency,int[]defaultTarget){
        this.movementFluency=movementFluency;
        this.defaultTarget=defaultTarget;
    }

    public abstract int[] behave(int[][]map, int[] ghostMapPosition, Pacman pacman,char ghostDirection, int blocksize);

    protected int[] nextDirection(int[] ghostPosition,int[] target,int[][]map,char ghostDirection,boolean canGoUpDown){
        //ghostPosition Y X
        int xDist,yDist;
        int[][]nearPositions;
        double[] distances;
        char[] direction;
        char opositeDirection;
        double[] minDistance;
        int[] nextPosition;

        distances=new double[4];
        direction=new char[1];
        minDistance=new double[1];
        nextPosition=new int[2];
        opositeDirection=this.getOpositeDirection(ghostDirection);

        nearPositions=this.getNearPositions(map,ghostPosition);
        for(int i=0;i<distances.length;i++){
            yDist = Math.abs(nearPositions[i][0]-target[0]);
            xDist= Math.abs(nearPositions[i][1]-target[1]);
            distances[i]=Math.hypot(xDist, yDist);
        }

        if(canGoUpDown){
            //Log.i("GPD","Strange");
            minDistance(minDistance,direction,nextPosition,opositeDirection,map,nearPositions[0],'u',distances[0],nearPositions[1],'l',distances[1],ghostPosition,ghostDirection);
            minDistance(minDistance,direction,nextPosition,opositeDirection,map,nextPosition,direction[0],minDistance[0],nearPositions[2],'d',distances[2],ghostPosition,ghostDirection);
            minDistance(minDistance,direction,nextPosition,opositeDirection,map,nextPosition,direction[0],minDistance[0],nearPositions[3],'r',distances[3],ghostPosition,ghostDirection);
        }else{
            minDistance(minDistance,direction,nextPosition,opositeDirection,map,nearPositions[1],'l',distances[1],nearPositions[3],'r',distances[3],ghostPosition,ghostDirection);
        }

        return new int[]{nextPosition[0],nextPosition[1],direction[0]};
    }

    protected void minDistance(double[]minDistance,char[]resultDirection,int[]resPosition,char opositeDirection,int[][] map,int[]position1,char direction1,double distance1,int[]position2,char direction2,double distance2,int[]currentPosition, char ghostDirection){
        //La direccion elegida no puede ser la opuesta a la direccion actual la siguiente
        boolean canUsePosition1,canUsePosition2;

        canUsePosition1=direction1!=opositeDirection && map[position1[0]][position1[1]]!=1;
        canUsePosition2=direction2!=opositeDirection && map[position2[0]][position2[1]]!=1;
        //posicion debe ser una posición válida (que no sea pared)
        if(canUsePosition1 && (!canUsePosition2 || distance1<=distance2)){
            resultDirection[0]=direction1;
            minDistance[0]=distance1;

            resPosition[0]=position1[0];
            resPosition[1]=position1[1];
        }else if(canUsePosition2){
            resultDirection[0]=direction2;
            minDistance[0]=distance2;

            resPosition[0]=position2[0];
            resPosition[1]=position2[1];
        }else{
            //Log.i("Min Distance","CURRENT POSITION ["+(currentPosition[0]+1)+","+(currentPosition[1]+1)+"], DIRECTION "+ghostDirection+"]\nPOSITION1 ["+(position1[0]+1)+", "+(position1[1]+1)+"] DIRECTION "+direction1+ ", DISTANCE "+distance1+"\n"+"POSITION2 ["+(position2[0]+1)+", "+(position2[1]+1)+"] DIRECTION "+direction2+ ", DISTANCE "+distance2);
            resPosition[0]=0;
            resPosition[1]=0;
            resultDirection[0]=' ';
            minDistance[0]=Double.MAX_VALUE;
        }
    }

    public char getOpositeDirection(char direction){
        char oposite;
        switch (direction){
            case 'u':
                oposite='d';
                break;
            case 'd':
                oposite='u';
                break;
            case 'r':
                oposite='l';
                break;
            case 'l':
                oposite='r';
                break;
            default:
                oposite=' ';
                break;
        }
        return oposite;
    }

    public int[][]getNearPositions(int[][]map,int[] ghostPosition){
        int left,right,up,down,posXMap,posYMap;

        posXMap=ghostPosition[1];
        posYMap=ghostPosition[0];
        left=posXMap-1;
        right=posXMap+1;
        up=posYMap-1;
        down=posYMap+1;
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


        return new int[][]{
                {up,posXMap}, //up
                {posYMap,left}, //left
                {down,posXMap}, //down
                {posYMap,right}, //right
        };
    }

    protected boolean canGoUpDown(int[]currentPosition,int[][]notUpDownPositions){
        boolean can;
        int i;

        can=true;
        i=0;

        while (can && i<notUpDownPositions.length){
            if(currentPosition[0]==notUpDownPositions[i][0] &&currentPosition[1]==notUpDownPositions[i][1]){
                can=false;
            }
            i++;
        }
        return can;
    }

    @NotNull
    protected Path getPath(int[][]map, int[] ghostMapPosition, int[] target, char ghostDirection, int[][]notUpDownPositions){
        Path pathToTarget;
        PathNode node;
        boolean cont,canGoUpDown;
        int[]currentPosition,positionToAdd;
        char currentDirection;

        canGoUpDown=this.canGoUpDown(ghostMapPosition,notUpDownPositions);
        node=new PathNode(ghostMapPosition,ghostDirection);
        pathToTarget=new Path(node,target);
        cont=true;
        currentPosition=ghostMapPosition;
        currentDirection=ghostDirection;

        while(cont){
            positionToAdd=this.nextDirection(currentPosition,target,map,currentDirection,canGoUpDown);
            currentPosition=new int[]{positionToAdd[0],positionToAdd[1]};
            currentDirection=(char)positionToAdd[2];
            node=new PathNode(currentPosition,currentDirection);
            cont=pathToTarget.addNode(node) && !(currentPosition[0]!=target[0]&&currentPosition[1]!=target[1]);
        }
        return pathToTarget;
    }

    public boolean isFrightened(){
        return false;
    }

    public boolean isRespawning(){
        return false;
    }

    public boolean isAttacking(){
        return false;
    }

    protected boolean shouldChangeDirection(int[]ghostScreenPosition, int blocksize){
        return ghostScreenPosition[0]%blocksize==0 && ghostScreenPosition[1]%blocksize==0;
    }

    protected int[] getNextDirection(int[]ghostMapPosition,char direction){
        switch (direction){
            case 'u':
                ghostMapPosition[0]-=1;
                break;
            case 'd':
                ghostMapPosition[0]+=1;
                break;
            case 'r':
                ghostMapPosition[1]+=1;
                break;
            case 'l':
                ghostMapPosition[1]-=1;
                break;
            default:
                break;
        }

        return new int[]{ghostMapPosition[0],ghostMapPosition[1],direction};
    }

    protected boolean useDefaultTarget(int[] ghostMapPosition,int[][]map){
        return map[ghostMapPosition[0]][ghostMapPosition[1]]==99 || map[ghostMapPosition[0]][ghostMapPosition[1]]==10;
    }

}
