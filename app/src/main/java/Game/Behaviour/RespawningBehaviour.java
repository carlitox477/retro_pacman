package Game.Behaviour;

import android.os.Build;
import androidx.annotation.RequiresApi;
import Game.GameManager;
import java.util.List;
import Game.Path.AStar;
import Game.Path.Node;

public class RespawningBehaviour extends Behaviour{

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int[] behave(GameManager gameManager, int blocksize, int srcX, int srcY, int currentDirection) {
        //Going to respawn
        int direction;
        int[][]map;
        List<Node> path;
        AStar aStar;

        map=gameManager.getGameMap().getMap();

        direction = currentDirection;
        if ((srcX % blocksize == 0) && (srcY % blocksize == 0)) {
            //ver aca
            aStar = new AStar(map, srcX / blocksize,srcY/ blocksize);
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
