package Game.Behavior.ChaseBehavior;

import Game.Character_package.Pacman;

public class BehaviorChaseRandom extends BehaviorChase {
    private int[]corner;

    public BehaviorChaseRandom(int[][] notUpDownDecisionPositions, int[]corner, int movementFluency,int[]defaultTarget) {
        super(notUpDownDecisionPositions,movementFluency,defaultTarget);
        this.corner=corner;
    }

    @Override
    public int[] behave(int[][]map, int[] ghostScreenPosition, Pacman pacman,char ghostDirection,int blocksize) {
        int xDist,yDist;
        int[]nextDirection,pacmanPosition,ghostMapPosition,target;
        double dist;

        ghostMapPosition=new int[]{ghostScreenPosition[0]/blocksize,ghostScreenPosition[1]/blocksize};
        pacmanPosition=new int[]{pacman.getPositionMapY(),pacman.getPositionMapX()};

        xDist = Math.abs(ghostMapPosition[1] - pacmanPosition[1]);
        yDist = Math.abs(ghostMapPosition[0] - pacmanPosition[0]);
        dist = Math.hypot(xDist, yDist);

        if(dist<=12){
            //target is the corner
            target=this.corner;
        }else{
            //target is the pacman
            target=pacmanPosition;
        }

        nextDirection=this.getNextDirectionPosition(ghostScreenPosition,target,map,ghostDirection,blocksize);

        return new int[]{nextDirection[0],nextDirection[1],nextDirection[2],this.movementFluency};
    }
}
