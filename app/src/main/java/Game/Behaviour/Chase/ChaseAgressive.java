package Game.Behaviour.Chase;
import android.os.Build;
import androidx.annotation.RequiresApi;


import Game.GameManager;

import Game.Character_package.Pacman;
import Game.Path.*;

import java.util.List;

public class ChaseAgressive extends ChaseBehaviour{
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected int defineDirection(GameManager gameManager,int blocksize, int ghostScreenPosX, int ghostScreenPosY, int currentDirection){
        List<Node> newPath;
        AStar aStar;
        Pacman pacman=gameManager.getPacman();
        int direction, xPosMapPacman,yPosMapPacman,ghostMapPosX,ghostMapPosY;
        int[][] map;

        map=gameManager.getGameMap().getMap();
        direction=currentDirection;
        xPosMapPacman=pacman.getPositionMapX();
        yPosMapPacman=pacman.getPositionMapY();
        ghostMapPosX=ghostScreenPosX / blocksize;
        ghostMapPosY=ghostScreenPosY / blocksize;

        if ((ghostScreenPosX % blocksize == 0) && (ghostScreenPosY % blocksize == 0)) {
            aStar = new AStar(map, ghostMapPosX,ghostMapPosY);
            newPath = aStar.findPathTo(xPosMapPacman,yPosMapPacman);

            if(newPath == null)
                direction = super.getDirection(path);
            else
                direction = super.getDirection(newPath);

        }
        return direction;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] behave(GameManager gameManager, int blocksize, int srcX, int srcY, int currentDirection) {
        return super.chase(gameManager,blocksize, srcX, srcY, currentDirection);
    }
}


