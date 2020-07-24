package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Behaviour.*;
import com.example.pacman.GameView;
import path.*;

import java.util.List;

public class ChaseAgressive extends ChaseBehaviour{
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected int defineDirection(GameView gv, int ghostScreenPosX, int ghostScreenPosY, int currentDirection){
        List<Node> newPath;
        AStar aStar;
        int direction,blocksize, xPosMapPacman,yPosMapPacman,ghostMapPosX,ghostMapPosY;


        blocksize=gv.getBlockSize();
        direction=currentDirection;
        xPosMapPacman=gv.getPacman().getPositionMapX();
        yPosMapPacman=gv.getPacman().getPositionMapY();
        ghostMapPosX=ghostScreenPosX / blocksize;
        ghostMapPosY=ghostScreenPosY / blocksize;

        if ((ghostScreenPosX % blocksize == 0) && (ghostScreenPosY % blocksize == 0)) {
            aStar = new AStar(gv, ghostMapPosX,ghostMapPosY);
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
    public int[] behave(GameView gv, int srcX, int srcY, int currentDirection) {
        return super.chase(gv, srcX, srcY, currentDirection);
    }
}


