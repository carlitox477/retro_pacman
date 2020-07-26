package Game.Behaviour.Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import Game.GameManager;

import Game.Behaviour.Behaviour;

public abstract class ChaseBehaviour extends Behaviour {
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected int[] chase(GameManager gameManager, int blocksize, int srcX, int srcY, int currentDirection){
        int direction;
        direction=defineDirection(gameManager,blocksize, srcX,srcY,currentDirection);
        return super.getNextPosition(blocksize,direction,srcX,srcY);
        }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected abstract int defineDirection(GameManager gameManager,int blocksize, int srcX, int srcY, int currentDirection);

    @Override
    public boolean isChasing() {
        return true;
    }
}
