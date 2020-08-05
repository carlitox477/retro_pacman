package Game.Behaviour;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Game.GameManager;
import Game.GameMap;
import java.util.List;
import Game.Path.*;

public class FrightenedBehaviour extends Behaviour{
    private int escapePointX = 0;
    private int escapePointY = 0;
    private List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] behave(GameManager gameManager, int blocksize, int srcX, int srcY, int currentDirection) {
        //escape
        int direction,posXmap,posYmap;
        int[] randomEscapePoint;
        GameMap gameMap;
        AStar astar;

        gameMap=gameManager.getGameMap();
        direction = currentDirection;

        if ((srcX % blocksize== 0) && (srcY % blocksize == 0)) {
            if(escapePointX == 0 && escapePointY == 0 || (escapePointX * blocksize == srcX && escapePointY * blocksize == srcY  )){
                posXmap=srcX / blocksize;
                posYmap=srcY / blocksize;

                randomEscapePoint = gameMap.generateMapSpawn();
                escapePointY =  randomEscapePoint[0];
                escapePointX = randomEscapePoint[1];
                astar = new AStar(gameMap.getMap(),posXmap,posYmap);
                path = astar.findPathTo(escapePointX,escapePointY);
            }

            direction = super.getDirection(path);
        }

        return super.getNextPosition(blocksize, direction, srcX, srcY);
    }

    @Override
    protected int[] getTarget(GameManager gameManager) {
        return new int[0];
    }

    @Override
    public boolean isFrightened() {
        return true;
    }
}