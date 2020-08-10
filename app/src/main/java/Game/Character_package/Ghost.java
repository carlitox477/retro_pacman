package Game.Character_package;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Semaphore;

import Game.Behavior.*;
import Game.Behavior.ChaseBehavior.*;
import Game.GameCountDown.CountDownGhost;
import Game.GameView;



public class Ghost extends Character {
    private static Bitmap[] vulnerableGhostBitmap,respawningGhostBitmap;
    private Behavior currentBehaviour;

    private BehaviorChase behaviorChaseBehaviour; //set bitmap
    private BehaviorFrighten frightenedBehaviour;
    private BehaviorScatter scatterBehaviour;
    private BehaviorRespawn respawningBehaviour;
    private CountDownGhost countdownGhost;

    public Ghost(String name,GameView gv,int[]respawnPosition,int[]scatterTarget, BehaviorChase chaseBehaviour,int movementFluencyLevel,int[][]notUpDownPositions,char spawnDirection,int[]defaultGhostTarget) {
        super(name,"ghost_",gv,2,respawnPosition);

        this.currentDirection = spawnDirection;
        this.frightenedBehaviour = new BehaviorFrighten(movementFluencyLevel/2,defaultGhostTarget);
        this.respawningBehaviour = new BehaviorRespawn(new int[]{this.getPositionMapY(),this.getPositionMapX()},movementFluencyLevel,defaultGhostTarget);
        this.scatterBehaviour=new BehaviorScatter(scatterTarget,notUpDownPositions,movementFluencyLevel,defaultGhostTarget);
        this.behaviorChaseBehaviour =chaseBehaviour;
        this.currentBehaviour=chaseBehaviour;
        this.getCurrentBitmap();
    }

