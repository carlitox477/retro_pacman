package Game;

import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import Game.Behavior.ChaseBehavior.*;
import Game.Character_package.Ghost;
import Game.Character_package.Pacman;
import Game.GameCountDown.*;


public class GameManager {
    private static final int TOTAL_LEVELS=256;
    private GameMap gameMap;
    private int level,bonusResetTime,score;
    private CountDownScareGhosts scareCounter;
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
        this.scareCounter=null;
    }

    public void addScore(int s){
        this.score+=s;
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
        //Log.i("Score", Double.toString(this.score).substring(0,Double.toString(this.score).indexOf('.')));
        this.gameMap.getMap()[posYMap][posXMap]=0;
    }

    public void eatBonus(int posXMap,int posYMap){
        this.score+=500;
        //Log.i("Score", Double.toString(this.score).substring(0,Double.toString(this.score).indexOf('.')));
        this.gameMap.getMap()[posYMap][posXMap]=0;
    }

    public void eatSuperPallet(int posXMap,int posYMap){
        this.score+=50;
        Log.i("Score", Double.toString(this.score).substring(0,Double.toString(this.score).indexOf('.')));
        this.gameMap.getMap()[posYMap][posXMap]=0;

        //Si hay un timer andando lo cancelo y ejecuto otro
        if (this.scareCounter != null){
            this.scareCounter.cancel();
        }
        this.scareCounter = new CountDownScareGhosts(this.ghosts,this.gameMap.getMap());
        this.scareCounter.start();
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
    public void moveGhosts(Canvas canvas,int blocksize) {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].move(this.gameMap.getMap(),this.pacman);
            ghosts[i].draw(canvas);
        }
    }

    public synchronized void initGhosts(@NotNull GameView gv) {
        int[][]spawnPositions,cornersPositions, notUpDownPositions,defaultTargets;
        int movementeFluency;

        defaultTargets=this.gameMap.getDefaultGhostTarget();
        notUpDownPositions=this.gameMap.getNotUpDownDecisionPositions();
        spawnPositions=this.gameMap.getGhostsSpawnPositions();
        cornersPositions=this.gameMap.getGhostsScatterTarget();
        movementeFluency=gv.getMovementFluencyLevel();
        //start position
        // 5 blinky spawn [13, 11]
        // 6 pinky spawn [15,11]
        // 7 inky spawn [13,16]
        // 8 clyde spawn [15,16]
        this.ghosts=new Ghost[4];
        ghosts[0] = new Ghost("blinky",gv,spawnPositions[0], cornersPositions[0] ,new BehaviorChaseAgressive(notUpDownPositions,movementeFluency,defaultTargets[0]),movementeFluency,notUpDownPositions,'l',defaultTargets[0]);
        ghosts[1] = new Ghost("pinky",gv,spawnPositions[1],cornersPositions[1],new BehaviorChaseAmbush(notUpDownPositions,movementeFluency,defaultTargets[1]),movementeFluency,notUpDownPositions,'r',defaultTargets[1]);
        ghosts[2] = new Ghost("inky",gv,spawnPositions[2],cornersPositions[2],new BehaviorChasePatrol(notUpDownPositions,this.ghosts[0],movementeFluency,defaultTargets[0]),movementeFluency,notUpDownPositions,'l',defaultTargets[0]);
        ghosts[3] = new Ghost("clyde",gv,spawnPositions[3],cornersPositions[3],new BehaviorChaseRandom(notUpDownPositions,cornersPositions[3],movementeFluency,defaultTargets[1]),movementeFluency,notUpDownPositions,'r',defaultTargets[1]);

        try{
            Thread.sleep(200);
        }catch(Exception e){}

        for (int i=0;i<ghosts.length;i++){
            ghosts[i].onLevelStart(1);
        }

    }

    public boolean checkWinLevel() {
        //player win the level if he has eaten all the pallet
        return this.gameMap.countPallets()==0;
    }

    public void cancelThreads(){
        for (int i=0 ; i<this.ghosts.length;i++){
            this.ghosts[i].cancelBehavoirThread();
        }
        if(this.scareCounter!=null){
            this.scareCounter.cancel();
        }
    }

}
