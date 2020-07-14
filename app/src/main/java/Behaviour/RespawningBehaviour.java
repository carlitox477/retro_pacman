package Behaviour;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.pacman.GameView;

import java.util.List;

import path.AStar;
import path.Node;

public class RespawningBehaviour {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] respawn(GameView gv, int srcX, int srcY, int currentDirection) {
        int direction = currentDirection;
        if ((srcX % gv.getBlockSize() == 0) && (srcY % gv.getBlockSize() == 0)) {

            AStar aStar = new AStar(gv, srcX / gv.getBlockSize(),srcY/ gv.getBlockSize());
            List<Node> path = aStar.findPathTo(9,9);
            direction = Behaviour.getDirection(path);

        }

        return Behaviour.getNextPosition(gv, direction, srcX, srcY);
    }

}
