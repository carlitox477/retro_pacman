package Game.Character_package;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

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

    public static void loadCommonBitmaps(Resources resources,int blockSize){
        vulnerableGhostBitmap=new Bitmap[2];
        respawningGhostBitmap=new Bitmap[4];

        //vulnerableGhostBitmap[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.vulnerable_ghost), blockSize, blockSize, false);
        //vulnerableGhostBitmap[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.vulnerable_ghost), blockSize, blockSize, false);

        //respawningGhostBitmap[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.eyes_ghost), blockSize, blockSize, false);
        //respawningGhostBitmap[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.eyes_ghost), blockSize, blockSize, false);
        //respawningGhostBitmap[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.eyes_ghost), blockSize, blockSize, false);
        //respawningGhostBitmap[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.eyes_ghost), blockSize, blockSize, false);
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
        this.getCurrentBitmap();
    }

    public void setFrightenedBehaviour() {
        //Change current bitmap
        if (!this.currentBehaviour.isRespawning()) {
            this.currentBitmapArray=Ghost.vulnerableGhostBitmap;
            this.currentBehaviour=this.frightenedBehaviour;
            this.getCurrentBitmap();
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
                    currentBM=Ghost.vulnerableGhostBitmap[0];
                    break;
                case 'r':
                    currentBM=Ghost.vulnerableGhostBitmap[1];
                    break;
                case 'l':
                    currentBM=Ghost.vulnerableGhostBitmap[2];
                    break;
                case 'd':
                    currentBM=Ghost.vulnerableGhostBitmap[3];
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
