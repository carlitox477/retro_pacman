package Game.Behaviour.Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Game.Character_package.Pacman;
import Game.Path.*;

import Game.GameManager;
import java.util.List;

public class ChasePatrol extends ChaseBehaviour {
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int defineDirection(GameManager gameManager, int blocksize, int ghostScreenPosX, int ghostScreenPosY, int currentDirection) {
        Pacman pacman;
        int direction,xPosMapPacman,yPosMapPacman,ghostMapPosX,ghostMapPosY;
        int[] target;
        int[][] map;
        boolean isOutOfBounds;

        pacman=gameManager.getPacman();
        xPosMapPacman = pacman.getPositionMapX();
        yPosMapPacman = pacman.getPositionMapY();
        ghostMapPosX=ghostScreenPosX/blocksize;
        ghostMapPosY=ghostScreenPosY/blocksize;
        map = gameManager.getGameMap().getMap();
        direction = currentDirection;

        if ((ghostScreenPosX % blocksize == 0) && (ghostScreenPosY % blocksize == 0)) {
            target=pacman.getNextPositionScreen();

            target[0] += ((target[0]  - ghostScreenPosX) * 2);
            target[1] += ((target[1] - ghostScreenPosY) * 2);

            isOutOfBounds=super.outOfBounds(target[0] / blocksize, target[1] / blocksize);
            AStar aStar = new AStar(map,ghostMapPosX,ghostMapPosY);

            if (!isOutOfBounds && map[target[1] / blocksize][target[0] / blocksize] != 1) {
                path = aStar.findPathTo(target[0] / blocksize,target[1] / blocksize);
            }else {
                path = aStar.findPathTo(xPosMapPacman, yPosMapPacman);
            }

            direction = super.getDirection(path);
        }
        return direction;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] behave(GameManager gameManager, int blocksize, int srcX, int srcY, int currentDirection) {
        return super.chase(gameManager,blocksize, srcX, srcY, currentDirection);
    }
}


