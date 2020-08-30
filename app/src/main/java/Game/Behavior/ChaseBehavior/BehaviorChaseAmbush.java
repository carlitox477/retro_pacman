package Game.Behavior.ChaseBehavior;

import org.jetbrains.annotations.NotNull;

import Game.Character_package.Ghost;
import Game.Character_package.Pacman;

public class BehaviorChaseAmbush extends BehaviorChase {
    public BehaviorChaseAmbush(int[][] notUpDownDecisionPositions, int movementFluency,int[]defaultTarget) {
        super(notUpDownDecisionPositions,movementFluency,defaultTarget);
    }

    @Override
    public int[] behave(int[][]map, int[] ghostScreenPosition, Pacman pacman,char ghostDirection,int blocksize) {
        int[] target,nextDirection;
        target=this.decideTarget(map,pacman);
        nextDirection=this.getNextDirectionPosition(ghostScreenPosition,target,map,ghostDirection,blocksize);

        return new int[]{nextDirection[0],nextDirection[1],nextDirection[2],this.movementFluency};
    }

    private int[] decideTarget(int[][]map, @NotNull Pacman pacman){
        char pacmanDirection;
        int[]pacmanMapPosition,target;
        int[][]targetPositions;
        int up,left,down,right;

        pacmanDirection=pacman.getCurrentDirection();
        pacmanMapPosition=new int[]{pacman.getPositionMapY(),pacman.getPositionMapX()};

        up=pacmanMapPosition[0]-4;
        left=pacmanMapPosition[1]-4;
        down=pacmanMapPosition[0]+4;
        right=pacmanMapPosition[1]+4;

        if(up<0){
            up=0;
        }

        if(left<0){
            left=0;
        }

        if(down>=map.length){
            down=map.length-1;
        }

        if(right>=map[0].length){
            right=map[0].length-1;
        }


        targetPositions=new int[][]{
                {up,left},//up
                {pacmanMapPosition[0],left},//left
                {down,pacmanMapPosition[1]},//down
                {pacmanMapPosition[0],right}//right
        };

        switch (pacmanDirection){
            case 'u':
                target=targetPositions[0];
                break;
            case 'l':
                target=targetPositions[1];
                break;
            case 'd':
                target=targetPositions[2];
                break;
            default:
                target=targetPositions[3];
                break;
        }

        return target;
    }
}
