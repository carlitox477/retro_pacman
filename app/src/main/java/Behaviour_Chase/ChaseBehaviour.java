package Behaviour_Chase;

import com.example.pacman.GameView;

public interface ChaseBehaviour {
    public int[] chase(GameView gv, int srcX, int srcY, int currentDirection);
    public int defineDirection(GameView gv, int srcX, int srcY, int currentDirection);

}
