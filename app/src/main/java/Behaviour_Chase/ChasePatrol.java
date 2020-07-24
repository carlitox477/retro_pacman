package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Character_package.Pacman;
import path.*;

import Behaviour.Behaviour;
import com.example.pacman.GameView;
import java.util.List;

public class ChasePatrol extends ChaseBehaviour {
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int defineDirection(GameView gv, int srcX, int srcY, int currentDirection) {
        Pacman pacman;
        int direction,blockSize,xPosMapPacman,yPosMapPacman,blinkyX,blinkyY,ghostMapPosX,ghostMapPosY;
        int[] target;
        int[][] map;
        boolean isOutOfBounds;

        pacman=gv.getPacman();
        blockSize = gv.getBlockSize();
        xPosMapPacman = pacman.getPositionMapX();
        yPosMapPacman = pacman.getPositionMapY();
        ghostMapPosX=srcX/blockSize;
        ghostMapPosY=srcY/blockSize;
        map = gv.getGameMap().getMap();
        direction = currentDirection;

        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {
            target=pacman.getNextPositionScreen();

            blinkyX = gv.getGhost(1).getxPos();
            blinkyY = gv.getGhost(1).getyPos();

            target[0] += ((target[0]  - blinkyX) * 2);
            target[1] += ((target[1] - blinkyY) * 2);

            isOutOfBounds=super.outOfBounds(target[0] / blockSize, target[1] / blockSize);
            AStar aStar = new AStar(gv,ghostMapPosX,ghostMapPosY);

            if (!isOutOfBounds && map[target[1] / blockSize][target[0] / blockSize] != 1) {
                path = aStar.findPathTo(target[0] / blockSize,target[1] / blockSize);
            }else {
                path = aStar.findPathTo(xPosMapPacman, yPosMapPacman);
            }

            direction = super.getDirection(path);
        }
        return direction;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] behave(GameView gv, int srcX, int srcY, int currentDirection) {
        return super.chase(gv,srcX,srcY,currentDirection);
    }
}


