package Game.Behaviour;

import android.os.Build;
import androidx.annotation.RequiresApi;
import Game.GameManager;
import java.util.List;
import Game.Path.AStar;
import Game.Path.Node;

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
    public int[] behave(GameManager gameManager, int blocksize, int srcX, int srcY, int currentDirection) {
        //scatter
        int direction, posXmap,posYmap;
        List<Node> path;
        AStar aStar;
        int[][]map;

        direction=currentDirection;
        map=gameManager.getGameMap().getMap();

        if ((srcX % blocksize == 0) && (srcY % blocksize == 0)) {
            //Si el resto es 0 tengo una posición especifica del mapa
            posXmap=srcX  / blocksize;
            posYmap=srcY  / blocksize;

            aStar = new AStar(map, posXmap, posYmap);
            path = aStar.findPathTo(targetX, targetY);
            direction = super.getDirection(path);
        }

        return super.getNextPosition(blocksize, srcX, srcY, direction);
    }

    @Override
    protected int[] getTarget(GameManager gameManager) {
        return new int[0];
    }

    @Override
    public boolean isScattering() {
        return true;
    }
}

