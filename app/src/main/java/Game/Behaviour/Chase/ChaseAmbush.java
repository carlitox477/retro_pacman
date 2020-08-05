package Game.Behaviour.Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Game.Character_package.Pacman;
import Game.Path.*;

import Game.GameManager;

import java.util.List;

public class ChaseAmbush extends ChaseBehaviour{
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected int defineDirection(GameManager gameManager, int blocksize, int ghostScreenPosX, int ghostScreenPosY, int currentDirection){
        int xPosMapPacman,yPosMapPacman, direction,ghostMapPosX,ghostMapPosY;
        boolean isOutBound;

        int[][] map;
        int[] target;
        AStar aStar;
        List<Node> newPath;
        Pacman pacman;

        pacman=gameManager.getPacman();

        direction=currentDirection;

        xPosMapPacman=pacman.getPositionMapX();
        yPosMapPacman=pacman.getPositionMapY();
        map = gameManager.getGameMap().getMap();


        if ((ghostScreenPosX % blocksize == 0) && (ghostScreenPosY % blocksize== 0)) {
            target=pacman.getNextPositionScreen();
            ghostMapPosX=ghostScreenPosX/blocksize;
            ghostMapPosY=ghostScreenPosY/blocksize;

            isOutBound=super.outOfBounds(target[0] / blocksize, target[1] / blocksize);

            aStar = new AStar(map, ghostMapPosX, ghostMapPosY);
            if(!isOutBound && map[target[1] / blocksize][target[0] / blocksize] != 1){
                newPath = aStar.findPathTo(target[0] / blocksize, target[1] / blocksize);
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
    public int[] behave(GameManager gameManager, int blocksize, int srcX, int srcY, int currentDirection) {
        return super.chase(gameManager,blocksize, srcX, srcY, currentDirection);
    }

    @Override
    protected int[] getTarget(GameManager gameManager) {
        return new int[0];
    }
}


