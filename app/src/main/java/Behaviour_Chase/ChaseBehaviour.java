package Behaviour_Chase;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.pacman.GameView;

import Behaviour.Behaviour;

public abstract class ChaseBehaviour extends Behaviour {
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected int[] chase(GameView gv, int srcX, int srcY, int currentDirection){
        int direction,blocksize;
        blocksize=gv.getBlockSize();
        direction=defineDirection(gv, srcX,srcY,currentDirection);
        return super.getNextPosition(blocksize,direction,srcX,srcY);
        }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected abstract int defineDirection(GameView gv, int srcX, int srcY, int currentDirection);

    @Override
    public boolean isChasing() {
        return true;
    }
}
