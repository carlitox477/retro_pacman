package Game;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import com.example.pacman.R;

import java.util.concurrent.Semaphore;

import Game.Behavior.ChaseBehavior.*;
import Game.Character_package.Ghost;
import Game.Character_package.Pacman;
import Game.GameCountDown.*;


public class GameManager {
    private static final int TOTAL_LEVELS=256;
    private static int SCORE=0;
    private GameMap gameMap;
    private int level,bonusResetTime;//,score;
    private CountDownScareGhosts scareCountDown;
    private Pacman pacman;
    private Ghost[] ghosts;
    private boolean fruitHasBeenInTheLevel;
    private static char GAME_STATE; //playing or in pause, w win, l lost
    private static Semaphore CHANGE_SCORE_MUTEX;

    public GameManager(){
        this.fruitHasBeenInTheLevel=false;
        //this.score=0;
        this.gameMap=new GameMap();
        this.gameMap.loadMap1();
        this.level=1;
        this.ghosts=new Ghost[4];
        this.bonusResetTime = 5000;
        this.scareCountDown=null;
        GAME_STATE='p';
    }

    public char getGameState() {
        return GAME_STATE;
    }

    public void setChangeScoreSemaphore(Semaphore changeScoreSemaphore) {
        CHANGE_SCORE_MUTEX = changeScoreSemaphore;
        //if(this.changeScoreSemaphore==null){
        //    Log.i("Change Score Semaphore","I'm null");
        //}else{
        //    Log.i("Change Score Semaphore","I'm not null");
        //}
    }

    public void addScore(int s){
        //this.score+=s;
        SCORE+=s;
        CHANGE_SCORE_MUTEX.release();
        /*if(this.changeScoreSemaphore==null){
            Log.i("Change Score Semaphore","I'm null");
        }else{
            Log.i("Change Score Semaphore","I'm not null");
        }*/
        //this.changeScoreSemaphore.release();
    }

    public int getScore() {
        return SCORE;
        //return this.score;
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
    public Pacman getPacman(){
        return this.pacman;
    }
    public void setPacman(Pacman pacman){
        this.pacman=pacman;
    }


    public void eatPallet(int posXMap, int posYMap){
        SCORE+=10;
        CHANGE_SCORE_MUTEX.release();
        //this.score+=10;
        //Log.i("Score GM", ""+SCORE);
        //Log.i("Score GM", ""+this.score);
        this.gameMap.getMap()[posYMap][posXMap]=0;
        //this.changeScoreSemaphore.release();
        //if(this.changeScoreSemaphore==null){
        //    Log.i("Change Score Semaphore","I'm null");
        //}else{
        //    Log.i("Change Score Semaphore","I'm not null");
        //}
    }

    public void eatBonus(int posXMap,int posYMap){
        SCORE+=500;
        CHANGE_SCORE_MUTEX.release();
        //this.score+=500;
        //Log.i("Score", Double.toString(this.score).substring(0,Double.toString(this.score).indexOf('.')));
        this.gameMap.getMap()[posYMap][posXMap]=0;
        //this.changeScoreSemaphore.release();
    }

    public void eatSuperPallet(int posXMap,int posYMap){
        SCORE+=50;
        CHANGE_SCORE_MUTEX.release();
        //this.score+=50;
        this.gameMap.getMap()[posYMap][posXMap]=0;

        //Si hay un timer andando lo cancelo y ejecuto otro
        if (this.scareCountDown != null){
            this.scareCountDown.cancel();
        }
        this.scareCountDown = new CountDownScareGhosts(this.ghosts,this.gameMap.getMap());
        this.scareCountDown.start();
        //this.changeScoreSemaphore.release();
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

    public synchronized void initGhosts(int blocksize, Resources res, String packageName,int movementFluency) {
        int[][]spawnPositions,cornersPositions, notUpDownPositions,defaultTargets;

        defaultTargets=this.gameMap.getDefaultGhostTarget();
        notUpDownPositions=this.gameMap.getNotUpDownDecisionPositions();
        spawnPositions=this.gameMap.getGhostsSpawnPositions();
        cornersPositions=this.gameMap.getGhostsScatterTarget();
        //start position
        // 5 blinky spawn [13, 11]
        // 6 pinky spawn [15,11]
        // 7 inky spawn [13,16]
        // 8 clyde spawn [15,16]
        this.ghosts=new Ghost[4];
        ghosts[0] = new Ghost("blinky",spawnPositions[0], cornersPositions[0] ,new BehaviorChaseAgressive(notUpDownPositions,movementFluency,defaultTargets[0]),movementFluency,notUpDownPositions,'l',defaultTargets[0],blocksize,res,packageName);
        ghosts[1] = new Ghost("pinky",spawnPositions[1],cornersPositions[1],new BehaviorChaseAmbush(notUpDownPositions,movementFluency,defaultTargets[1]),movementFluency,notUpDownPositions,'r',defaultTargets[1],blocksize,res,packageName);
        ghosts[2] = new Ghost("inky",spawnPositions[2],cornersPositions[2],new BehaviorChasePatrol(notUpDownPositions,this.ghosts[0],movementFluency,defaultTargets[0]),movementFluency,notUpDownPositions,'l',defaultTargets[0],blocksize,res,packageName);
        ghosts[3] = new Ghost("clyde",spawnPositions[3],cornersPositions[3],new BehaviorChaseRandom(notUpDownPositions,cornersPositions[3],movementFluency,defaultTargets[1]),movementFluency,notUpDownPositions,'r',defaultTargets[1],blocksize,res,packageName);

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

    public int onResume(){
        int musicID;
        for (int i=0 ; i<this.ghosts.length;i++){
            if(this.ghosts[i]!=null){
                Log.i("PAUSE","resume "+i);
                this.ghosts[i].onResume();
            }
        }
        if(this.scareCountDown!=null && !this.scareCountDown.hasEnded()){
            this.scareCountDown.onResume();
            musicID=R.raw.pacman_power_siren;
        }else{
            Log.i("Media player", "Created");
            musicID =R.raw.pacman_siren;
        }
        return musicID;
    }

    public void onPause(){
        for (int i=0 ; i<this.ghosts.length;i++){
            if(this.ghosts[i]!=null){
                Log.i("PAUSE","ghost "+i);
                this.ghosts[i].onPause();
            }
        }
        if(this.scareCountDown!=null && !this.scareCountDown.hasEnded()){
            this.scareCountDown=this.scareCountDown.onPause();
        }
    }

    public boolean isScareCountDownRunning(){
        return this.scareCountDown!=null && !this.scareCountDown.hasEnded();
    }

    public void cancelThreads(){
        for (int i=0 ; i<this.ghosts.length;i++){
            this.ghosts[i].cancelBehavoirThread();
        }
        if(this.scareCountDown!=null){
            this.scareCountDown.cancel();
        }
    }

}
