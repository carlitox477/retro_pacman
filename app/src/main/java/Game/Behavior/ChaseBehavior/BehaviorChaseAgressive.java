package Game.Behavior.ChaseBehavior;

import Game.Character_package.Ghost;
import Game.Character_package.Pacman;

public class BehaviorChaseAgressive extends BehaviorChase {

    public BehaviorChaseAgressive(int[][]notUpDownDecisionPositions, int movementFluency,int[]defaultTarget) {
        super(notUpDownDecisionPositions,movementFluency,defaultTarget);
    }

    @Override
    public int[] behave(int[][]map, int[] ghostScreenPosition, Pacman pacman,char ghostDirection,int blocksize){
        //ghosy is Y X format
        //Always tries to get the pacman
        int[]nextDirection,pacmanMapPosition;
        pacmanMapPosition=new int[]{pacman.getPositionMapY(),pacman.getPositionMapX()};
        nextDirection=this.getNextDirectionPosition(ghostScreenPosition,pacmanMapPosition,map,ghostDirection,blocksize);

        return new int[]{nextDirection[0],nextDirection[1],(char)nextDirection[2],this.movementFluency};
    }
}
