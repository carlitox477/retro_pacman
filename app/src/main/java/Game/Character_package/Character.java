package Game.Character_package;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import Game.GameView;

public abstract class Character {
    protected String name,prefix;
    protected Bitmap[][]bitmaps;
    protected Bitmap[] currentBitmapArray;
    protected char currentDirection;
    protected int fpm, blocksize, currentFrame;
    protected int[] spawnPositionScreen,currentPositionScreen;

    public Character(String name,String prefix, GameView gv, int fpm,int[]spawnPosition){
        this.name=name;
        this.prefix=prefix;
        this.blocksize=gv.getBlockSize();
        this.fpm=fpm;

        this.currentFrame=0;
        this.currentDirection=' ';
        this.currentPositionScreen=new int[]{spawnPosition[0]*blocksize,spawnPosition[1]*blocksize};
        this.spawnPositionScreen=new int[]{this.currentPositionScreen[0],this.currentPositionScreen[1]};
        this.bitmaps=new Bitmap[4][fpm];
        this.loadBitmaps(gv);
        this.currentBitmapArray=this.bitmaps[0];

    }

    public int getSpawnPositionMapX(){
        return this.spawnPositionScreen[0]/this.blocksize;
    }
    public int getSpawnPositionMapY(){
        return this.spawnPositionScreen[1]/this.blocksize;
    }

    public int getSpawnPositionScreenX(){
        return this.spawnPositionScreen[0];
    }

    public int getSpawnPositionScreenY(){
        return this.spawnPositionScreen[1];
    }

    public int getPositionMapX(){
        return this.currentPositionScreen[0]/this.blocksize;
    }

    public int getPositionMapY(){
        return this.currentPositionScreen[1]/this.blocksize;
    }

    public int getPositionScreenX(){
        return this.currentPositionScreen[0];
    }
    
    public int getPositionScreenY(){
        return this.currentPositionScreen[1];
    }

    public int getCurrentFrame() {
        int sal=currentFrame;
        this.currentFrame++;
        this.currentFrame%=fpm;
        return sal;
    }

    public void respawn(){
        this.currentPositionScreen[0]=this.spawnPositionScreen[0];
        this.currentPositionScreen[1]=this.spawnPositionScreen[1];
    }

    public char getCurrentDirection(){
        return this.currentDirection;
    }

    public Bitmap[][] getBitmaps() {
        return bitmaps;
    }

    protected void loadBitmaps(GameView gv){
        //fpm: frames per movement; pacman 4; ghosts 2
        int idBm,spriteSize;
        String packageName;
        Resources res;
        String[] positions;

        spriteSize=gv.getBlockSize();
        res = gv.getResources();
        packageName = gv.getContext().getPackageName();
        positions=new String[]{"up","right","left","down"};

        for (int i=0;i<4;i++){
            for (int j=0;j<fpm;j++){
                idBm=res.getIdentifier(prefix+name + "_"+positions[i] + j, "drawable", packageName);
                Log.i("Load Bitmap", "Loading \""+idBm+"\"");
                Log.i("Load Bitmap", "Package Name \""+packageName+"\"");
                this.bitmaps[i][j]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        res, idBm), spriteSize, spriteSize, false);
                Log.i("Load Bitmap", "\""+prefix+name + "_"+positions[i] + j+"\" loaded");
            }
        }
    }

    public char getDirection(){
        return this.currentDirection;
    }

    public Bitmap getCurrentBitmap(){
       // int frame;
        switch (currentDirection){
            case 'u':
                this.currentBitmapArray=this.bitmaps[0];
                break;
            case 'r':
                this.currentBitmapArray=this.bitmaps[1];
                break;
            case 'l':
                this.currentBitmapArray=this.bitmaps[2];
                break;
            case 'd':
                this.currentBitmapArray=this.bitmaps[3];
                break;
            default:
                break;
        }
        //frame=this.currentFrame;
        //this.currentFrame++;
        //return this.currentBitmapArray[frame];
        return this.currentBitmapArray[this.currentFrame];
    }

    public void changeFrame(){
        this.currentFrame=(this.currentFrame+1)%fpm;
    }

    public void draw(Canvas canvas){
        Paint paint;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(this.getCurrentBitmap(),this.currentPositionScreen[0],this.currentPositionScreen[1],paint);
    }

}
