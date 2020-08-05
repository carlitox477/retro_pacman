package Game.Character_package;

import android.graphics.Canvas;
import android.util.Log;

import Game.GameManager;
import Game.GameView;

public class Pacman extends Character {
    private char nextDirection;
    protected int movementFluencyLevel;

    public Pacman(String characterName, String prefix, GameView gv, int movementFluencyLevel) {
        super(characterName,prefix,gv,4,gv.getGameManager().getGameMap().getPacmanSpawnPosition());
        this.movementFluencyLevel=movementFluencyLevel;
    }

    public int[] getNextPositionScreen(){
        int destX,destY;
        switch (this.currentDirection){
            case 'u':
                destX=this.currentPositionScreen[0] + this.blocksize / 2;
                destY=this.currentPositionScreen[1] - (int)(2.5*this.blocksize);
                break;
            case 'r':
                destX= this.currentPositionScreen[0]=(int)(3.5*this.blocksize);
                destY=this.currentPositionScreen[1]+this.blocksize / 2;
                break;
            case 'd':
                destX=this.currentPositionScreen[0]+this.blocksize/2;
                destY=this.currentPositionScreen[1]+(int)(3.5*this.blocksize);
                break;
            case 'l':
                destX=this.currentPositionScreen[0]-(int)(2.5*this.blocksize);
                destY=this.currentPositionScreen[1];
                break;
            default:
                destX=this.currentPositionScreen[0];
                destY=this.currentPositionScreen[1];
                break;
        }
    return new int[]{destX,destY};
    }

    public void setNextDirection(char nextDirection){
        this.nextDirection=nextDirection;
    }

    public void move(GameManager gm, Canvas canvas){
        int posXMap, posYMap;
        Ghost[] ghosts;
        int[][]map;

        map=gm.getGameMap().getMap();
        ghosts=gm.getGhosts();

        this.usePortal(map[0].length,this.movementFluencyLevel);
        posXMap=this.currentPositionScreen[0]/this.blocksize;
        posYMap=this.currentPositionScreen[1]/this.blocksize;
        //this.eatGhosts(ghosts);

        if(this.currentPositionScreen[0]%this.blocksize==0&&this.currentPositionScreen[1]%this.blocksize==0){
            //New map position
            switch (map[posYMap][posXMap]){
                case 2:
                    gm.eatPallet(posXMap,posYMap);
                    break;
                case 3:
                    gm.eatSuperPallet(posXMap,posYMap);
                    break;
                case 9:
                    gm.eatBonus(posXMap,posYMap);
                    break;
                default:
                    break;
            }
            gm.tryCreateBonus();
            this.changeDirection(posXMap,posYMap,gm.getGameMap().getMap());
        }
        if (this.currentPositionScreen[0] < 0) {
            //if we move previously and the position is out of range
            this.currentPositionScreen[0]= this.blocksize * map[0].length;
        }
        this.changePositionScreen(this.currentDirection,this.movementFluencyLevel);
        this.draw(canvas);
    }


    private void eatGhosts(Ghost[] ghosts){
        //check if ghosts should respawn
        //¿Por que 5?
        boolean shouldRespawn;
        for (int i = 0; i < ghosts.length; i++) {
            shouldRespawn=ghosts[i].getState().isFrightened() &&
                    (Math.abs(this.currentPositionScreen[0]) <= ghosts[i].getPositionScreenX() + 5) &&
                    (Math.abs(this.currentPositionScreen[1]) <= ghosts[i].getPositionScreenY() + 5) &&
                    (Math.abs(this.currentPositionScreen[0]) >= ghosts[i].getPositionScreenX() - 5) &&
                    (Math.abs(this.currentPositionScreen[1]) >= ghosts[i].getPositionScreenY() - 5);
            if (shouldRespawn){
                ghosts[i].setRespawnBehaviour();
            }
        }
    }

    private void changeDirection(int posXinMap,int posYinMap, int[][] map){
        if (posXinMap > 0 && posXinMap <= map[0].length) {
            //Log.i("Move pacman","try");
             if (!((this.nextDirection == 'l' && (map[posYinMap][(posXinMap - 1)]) == 1) || //check if it is a wall
                    (this.nextDirection == 'r' && (map[posYinMap][(posXinMap + 1)%map[1].length]) == 1) || //check if it is a wall
                    (this.nextDirection == 'u' && (map[posYinMap - 1][posXinMap]) == 1) || //check if it is a wall
                    (this.nextDirection == 'd' && (map[posYinMap + 1][posXinMap] == 1 || map[posYinMap + 1][posXinMap]==10) //check if it is a wall or the door of the ghost spawn point
                    ))) {
                this.currentDirection=this.nextDirection;
            }else if (((this.currentDirection == 'l' && (map[posYinMap][(posXinMap - 1)]) == 1) || //check if it is a wall
                     (this.currentDirection == 'r' && (map[posYinMap][(posXinMap + 1)%map[1].length]) == 1) || //check if it is a wall
                     (this.currentDirection == 'u' && (map[posYinMap - 1][posXinMap]) == 1) || //check if it is a wall
                     (this.currentDirection == 'd' && (map[posYinMap + 1][posXinMap] == 1 || map[posYinMap + 1][posXinMap]==10) //check if it is a wall or the door of the ghost spawn point
                     ))){
                 //Para que el pacman no se cambie de posicion
                 this.currentDirection = ' ';
            }

        }
    }

}
