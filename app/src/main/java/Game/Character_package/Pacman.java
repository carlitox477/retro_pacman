package Game.Character_package;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Semaphore;

import Game.GameManager;

public class Pacman extends Character {
    private char nextDirection;
    private int lives;
    private int movementFluencyLevel;
    private static Semaphore CHANGE_DIRECTION_MUTEX;

    public Pacman(String characterName, String prefix, int movementFluencyLevel, int[]spawnPosition, int blocksize, Resources res, String packageName) {
        super(characterName,prefix,4,spawnPosition, blocksize, res, packageName);
        this.lives=3;
        this.movementFluencyLevel=movementFluencyLevel;
    }

    public void setNextDirection(char nextDirection){
        this.nextDirection=nextDirection;
    }
    public void setChangeDirectionSemaphore(Semaphore changeDirectionSemaphore){
        CHANGE_DIRECTION_MUTEX=changeDirectionSemaphore;
    }

    public boolean move(@NotNull GameManager gm, Canvas canvas){
        int posXMap, posYMap;
        Ghost[] ghosts;
        int[][]map;
        boolean shouldRespawn;

        map=gm.getGameMap().getMap();
        ghosts=gm.getGhosts();

        this.usePortal(map[0].length,this.movementFluencyLevel);
        posXMap=this.currentPositionScreen[0]/this.blocksize;
        posYMap=this.currentPositionScreen[1]/this.blocksize;
        shouldRespawn=!this.successTryingToEatGhosts(ghosts,gm);

        if(shouldRespawn){
            Log.i("Pacman","it was eaten");
            this.lives--;
            this.respawn(ghosts);
        }else{
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
        return shouldRespawn;
    }

    public boolean hasLifes(){
        return this.lives>0;
    }


    private boolean successTryingToEatGhosts(Ghost[] ghosts,GameManager gm){
        //check if ghosts should respawn
        //¿Why 5?
        boolean ghostShouldRespawn,pacmanShouldNotRespawn;
        int i,score;

        i=0;
        score=200;

        do{
            ghostShouldRespawn=ghosts[i].getState().isFrightened() &&
                    (this.currentPositionScreen[0] <= ghosts[i].getPositionScreenX() + this.blocksize/4) &&
                    (this.currentPositionScreen[1] <= ghosts[i].getPositionScreenY() + this.blocksize/4) &&
                    (this.currentPositionScreen[0] >= ghosts[i].getPositionScreenX() - this.blocksize/4) &&
                    (this.currentPositionScreen[1] >= ghosts[i].getPositionScreenY() - this.blocksize/4);
            pacmanShouldNotRespawn=ghosts[i].getState().isAttacking() &&
                    (this.currentPositionScreen[0] <= ghosts[i].getPositionScreenX() + this.blocksize/4) &&
                    (this.currentPositionScreen[1] <= ghosts[i].getPositionScreenY() + this.blocksize/4) &&
                    (this.currentPositionScreen[0] >= ghosts[i].getPositionScreenX() - this.blocksize/4) &&
                    (this.currentPositionScreen[1] >= ghosts[i].getPositionScreenY() - this.blocksize/4);
            if (ghostShouldRespawn){
                ghosts[i].setRespawnBehaviour();
                gm.addScore(score);
                score*=2;
            }
            i++;

        }while(i<ghosts.length && !pacmanShouldNotRespawn);

        return !pacmanShouldNotRespawn;
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
                //this.changeDirectionSemaphore.release();
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

    public void respawn(Ghost[] ghosts){
        this.respawn();
        for(int i=0; i<ghosts.length;i++){
            ghosts[i].respawn();
        }
    }

}
