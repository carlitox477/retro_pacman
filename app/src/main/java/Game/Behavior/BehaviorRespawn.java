package Game.Behavior;

import Game.Character_package.Ghost;
import Game.Character_package.Pacman;

public class BehaviorRespawn extends Behavior{
    private int[] respawnTarget;

    public BehaviorRespawn(int[] respawnTarget, int movementFluency,int[]defaultTarget) {
        super(movementFluency,defaultTarget);
        this.respawnTarget=respawnTarget;
    }

    @Override
    public int[] behave(int[][]map, int[] ghostMapPosition, Pacman pacman,char ghostDirection,int blocksize) {
        int[]nextDirection;

        nextDirection=this.nextDirection(ghostMapPosition,this.respawnTarget,map,ghostDirection,true);

        return new int[]{nextDirection[0],nextDirection[1],nextDirection[2],this.movementFluency};
    }

    @Override
    public boolean isRespawning(){
        return true;
    }
}
