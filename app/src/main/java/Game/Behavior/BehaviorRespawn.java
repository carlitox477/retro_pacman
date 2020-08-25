package Game.Behavior;

import android.util.Log;
import Game.Character_package.Pacman;

public class BehaviorRespawn extends Behavior{
    private int[] respawnTarget;

    public BehaviorRespawn(int[] respawnTarget, int movementFluency,int[]defaultTarget) {
        super(movementFluency,defaultTarget);
        this.respawnTarget=respawnTarget;
    }

    @Override
    public int[] behave(int[][]map, int[] ghostScreenPosition, Pacman pacman,char ghostDirection,int blocksize) {
        int[]nextDirection;
        int[] ghostMapPosition;

        ghostMapPosition=new int[]{ghostScreenPosition[0]/blocksize,ghostScreenPosition[1]/blocksize};
        //Log.i("RT","["+ghostMapPosition[0]+", "+ghostMapPosition[1]+"]");


        if(this.shouldChangeDirection(ghostScreenPosition,blocksize)){
            if(map[ghostMapPosition[0]][ghostMapPosition[1]]==98){
                nextDirection=new int[]{ghostMapPosition[0],ghostMapPosition[1],'d',this.movementFluency};
            }else if(map[ghostMapPosition[0]][ghostMapPosition[1]]!=99 && map[ghostMapPosition[0]][ghostMapPosition[1]]!=10 ){
                nextDirection=this.nextDirection(ghostMapPosition,this.defaultTarget,map,ghostDirection,true);
            }else{
                nextDirection=this.nextDirection(ghostMapPosition,this.respawnTarget,map,ghostDirection,true);
            }
        }else{
            nextDirection=this.getNextDirection(ghostMapPosition,ghostDirection);
        }

        return new int[]{nextDirection[0],nextDirection[1],nextDirection[2],this.movementFluency};
    }

    @Override
    public boolean isRespawning(){
        return true;
    }
}
