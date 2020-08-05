package Game.Character_package;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import Game.Behavior.*;
import Game.Behavior.ChaseBehavior.*;
import Game.GameCountDown.CountdownGhostsState;
import Game.GameView;



public class Ghost extends Character {
    private static Bitmap[] vulnerableGhostBitmap,respawningGhostBitmap;
    private Behavior currentBehaviour;

    private BehaviorChase behaviorChaseBehaviour; //set bitmap
    private BehaviorFrighten frightenedBehaviour;
    private BehaviorScatter scatterBehaviour;
    private BehaviorRespawn respawningBehaviour;
    private CountdownGhostsState countdownGhostsState;

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

    public Ghost(String name,GameView gv,int[]respawnPosition,int[]scatterTarget, BehaviorChase chaseBehaviour,int movementFluencyLevel,int[][]notUpDownPositions,char spawnDirection,int[]defaultGhostTarget) {
        super(name,"ghost_",gv,2,respawnPosition);

        this.currentDirection = spawnDirection;
        this.frightenedBehaviour = new BehaviorFrighten(movementFluencyLevel/2,defaultGhostTarget);
        this.respawningBehaviour = new BehaviorRespawn(respawnPosition,movementFluencyLevel,defaultGhostTarget);
        this.scatterBehaviour=new BehaviorScatter(scatterTarget,notUpDownPositions,movementFluencyLevel,defaultGhostTarget);
        this.behaviorChaseBehaviour =chaseBehaviour;
        //this.currentBehaviour=chaseBehaviour;
        this.currentBehaviour=this.scatterBehaviour;
        this.getCurrentBitmap();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void move(int[][]map,Pacman pacman) {
        int[] ghostScreenPosition,nextPosition; //= new int[3]; //{posX,posY,direction}

        //Log.i("Ghost current direction",this.currentDirection+"");
        ghostScreenPosition=new int[]{this.getPositionScreenY(),this.getPositionScreenX()};
        nextPosition=this.currentBehaviour.behave(map,ghostScreenPosition,pacman,this.currentDirection,this.blocksize);
        this.changePositionScreen((char)nextPosition[2],nextPosition[3]);
        this.currentDirection = (char)nextPosition[2];
        this.usePortal(map[0].length,this.getPositionMapX());

        //Log.i("Ghost direction",this.currentDirection+"");
        //Log.i("Ghost is attacking",this.currentBehaviour.isAttacking()+"");
        //Log.i("Ghost Draw","["+this.currentPositionScreen[1]%this.blocksize+", "+this.currentPositionScreen[0]%this.blocksize+"]");
    }

    public void setChaseBehaviour() {
        //Log.i("GHOST "+this.name.toUpperCase(),"Chasing");
        //Change current bitmap
        this.currentBehaviour=this.behaviorChaseBehaviour;
        this.currentDirection=this.currentBehaviour.getOpositeDirection(this.currentDirection);
        this.getCurrentBitmap();
    }

    public void setScatterBehaviour() {
        //Change current bitmap
        //Log.i("GHOST "+this.name.toUpperCase(),"Scatter");
        this.currentBehaviour=this.scatterBehaviour;
        this.currentDirection=this.currentBehaviour.getOpositeDirection(this.currentDirection);
    }

    public void setFrightenedBehaviour() {
        //Change current bitmap
        //Log.i("GHOST "+this.name.toUpperCase(),"Afraid");
        if (!this.currentBehaviour.isRespawning()) {
            this.currentBitmapArray=Ghost.vulnerableGhostBitmap;
            this.currentBehaviour=this.frightenedBehaviour;
            this.currentDirection=this.currentBehaviour.getOpositeDirection(this.currentDirection);
        }
    }

    public void setRespawnBehaviour() {
        //Change current bitmap
        //Log.i("GHOST "+this.name.toUpperCase(),"Respawning");
        this.currentBitmapArray=Ghost.respawningGhostBitmap;
        this.currentBehaviour=this.respawningBehaviour;
        this.currentDirection=this.currentBehaviour.getOpositeDirection(this.currentDirection);
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


}
