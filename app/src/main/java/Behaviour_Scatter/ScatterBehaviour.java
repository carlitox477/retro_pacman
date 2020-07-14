package Behaviour_Scatter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Behaviour.Behaviour;
import com.example.pacman.GameView;

import java.util.List;

import path.AStar;
import path.Node;

public abstract class ScatterBehaviour {
    protected int[] cornerDirections;
    protected boolean inCorner = false;
    protected int step = 0;
    protected int matchX;
    protected int matchY;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] scatter(GameView gv, int srcX, int srcY, int currentDirection) {
        int direction = currentDirection;

        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {

            if(srcX  / gv.getBlockSize()  == matchX && srcY  / gv.getBlockSize() == matchY ){
                inCorner = true;
            }

            if(!inCorner) {
                AStar aStar = new AStar(gv, srcX / gv.getBlockSize(), srcY / gv.getBlockSize());
                List<Node> path = aStar.findPathTo(matchX, matchY);
                direction = Behaviour.getDirection(path);
            }else{
                direction = cornerDirections[step];
                step++;
                if(step == cornerDirections.length - 1)
                    step = 0;
            }
        }

        return Behaviour.getNextPosition(gv, srcX, srcY, direction);
    }
}

