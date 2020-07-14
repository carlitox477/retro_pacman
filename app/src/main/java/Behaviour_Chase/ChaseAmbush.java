package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import path.*;

import Behaviour.Behaviour;
import com.example.pacman.GameView;

import java.util.List;

public class ChaseAmbush implements ChaseBehaviour{
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] chase(GameView gv, int srcX, int srcY, int currentDirection) {
        int direction;
        direction=defineDirection(gv,srcX,srcY,currentDirection);
        return Behaviour.getNextPosition(gv,direction,srcX,srcY);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int defineDirection(GameView gv, int srcX, int srcY, int currentDirection){
        int pacmanDirection,blockSize,xPosPacman,yPosPacman, direction;
        int[][] map;
        int[] target;
        AStar aStar;
        List<Node> newPath;

        target=new int[2];
        target[0]=0;
        target[1]=0;
        direction=currentDirection;
        blockSize= gv.getBlockSize();
        pacmanDirection = gv.getPacmanDirection();
        xPosPacman = gv.getxPosPacman();
        yPosPacman = gv.getyPosPacman();
        map = gv.getMap();


        if ((srcX % blockSize == 0) && (srcY % blockSize== 0)) {
            Behaviour.getPacmanNextPosition(pacmanDirection,target,blockSize,xPosPacman,yPosPacman);

            if (!Behaviour.outOfBounds(target[0] / blockSize, target[1] / blockSize)) {
                if (map[target[1] / blockSize][target[0] / blockSize] != 1) {
                    aStar = new AStar(gv, srcX / blockSize, srcY / blockSize);
                    newPath = aStar.findPathTo(target[0] / blockSize, target[1] / blockSize);
                    if (newPath == null)
                        direction = Behaviour.getDirection(path);
                    else
                        direction = Behaviour.getDirection(newPath);
                } else {
                    aStar = new AStar(gv, srcX / blockSize, srcY / blockSize);
                    newPath = aStar.findPathTo(xPosPacman / blockSize, yPosPacman / blockSize);
                    direction = Behaviour.getDirection(newPath);
                }
            }else{
                aStar = new AStar(gv, srcX / blockSize, srcY / blockSize);
                newPath = aStar.findPathTo(xPosPacman / blockSize, yPosPacman / blockSize);
                direction = Behaviour.getDirection(newPath);
            }


        }
        return direction;
    }


}


