package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import path.*;

import Behaviour.Behaviour;
import com.example.pacman.GameView;
import java.util.List;

public class ChasePatrol implements ChaseBehaviour {
    public List<Node> path;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] chase(GameView gv, int srcX, int srcY, int currentDirection) {
        int direction = defineDirection(gv,srcX, srcY,currentDirection);
        return Behaviour.getNextPosition(gv,direction,srcX,srcY);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int defineDirection(GameView gv, int srcX, int srcY, int currentDirection) {
        int direction,pacmanDirection,blockSize,xPosPacman,yPosPacman,blinkyX,blinkyY,vectorX,vectorY;
        int[] target;
        int[][] map;

        pacmanDirection = gv.getPacmanDirection();
        blockSize = gv.getBlockSize();
        xPosPacman = gv.getxPosPacman();
        yPosPacman = gv.getyPosPacman();
        map = gv.getMap();
        direction = currentDirection;

        target=new int[2];
        target[0] = 0; //X target
        target[1] = 0; //Y target



        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {
            Behaviour.getPacmanNextPosition(pacmanDirection,target,blockSize,xPosPacman,yPosPacman);

            blinkyX = gv.getGhost(1).getxPos();
            blinkyY = gv.getGhost(1).getyPos();

            vectorX = (target[0]  - blinkyX) * 2;
            vectorY = (target[1] - blinkyY) * 2;

            target[0] += vectorX;
            target[1] += vectorY;

            AStar aStar = new AStar(gv,srcX / blockSize,srcY / blockSize);
            if (!Behaviour.outOfBounds(target[0] / blockSize, target[1] / blockSize)) {
                if(map[target[1] / blockSize][target[0] / blockSize] != 1){
                    path = aStar.findPathTo(target[0] / blockSize,target[1] / blockSize);
                }
                else
                    path = aStar.findPathTo(xPosPacman / blockSize ,yPosPacman / blockSize );
            }else
                path = aStar.findPathTo(xPosPacman / blockSize ,yPosPacman/ blockSize );

            if(path.isEmpty())
                path = aStar.findPathTo(xPosPacman/ blockSize ,yPosPacman/ blockSize );

            direction = Behaviour.getDirection(path);
        }
        return direction;
    }

}


