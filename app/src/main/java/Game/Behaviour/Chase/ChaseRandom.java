package Game.Behaviour.Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Game.Character_package.Pacman;
import Game.Path.*;

import Game.GameManager;

import java.util.List;

public class ChaseRandom extends ChaseBehaviour {
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int defineDirection(GameManager gameManager,int blocksize, int ghostScreenPosX, int ghostScreenPosY, int currentDirection) {
        int direction,xPosMapPacman,yPosMapPacman,xDist,yDist,ghostMapPosX,ghostMapPosY;
        int[][]map;
        int[]target;
        double dist;
        Pacman pacman;
        AStar aStar;

        direction=currentDirection;
        map=gameManager.getGameMap().getMap();
        pacman=gameManager.getPacman();

        if ((ghostScreenPosX % blocksize == 0) && (ghostScreenPosY % blocksize == 0)) {
            xPosMapPacman=pacman.getPositionMapX();
            yPosMapPacman=pacman.getPositionMapY();
            ghostMapPosX=ghostScreenPosX /blocksize;
            ghostMapPosY=ghostScreenPosY /blocksize;

            xDist = Math.abs(ghostScreenPosX - pacman.getPositionScreenX());
            yDist = Math.abs(ghostScreenPosY - pacman.getPositionScreenY());
            dist = Math.hypot(xDist, yDist);

            if (dist <= 8 * blocksize) {
                //Buscar pacman
                target=new int[]{xPosMapPacman,yPosMapPacman};
            }else{
                //Buscar esquina?
                target=new int[]{4,15};
            }
            aStar = new AStar(map,ghostMapPosX,ghostMapPosY);
            path = aStar.findPathTo(target[0], target[1]);
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