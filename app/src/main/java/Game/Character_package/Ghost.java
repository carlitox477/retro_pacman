package Game.Character_package;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import Game.Behaviour.Behaviour;
import Game.Behaviour.FrightenedBehaviour;
import Game.Behaviour.RespawningBehaviour;
import Game.Behaviour.ScatterBehaviour;
import Game.GameManager;
import Game.GameView;

import Game.Behaviour.Chase.ChaseBehaviour;


public class Ghost extends Character {
    private static Bitmap[] vulnerableGhostBitmap,respawningGhostBitmap;
    private Behaviour currentBehaviour;

    private ChaseBehaviour chaseBehaviour; //set bitmap
    private FrightenedBehaviour frightenedBehaviour;
    private ScatterBehaviour scatterBehaviour;
    private RespawningBehaviour respawningBehaviour;

    public static void loadCommonBitmaps(GameView gv){
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
            Log.i("Load Bitmap",pngName+" loaded" );
        }

        for(int i=0;i<vulnerableGhostBitmap.length;i++){
            pngName="ghost_frighten"+i;
            idBm=res.getIdentifier(pngName,"drawable",packageName);
            vulnerableGhostBitmap[i]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, idBm), blockSize, blockSize, false);
            Log.i("Load Bitmap",pngName+" loaded" );
        }

    }

    public Ghost(String name,GameView gv,int[]mapSpawnPosition,int[]scatterTarget, ChaseBehaviour chaseBehaviour) {
        super(name,"ghost_",gv,2,mapSpawnPosition);

        this.frightenedBehaviour = new FrightenedBehaviour();
        this.respawningBehaviour = new RespawningBehaviour();
        this.scatterBehaviour=new ScatterBehaviour(scatterTarget);
        this.chaseBehaviour=chaseBehaviour;
        this.getCurrentBitmap();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void move(GameManager gameManager, int blocksize) {
        int[] nextPosition; //= new int[3]; //{posX,posY,direction}
        nextPosition=this.currentBehaviour.behave(gameManager,blocksize,this.currentPositionScreen[0], this.currentPositionScreen[1], this.currentDirection);
        this.currentPositionScreen[0] = nextPosition[0];
        this.currentPositionScreen[1]= nextPosition[1];
        this.currentDirection = (char)nextPosition[2];
    }

    public void setChaseBehaviour() {
        //Change current bitmap
        this.currentBehaviour=this.chaseBehaviour;
        this.getCurrentBitmap();
    }

    public void setScatterBehaviour() {
        //Change current bitmap
        this.currentBehaviour=this.scatterBehaviour;
    }

    public void setFrightenedBehaviour() {
        //Change current bitmap
        if (!this.currentBehaviour.isRespawning()) {
            this.currentBitmapArray=Ghost.vulnerableGhostBitmap;
            this.currentBehaviour=this.frightenedBehaviour;
        }
    }

    public void setRespawnBehaviour() {
        //Change current bitmap
        this.currentBitmapArray=Ghost.respawningGhostBitmap;
        this.currentBehaviour=this.respawningBehaviour;
    }

    public Behaviour getState() {
        return this.currentBehaviour;
    }

    public int getxPos() {
        return this.currentPositionScreen[0];
    }

    public int getyPos() {
        return this.currentPositionScreen[1];
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
