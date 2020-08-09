package Game.Behavior;

import android.util.Log;

import java.util.Random;

import Game.Character_package.Pacman;

public class BehaviorFrighten extends Behavior {
    private boolean firstMoveDone;

    public BehaviorFrighten(int movementFluency,int[]defaultTarget) {
        super(movementFluency,defaultTarget);
        this.firstMoveDone=false;
    }

    @Override
    public int[] behave(int[][]map, int[] ghostScreenPosition, Pacman pacman,char ghostDirection, int blocksize) {
        int[][] nearPositions;
        int[]ghostMapPosition,nextPosition;
        char opositeDirection,nextDirection;
        char[]directions;
        int posDirection,nextPosValue;


        ghostMapPosition=new int[]{ghostScreenPosition[0]/blocksize,ghostScreenPosition[1]/blocksize};
        opositeDirection=this.getOpositeDirection(ghostDirection);
        nearPositions=this.getNearPositions(map,ghostMapPosition);
        directions=new char[]{'u','l','d','r'};


        if(this.shouldChangeDirection(ghostScreenPosition,blocksize)){
            if(!this.firstMoveDone){
                this.firstMoveDone=true;
                Log.i("Scare Behavior","Change direction "+ghostDirection);
            }
            //Log.i("Respawn B","Current direction "+ghostDirection+"; Current position ["+ghostMapPosition[1]+";"+ghostMapPosition[0]+"]");
            do{
                posDirection=new Random().nextInt(directions.length);
                nextDirection=directions[posDirection];
                switch (nextDirection){
                    case 'u':
                        nextPosition=nearPositions[0];
                        break;
                    case 'l':
                        nextPosition=nearPositions[1];
                        break;
                    case 'd':
                        nextPosition=nearPositions[2];
                        break;
                    case 'r':
                        nextPosition=nearPositions[3];
                        break;
                    default:
                        nextPosition=null;
                        break;
                }
                if(nextPosition!=null){
                    nextPosValue=map[nextPosition[0]][nextPosition[1]];
                }else{
                    nextPosValue=1;
                }
            }while(nextPosition!=null && (nextDirection==opositeDirection || nextPosValue==1 || nextPosValue==10));//wall or ghost house wall
            //Log.i("Respawn CD","New direction "+nextDirection+"; Next position ["+nextPosition[1]+";"+nextPosition[0]+"]");
        }else{
            if(!this.firstMoveDone){
                this.firstMoveDone=true;
                Log.i("Scare Behavior","Continue direction "+ghostDirection);
            }
            nextPosition=this.getNextDirection(ghostMapPosition,ghostDirection);
            nextDirection=(char)nextPosition[2];
        }
        return new int[]{nextPosition[0],nextPosition[1],nextDirection,this.movementFluency};
    }

    @Override
    public boolean isFrightened(){
        return true;
    }

}
