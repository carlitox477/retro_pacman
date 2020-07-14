package Behaviour;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.pacman.GameView;

import java.util.List;

import path.AStar;
import path.Node;

public class FrightenedBehaviour {


    private int escapePointX = 0;
    private int escapePointY = 0;
    private List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)

    public int[] escape(GameView gv, int srcX, int srcY, int currentDirection) {
        int direction = currentDirection;
        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {

            if(escapePointX == 0 && escapePointY == 0 || (escapePointX * gv.getBlockSize() == srcX && escapePointY * gv.getBlockSize() == srcY  )){
                int[] randomEscapePoint = gv.generateMapSpawn();
                escapePointY =  randomEscapePoint[0];
                escapePointX = randomEscapePoint[1];
                AStar astar = new AStar(gv,srcX / gv.getBlockSize(),srcY / gv.getBlockSize());
                path = astar.findPathTo(escapePointX,escapePointY);
            }

            direction = Behaviour.getDirection(path);
        }

        return Behaviour.getNextPosition(gv, direction, srcX, srcY);
    }

}