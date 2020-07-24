package Behaviour;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.pacman.GameView;

import java.util.List;

import path.AStar;
import path.Node;

public class FrightenedBehaviour extends Behaviour{
    private int escapePointX = 0;
    private int escapePointY = 0;
    private List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] behave(GameView gv, int srcX, int srcY, int currentDirection) {
        //escape
        int direction,blocksize,posXmap,posYmap;
        int[] randomEscapePoint;
        AStar astar;

        blocksize=gv.getBlockSize();
        direction = currentDirection;

        if ((srcX % blocksize== 0) && (srcY % blocksize == 0)) {
            if(escapePointX == 0 && escapePointY == 0 || (escapePointX * blocksize == srcX && escapePointY * blocksize == srcY  )){
                posXmap=srcX / blocksize;
                posYmap=srcY / blocksize;

                randomEscapePoint = gv.getGameMap().generateMapSpawn();
                escapePointY =  randomEscapePoint[0];
                escapePointX = randomEscapePoint[1];
                astar = new AStar(gv,posXmap,posYmap);
                path = astar.findPathTo(escapePointX,escapePointY);
            }

            direction = super.getDirection(path);
        }

        return super.getNextPosition(gv.getBlockSize(), direction, srcX, srcY);
    }

    @Override
    public boolean isFrightened() {
        return true;
    }
}