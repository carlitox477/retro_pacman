package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Character_package.Pacman;
import path.*;

import Behaviour.Behaviour;
import com.example.pacman.GameView;

import java.util.List;

public class ChaseAmbush extends ChaseBehaviour{
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected int defineDirection(GameView gv, int ghostScreenPosX, int ghostScreenPosY, int currentDirection){
        int blockSize,xPosMapPacman,yPosMapPacman, direction,ghostMapPosX,ghostMapPosY;
        boolean isOutBound;

        int[][] map;
        int[] target;
        AStar aStar;
        List<Node> newPath;
        Pacman pacman;

        pacman=gv.getPacman();

        direction=currentDirection;
        blockSize= gv.getBlockSize();

        xPosMapPacman=pacman.getPositionMapX();
        yPosMapPacman=pacman.getPositionMapY();
        map = gv.getGameMap().getMap();


        if ((ghostScreenPosX % blockSize == 0) && (ghostScreenPosY % blockSize== 0)) {
            target=pacman.getNextPositionScreen();
            ghostMapPosX=ghostScreenPosX/blockSize;
            ghostMapPosY=ghostScreenPosY/blockSize;

            isOutBound=super.outOfBounds(target[0] / blockSize, target[1] / blockSize);

            aStar = new AStar(gv, ghostMapPosX, ghostMapPosY);
            if(!isOutBound && map[target[1] / blockSize][target[0] / blockSize] != 1){
                newPath = aStar.findPathTo(target[0] / blockSize, target[1] / blockSize);
                if (newPath == null) {
                    direction = super.getDirection(path);
                }else {
                    direction = super.getDirection(newPath);
                }
            }else{
                newPath = aStar.findPathTo(xPosMapPacman, yPosMapPacman);
                direction = super.getDirection(newPath);
            }
        }
        return direction;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] behave(GameView gv, int srcX, int srcY, int currentDirection) {
        return super.chase(gv,srcX,srcY,currentDirection);
    }
}


