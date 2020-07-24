package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import path.*;


import com.example.pacman.GameView;
import java.util.List;

public class ChaseRandom extends ChaseBehaviour {
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int defineDirection(GameView gv, int ghostScreenPosX, int ghostScreenPosY, int currentDirection) {
        int blockSize,direction,xPosMapPacman,yPosMapPacman,xDist,yDist,ghostMapPosX,ghostMapPosY;
        double dist;
        AStar aStar;

        blockSize = gv.getBlockSize();
        direction=currentDirection;

        if ((ghostScreenPosX % blockSize == 0) && (ghostScreenPosY % blockSize == 0)) {
            xPosMapPacman=gv.getPacman().getPositionMapX();
            yPosMapPacman=gv.getPacman().getPositionMapY();
            ghostMapPosX=ghostScreenPosX /blockSize;
            ghostMapPosY=ghostScreenPosY /blockSize;

            xDist = Math.abs(ghostScreenPosX - gv.getPacman().getPositionScreenX());
            yDist = Math.abs(ghostScreenPosY - gv.getPacman().getPositionScreenY());
            dist = Math.hypot(xDist, yDist);

            if (dist <= 8 * blockSize) {
                //Buscar pacman
                aStar = new AStar(gv,ghostMapPosX,ghostMapPosY);
                path = aStar.findPathTo(xPosMapPacman, yPosMapPacman);
                direction = super.getDirection(path);
            }else{
                //Buscar esquina
                aStar = new AStar(gv, ghostMapPosX, ghostMapPosY);
                path = aStar.findPathTo(4, 15);
                direction = super.getDirection(path);
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