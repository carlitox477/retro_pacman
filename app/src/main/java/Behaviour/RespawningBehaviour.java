package Behaviour;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.pacman.GameView;

import java.util.List;

import path.AStar;
import path.Node;

public class RespawningBehaviour extends Behaviour{

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] respawn(GameView gv, int srcX, int srcY, int currentDirection) {
        //Going to respawn
        int direction,blocksize;
        List<Node> path;
        AStar aStar;

        direction = currentDirection;
        blocksize=gv.getBlockSize();
        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {
            aStar = new AStar(gv, srcX / blocksize,srcY/ blocksize);
            path = aStar.findPathTo(9,9);
            direction = super.getDirection(path);

        }
        return super.getNextPosition(blocksize, direction, srcX, srcY);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] behave(GameView gv, int srcX, int srcY, int currentDirection) {
        //Going to respawn
        int direction,blocksize;
        List<Node> path;
        AStar aStar;

        direction = currentDirection;
        blocksize=gv.getBlockSize();
        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {
            aStar = new AStar(gv, srcX / blocksize,srcY/ blocksize);
            path = aStar.findPathTo(9,9);
            direction = super.getDirection(path);

        }
        return super.getNextPosition(blocksize, direction, srcX, srcY);
    }

    @Override
    public boolean isRespawning() {
        return true;
    }
}
