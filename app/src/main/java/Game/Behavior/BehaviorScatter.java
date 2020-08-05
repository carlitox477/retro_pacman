package Game.Behavior;

import Game.Character_package.Pacman;

public class BehaviorScatter extends Behavior {
    private int[]corner;
    int[][]notUpDownDecisionPositions;

    public BehaviorScatter(int[] corner, int[][]notUpDirectionPositions, int movementFluency,int[]defaultTarget) {
        super(movementFluency,defaultTarget);
        this.notUpDownDecisionPositions=notUpDirectionPositions;
        this.corner=corner;
    }

    @Override
    public int[] behave(int[][]map, int[] ghostScreenPosition, Pacman pacman,char ghostDirection,int blocksize){
        int[]nextDirection,ghostMapPosition, target;
        boolean canGoUpDown;

        ghostMapPosition=new int[]{ghostScreenPosition[0]/blocksize,ghostScreenPosition[1]/blocksize};
        canGoUpDown=this.canGoUpDown(ghostMapPosition,this.notUpDownDecisionPositions);

        if(this.shouldChangeDirection(ghostScreenPosition,blocksize)){
            if(this.useDefaultTarget(ghostMapPosition,map)){
                target=this.defaultTarget;
            }else{
                target=this.corner;
            }
            nextDirection=this.nextDirection(ghostMapPosition,target,map,ghostDirection,canGoUpDown);
        }else{
            nextDirection=this.getNextDirection(ghostMapPosition,ghostDirection);
        }

        return new int[]{nextDirection[0],nextDirection[1],(char)nextDirection[2],this.movementFluency};
    }

    @Override
    public boolean isAttacking(){
        return true;
    }


}
