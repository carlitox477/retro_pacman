package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Behaviour.Behaviour;
import com.example.pacman.GameView;
import path.*;

import java.util.List;

public class ChaseAgressive implements ChaseBehaviour {
    public List<Node> path;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] chase(GameView gv, int srcX, int srcY, int currentDirection) {
        int direction;
        direction=defineDirection(gv, srcX,srcY,currentDirection);
        return Behaviour.getNextPosition(gv,direction,srcX,srcY);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int defineDirection(GameView gv, int srcX, int srcY, int currentDirection){
        List<Node> newPath;
        AStar aStar;
        int direction;

        direction=currentDirection;

        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {
            aStar = new AStar(gv, srcX / gv.getBlockSize(),srcY/ gv.getBlockSize());
            newPath = aStar.findPathTo(gv.getxPosPacman() / gv.getBlockSize(),gv.getyPosPacman() / gv.getBlockSize());

            if(newPath == null)
                direction = Behaviour.getDirection(path);
            else
                direction = Behaviour.getDirection(newPath);

        }
        return direction;
    }

}


