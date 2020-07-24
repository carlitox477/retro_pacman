package Behaviour;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Behaviour.Behaviour;
import com.example.pacman.GameView;

import java.util.List;

import path.AStar;
import path.Node;

public class ScatterBehaviour extends Behaviour{
    protected int[] targetPosition;
    protected int step;
    protected int targetX;
    protected int targetY;

    public ScatterBehaviour(int[]targetPosition){
        super();
        this.targetPosition=targetPosition;
        this.step = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] behave(GameView gv, int srcX, int srcY, int currentDirection) {
        //scatter
        int direction,blocksize, posXmap,posYmap;
        List<Node> path;
        AStar aStar;

        direction=currentDirection;
        blocksize=gv.getBlockSize();

        if ((srcX % blocksize == 0) && (srcY % blocksize == 0)) {
            //Si el resto es 0 tengo una posición especifica del mapa
            posXmap=srcX  / blocksize;
            posYmap=srcY  / blocksize;

            aStar = new AStar(gv, posXmap, posYmap);
            path = aStar.findPathTo(targetX, targetY);
            direction = super.getDirection(path);
        }

        return super.getNextPosition(blocksize, srcX, srcY, direction);
    }

    @Override
    public boolean isScattering() {
        return true;
    }
}

