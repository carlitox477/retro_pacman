package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import path.*;

import Behaviour.Behaviour;
import com.example.pacman.GameView;
import java.util.List;

public class ChaseRandom implements ChaseBehaviour {
    public List<Node> path;
    private int[] cornerDirections = {2, 2, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 3, 0, 0, 3, 3, 3};
    private boolean inCorner = false;
    private boolean chasing = false;
    private int step = 0;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] chase(GameView gv, int srcX, int srcY, int currentDirection) {
        int direction = defineDirection(gv, srcX, srcY, currentDirection);
        return Behaviour.getNextPosition(gv,direction,srcX,srcY);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int defineDirection(GameView gv, int srcX, int srcY, int currentDirection) {
        int blockSize,direction,xPacmanPos,yPacmanPos,xDist,yDist;
        double dist;
        AStar aStar;

        blockSize = gv.getBlockSize();
        direction=currentDirection;

        if ((srcX % blockSize == 0) && (srcY % blockSize == 0)) {
            xPacmanPos = gv.getxPosPacman();
            yPacmanPos = gv.getyPosPacman();
            xDist = Math.abs(srcX - xPacmanPos);
            yDist = Math.abs(srcY - yPacmanPos);
            dist = Math.hypot(xDist, yDist);

            if (dist <= 8 * blockSize) {
                chasing = true;
            } else {
                chasing = false;
            }

            if (chasing) {
                inCorner = false;
                step = 0;
                aStar = new AStar(gv,srcX /blockSize,srcY /blockSize);
                path = aStar.findPathTo(xPacmanPos / blockSize, yPacmanPos / blockSize);
                direction = Behaviour.getDirection(path);
            }
            else{
                if(srcX  / blockSize  == 4 && srcY  / blockSize == 15 ){
                    inCorner = true;
                }
                if(!inCorner) {
                    aStar = new AStar(gv, srcX / blockSize, srcY / blockSize);
                    path = aStar.findPathTo(4, 15);
                    direction = Behaviour.getDirection(path);
                }else{
                    direction = cornerDirections[step];
                    step++;
                    if(step == cornerDirections.length - 1)
                        step = 0;
                }
            }



        }
        return direction;
    }

}