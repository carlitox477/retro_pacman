package Character_package;

import android.graphics.Canvas;
import android.util.Log;

import com.example.pacman.Draw;
import com.example.pacman.GameView;
import com.example.pacman.Ghost;

public class Pacman extends Character{
    private int movementFluencyLevel;
    private char nextDirection, viewDirection;

    public Pacman(String characterName, String prefix, GameView gv, int movementFluencyLevel, int blocksize) {
        super(characterName,prefix,gv,4,blocksize);
        this.movementFluencyLevel=movementFluencyLevel;
    }

    public int[] getNextPositionScreen(){
        int destX,destY;
        switch (this.currentDirection){
            case 0:
                //destX = xPosPacman + blockSize / 2;
                destX=this.currentPositionScreen[0] + this.blocksize / 2;
                //destY = yPosPacman - 2 * blockSize - blockSize / 2;
                destY=this.currentPositionScreen[1] - (int)(2.5*this.blocksize);
                break;
            case 1:
                //destX = xPosPacman + 3 * blockSize + blockSize / 2;
                destX= this.currentPositionScreen[0]=(int)(3.5*this.blocksize);
                //destY = yPosPacman + blockSize / 2;
                destY=this.currentPositionScreen[1]+this.blocksize / 2;
                break;
            case 2:
                //destX = xPosPacman + blockSize / 2;
                destX=this.currentPositionScreen[0]+this.blocksize/2;
                //destY = yPosPacman + 3 * blockSize + blockSize / 2;
                destY=this.currentPositionScreen[1]+(int)(3.5*this.blocksize);
                break;
            case 3:
                //destX = xPosPacman - 2 * blockSize - blockSize / 2;
                destX=this.currentPositionScreen[0]-(int)(2.5*this.blocksize);
                //destY = yPosPacman + blockSize / 2;
                destY=this.currentPositionScreen[1];
                break;
            default:
                //destX = xPosPacman;
                destX=this.currentPositionScreen[0];
                //destY = yPosPacman;
                destY=this.currentPositionScreen[1];
                break;
        }
    return new int[]{destX,destY};
    }

    public void setNextDirection(char nextDirection){
        this.nextDirection=nextDirection;
    }

    public void move(int[][] map, Ghost[] ghosts, GameView gv, Canvas canvas){
        int posXMap, posYMap;
        posXMap=this.currentPositionScreen[0]/this.blocksize;
        posYMap=this.currentPositionScreen[1]/this.blocksize;

        this.usePortal(map[0].length);
        this.eatGhosts(ghosts);

        if(this.currentPositionScreen[0]%this.blocksize==0&&this.currentPositionScreen[1]%this.blocksize==0){
            //New map position
            switch (map[posYMap][posXMap]){
                case 2:
                    gv.eatPallet(posXMap,posYMap);
                    break;
                case 3:
                    gv.eatSuperPallet(posXMap,posYMap);
                    break;
                case 9:
                    gv.eatBonus(posXMap,posYMap);
                    break;
                default:
                    break;
            }

        }
        gv.tryCreateBonus();
        this.changeDirection(posXMap,posYMap,gv.getGameMap().getMap());
        if (this.currentPositionScreen[0] < 0) {
            //if we move previously and the position is out of range
            this.currentPositionScreen[0]= this.blocksize * map[0].length;
        }
        Draw.drawPacman(this,canvas,this.viewDirection);
        this.changePosition(this.currentDirection);
    }


    private void changePosition(char direction){
        switch (direction){
            case 'u':
                this.currentPositionScreen[1] -= movementFluencyLevel;
                break;
            case 'd':
                this.currentPositionScreen[1]+= movementFluencyLevel;
                break;
            case 'r':
                this.currentPositionScreen[0] += movementFluencyLevel;
                break;
            case 'l':
                this.currentPositionScreen[0] -= movementFluencyLevel;
                break;
            default:
                break;
        }
    }

    private void usePortal(int mapLength){
        int posXScreen, limitWidth;
        posXScreen=this.currentPositionScreen[0]/this.blocksize;
        limitWidth=mapLength*this.blocksize;


        if (posXScreen==-1) {
            //to use left portal
            this.currentPositionScreen[0]=(limitWidth-this.blocksize)-this.movementFluencyLevel;
        }else if(posXScreen==limitWidth){
            this.currentPositionScreen[0]=0;
        }
        Log.i("Pacman position","["+this.currentPositionScreen[0]/blocksize+", "+this.currentPositionScreen[1]/blocksize+"]");
    }

    private void eatGhosts(Ghost[] ghosts){
        //check if ghosts should respawn
        //¿Por que 5?
        boolean shouldRespawn;
        for (int i = 0; i < ghosts.length; i++) {
            shouldRespawn=ghosts[i].getState().isFrightened() &&
                    (Math.abs(this.currentPositionScreen[0]) <= ghosts[i].getxPos() + 5) &&
                    (Math.abs(this.currentPositionScreen[1]) <= ghosts[i].getyPos() + 5) &&
                    (Math.abs(this.currentPositionScreen[0]) >= ghosts[i].getxPos() - 5) &&
                    (Math.abs(this.currentPositionScreen[1]) >= ghosts[i].getyPos() - 5);
            if (shouldRespawn){
                ghosts[i].setRespawnBehaviour();
            }
        }
    }

    private void changeDirection(int posXinMap,int posYinMap, int[][] map){
        if (posXinMap > 0 && posXinMap < map[0].length * this.blocksize) {
            //Log.i("Move pacman","try");
             if (!((this.nextDirection == 'l' && (map[posYinMap][(posXinMap - 1)]) == 1) || //check if it is a wall
                    (this.nextDirection == 'r' && (map[posYinMap][(posXinMap + 1)%map[1].length]) == 1) || //check if it is a wall
                    (this.nextDirection == 'u' && (map[posYinMap - 1][posXinMap]) == 1) || //check if it is a wall
                    (this.nextDirection == 'd' && (map[posYinMap + 1][posXinMap] == 1 || map[posYinMap + 1][posXinMap]==10) //check if it is a wall or the door of the ghost spawn point
                    ))) {
                this.currentDirection=this.nextDirection;
                this.viewDirection = this.nextDirection;
            }else{
                 this.currentDirection = ' ';
            }

        }
    }


}