    public static void loadCommonBitmaps(@NotNull GameView gv){
        int idBm,blockSize;
        String packageName,pngName;
        String[] positions;
        Resources res;

        res=gv.getResources();
        blockSize=gv.getBlockSize();
        packageName = gv.getContext().getPackageName();

        positions=new String[]{"up","right","left","down"};
        respawningGhostBitmap=new Bitmap[4];
        vulnerableGhostBitmap=new Bitmap[4];

        for(int i=0;i<vulnerableGhostBitmap.length;i++){
            pngName="ghost_respawn_"+positions[i];
            idBm=res.getIdentifier(pngName,"drawable",packageName);
            respawningGhostBitmap[i]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, idBm), blockSize, blockSize, false);
            //Log.i("Load Bitmap",pngName+" loaded" );
        }

        for(int i=0;i<vulnerableGhostBitmap.length;i++){
            pngName="ghost_frighten"+i;
            idBm=res.getIdentifier(pngName,"drawable",packageName);
            vulnerableGhostBitmap[i]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, idBm), blockSize, blockSize, false);
            //Log.i("Load Bitmap",pngName+" loaded" );
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public synchronized void move(int[][]map,Pacman pacman){
        int[] ghostScreenPosition,nextPosition; //= new int[3]; //{posX,posY,direction}
        int moduleY, moduleX;

        //Log.i("Ghost current direction",this.currentDirection+"");
        ghostScreenPosition=new int[]{this.getPositionScreenY(),this.getPositionScreenX()};
        nextPosition=this.currentBehaviour.behave(map,ghostScreenPosition,pacman,this.currentDirection,this.blocksize);
        moduleY=ghostScreenPosition[0]%nextPosition[3];
        moduleX=ghostScreenPosition[1]%nextPosition[3];
        if(moduleY!=0 ||moduleX!=0){
            this.changePositionScreen((char)nextPosition[2],nextPosition[3]/2);
        }else{
            this.changePositionScreen((char)nextPosition[2],nextPosition[3]);
        }
        if(moduleY==0 &&moduleX==0 &&this.currentBehaviour.isRespawning() && this.getPositionMapY()==this.getSpawnPositionMapY() &&this.getPositionMapX()==this.getSpawnPositionMapX()){
            this.reestablishBehavior();
        }

        this.currentDirection = (char)nextPosition[2];
        this.usePortal(map[0].length,this.getPositionMapX());


        //Log.i("Ghost direction",this.currentDirection+"");
        //Log.i("Ghost is attacking",this.currentBehaviour.isAttacking()+"");
        //Log.i("Ghost Draw","["+this.currentPositionScreen[1]%this.blocksize+", "+this.currentPositionScreen[0]%this.blocksize+"]");
    }

    public synchronized void setChaseBehaviour() {
        //Log.i("GHOST "+this.name.toUpperCase(),"Chasing");
        //Change current bitmap
        this.currentBehaviour=this.behaviorChaseBehaviour;
        this.currentDirection=this.getOpositeDirection();
        this.getCurrentBitmap();

    }

    public synchronized void setScatterBehaviour() {
        //Change current bitmap
        //Log.i("GHOST "+this.name.toUpperCase(),"Scatter");
        this.currentBehaviour=this.scatterBehaviour;
        this.currentDirection=this.getOpositeDirection();
        this.getCurrentBitmap();

    }

    public synchronized void setFrightenedBehaviour() {
        //Change current bitmap
        //Log.i("GHOST "+this.name.toUpperCase(),"Afraid");
        if (!this.currentBehaviour.isRespawning()) {
            this.countdownGhost.pause();
            this.currentBehaviour=this.frightenedBehaviour;
            this.currentBitmapArray=Ghost.vulnerableGhostBitmap;
            this.getCurrentBitmap();
            this.currentDirection=this.getOpositeDirection();
        }
    }

    public synchronized void setRespawnBehaviour() {
        //Change current bitmap
        //Log.i("GHOST "+this.name.toUpperCase(),"Respawning");
        this.currentBitmapArray=Ghost.respawningGhostBitmap;
        this.currentBehaviour=this.respawningBehaviour;
        this.currentDirection=this.getOpositeDirection();
        this.getCurrentBitmap();
        //this.countdownGhost.pause();//to reestablish behavior later
    }

    public Behavior getState() {
        return this.currentBehaviour;
    }

    public Bitmap getCurrentBitmap(){
        Bitmap currentBM;
        if(this.currentBehaviour.isRespawning()){
            switch (this.currentDirection){
                case 'u':
                    currentBM=Ghost.respawningGhostBitmap[0];
                    break;
                case 'r':
                    currentBM=Ghost.respawningGhostBitmap[1];
                    break;
                case 'l':
                    currentBM=Ghost.respawningGhostBitmap[2];
                    break;
                case 'd':
                    currentBM=Ghost.respawningGhostBitmap[3];
                    break;
                default:
                    currentBM=null;
                    break;
            }
        }else if(this.currentBehaviour.isFrightened()){
            currentBM=Ghost.vulnerableGhostBitmap[this.currentFrame];
        }else{
            currentBM=super.getCurrentBitmap();
        }
        return currentBM;
    }

    public synchronized void setCountdownGhost(CountDownGhost countdownGhost,boolean run){
        this.countdownGhost=countdownGhost;
        if(run){
            Log.i("Ghost", "Started CD on Set CountDownGhost");
            this.countdownGhost.start();
        }
    }

    public synchronized void onLevelStart(int level){
        //Log.i("Ghost", "Started CD on level start");
        this.countdownGhost=new CountDownGhost(this,level,1,'c');
        this.countdownGhost.start();
    }

    public synchronized void reestablishBehavior(){
        Log.i("Ghost", "Started CD reestablish behavior");
        this.countdownGhost.start();
    }

    public synchronized void killPacman(){
        Log.i("Ghost", "Started CD reestablish behavior");
        this.countdownGhost.start();
    }


    private char getOpositeDirection(){
        char oposite;
        switch (this.currentDirection){
            case 'u':
                oposite='d';
                break;
            case 'd':
                oposite='u';
                break;
            case 'r':
                oposite='l';
                break;
            case 'l':
                oposite='r';
                break;
            default:
                oposite=' ';
                break;
        }
        return oposite;
    }

    public void respawn(){
        if(this.currentBehaviour.isAttacking()){
            this.countdownGhost.pause();
        }
        super.respawn();

    }

}
