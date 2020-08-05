package Game.Behavior.ChaseBehavior;

import android.util.Log;

import Game.Behavior.Behavior;

public abstract class BehaviorChase extends Behavior {
    protected int[][] notUpDownDecisionPositions;

    protected BehaviorChase(int[][]notUpDownDecisionPositions, int movementFluency,int[]defaultTarget) {
        super(movementFluency,defaultTarget);
        this.notUpDownDecisionPositions=notUpDownDecisionPositions;
    }

    protected int[]getNextDirectionPosition(int[] ghostScreenPosition,int[]target,int[][]map,char ghostDirection,int blocksize){
        int[] nextDirection,ghostMapPosition;
        boolean canGoUpDown;
        //ghostScreenPosition Y X
        //Log.i("Ghost Draw","["+ghostScreenPosition[1]%blocksize+", "+ghostScreenPosition[0]%blocksize+"]");
        ghostMapPosition=new int[]{ghostScreenPosition[0]/blocksize,ghostScreenPosition[1]/blocksize};
        canGoUpDown=this.canGoUpDown(ghostMapPosition,this.notUpDownDecisionPositions);


        if(this.shouldChangeDirection(ghostScreenPosition,blocksize)){
            if(this.useDefaultTarget(ghostMapPosition,map)){
                nextDirection=this.nextDirection(ghostMapPosition,this.defaultTarget,map,ghostDirection,canGoUpDown);
            }else{
                nextDirection=this.nextDirection(ghostMapPosition,target,map,ghostDirection,canGoUpDown);
            }
        }else{
            //Log.i("Behave ghost", "Don't change in position");
            nextDirection=this.getNextDirection(ghostMapPosition,ghostDirection);
        }
        return nextDirection;
    }

    @Override
    public boolean isAttacking(){
        return true;
    }


}
