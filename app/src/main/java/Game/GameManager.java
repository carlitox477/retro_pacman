package Game;

import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import Game.Behaviour.Chase.*;
import Game.Character_package.Ghost;
import Game.Character_package.Pacman;
import Game.GameCountDown.*;


public class GameManager {
    private static final int TOTAL_LEVELS=256;
    private GameMap gameMap;
    private int level,bonusResetTime,score;
    private CountdownGhostsState stateCounter;
    private Pacman pacman;
    private Ghost[] ghosts;
    boolean fruitHasBeenInTheLevel;

    public GameManager(){
        this.fruitHasBeenInTheLevel=false;
        this.score=0;
        this.gameMap=new GameMap();
        this.gameMap.loadMap1();
        this.level=1;
        this.ghosts=new Ghost[4];
        this.bonusResetTime = 5000;
    }

    public int getScore() {
        return this.score;
    }
    public int getLevel() {
        return this.level;
    }
    public GameMap getGameMap() {
        return this.gameMap;
    }
    public Ghost[] getGhosts(){
        return this.ghosts;
    }
    public Ghost getGhost(int i) {
        return this.ghosts[i];
    }
    public Pacman getPacman(){
        return this.pacman;
    }
    public void setPacman(Pacman pacman){
        this.pacman=pacman;
    }


    public void eatPallet(int posXMap, int posYMap){
        this.score+=10;
        Log.i("Score", Double.toString(this.score).substring(0,Double.toString(this.score).indexOf('.')));
        this.gameMap.getMap()[posYMap][posXMap]=0;
    }

    public void eatBonus(int posXMap,int posYMap){
        this.score+=500;
        Log.i("Score", Double.toString(this.score).substring(0,Double.toString(this.score).indexOf('.')));
        this.gameMap.getMap()[posYMap][posXMap]=0;
    }

    public void eatSuperPallet(int posXMap,int posYMap){
        this.score+=50;
        Log.i("Score", Double.toString(this.score).substring(0,Double.toString(this.score).indexOf('.')));
        this.gameMap.getMap()[posYMap][posXMap]=0;

        //Si hay un timer andando lo cancelo y ejecuto otro
        /*if (stateCounter != null)
            stateCounter.cancelTimer();

        stateCounter = new CountdownGhostsState(this, 2);
        stateCounter.start();*/
    }

    public void tryCreateBonus(){
        //only if pacman has eaten 20 pallets we should allow the fruit appear
        if(!this.fruitHasBeenInTheLevel && this.gameMap.getEatenPallets()>=20){
            //to not allow the fruit be again in the level
            this.fruitHasBeenInTheLevel=true;
            new CountdownBonusThread(this.gameMap,this.bonusResetTime).start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void moveGhosts(int blocksize) {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].move(this, blocksize);
        }
    }

    public void initGhosts(GameView gv) {
        int[][]spawnPositions;

        spawnPositions=this.gameMap.getGhostsSpawnPositions();
        //start position
        // 5 blinky spawn [13, 11]
        // 6 pinky spawn [15,11]
        // 7 inky spawn [13,16]
        // 8 clyde spawn [15,16]
        ghosts[0] = new Ghost("blinky",gv,spawnPositions[0], new int[]{0, this.gameMap.getMapWidth()-1},new ChaseAgressive());
        ghosts[1] = new Ghost("pinky",gv,spawnPositions[1],new int[]{0,0},new ChaseAmbush());
        ghosts[2] = new Ghost("inky",gv,spawnPositions[2],new int[]{this.gameMap.getMapHeight()-1,0},new ChasePatrol());
        ghosts[3] = new Ghost("clyde",gv,spawnPositions[3],new int[]{this.gameMap.getMapHeight()-1,this.gameMap.getMapWidth()-1},new ChaseRandom());
        stateCounter = new CountdownGhostsState(this.ghosts, 0);
        stateCounter.start();
    }

    public void checkWinLevel(Canvas c, int blocksize) throws InterruptedException {
        //player win the level if he has eaten all the pallet
        if(this.gameMap.countPallets()==0){
            Log.i("Game","WIN");
            this.level++;
            if(this.level<=TOTAL_LEVELS){
                this.gameMap.passLevelAnimation(c,blocksize,this.pacman,level);
                this.gameMap.resetMap();
                //if it isn't the final level reboot
            }else{
                //Start new level
            }

        }
    }

}
