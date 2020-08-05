package Game.Behavior.ChaseBehavior;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import Game.Character_package.Ghost;
import Game.Character_package.Pacman;

public class BehaviorChasePatrol extends BehaviorChase {
    private Ghost blinky;

    public BehaviorChasePatrol(int[][] notUpDownDecisionPositions, Ghost blinky, int movementFluency,int[]defaultTarget) {
        super(notUpDownDecisionPositions,movementFluency,defaultTarget);
        this.blinky=blinky;
    }

    @Override
    public int[] behave(int[][]map, int[] ghostScreenPosition, Pacman pacman,char ghostDirection,int blocksize) {
        int[]blinkyMapPosition,pacmanMapPosition,blinkyTarget,target,nextDirection,ghostMapPosition;
        char pacmanCurrentDirection;

        pacmanCurrentDirection=pacman.getCurrentDirection();
        blinkyMapPosition=new int[]{this.blinky.getPositionMapY(),this.blinky.getPositionMapX()};
        pacmanMapPosition=new int[]{pacman.getPositionMapY(),pacman.getPositionMapX()};
        blinkyTarget=this.getBlinkyTarget(pacmanMapPosition,pacmanCurrentDirection);
        target=this.defineTarget(blinkyMapPosition,blinkyTarget,map);

        nextDirection=this.getNextDirectionPosition(ghostScreenPosition,target,map,ghostDirection,blocksize);

        return new int[]{nextDirection[0],nextDirection[1],nextDirection[2],this.movementFluency};
    }

    @NotNull
    @Contract(pure = true)
    private int[]defineTarget(@NotNull int[]blinkyMapPosition, @NotNull int[]blinkyTarget, int[][]map){
        int[]vector,target;
        vector=new int[]{(blinkyTarget[0]-blinkyMapPosition[0])*2,(blinkyTarget[1]-blinkyMapPosition[1])*2};
        target=new int[]{blinkyTarget[0]+vector[0],blinkyTarget[1]+vector[1]};

        if(target[0]<0){
            target[0]=0;
        }else if(target[0]>=map.length){
            target[0]=map.length-1;
        }

        if(target[1]<0){
            target[1]=0;
        }else if(target[1]>=map[0].length){
            target[1]=map[0].length-1;
        }
        return target;
    }

    private int[]getBlinkyTarget(int[]pacmanMapPosition,char pacmanCurrentDirection){
        int[]target;

        target=new int[]{pacmanMapPosition[0],pacmanMapPosition[1]};

        switch (pacmanCurrentDirection){
            case 'u':
                target[0]-=2;
                break;
            case 'l':
                target[1]-=2;
                break;
            case 'd':
                target[0]+=2;
                break;
            default:
                target[1]+=2;
                break;
        }
        return target;
    }
}
