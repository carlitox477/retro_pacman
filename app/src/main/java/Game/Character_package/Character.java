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

    public Character(String name,String prefix, int fpm,int[]spawnPosition,int blocksize, Resources res, String packageName){
        this.name=name;
        this.prefix=prefix;
        this.blocksize=blocksize;
        this.fpm=fpm;

        this.currentFrame=0;
        this.currentDirection=' ';
        this.currentPositionScreen=new int[]{spawnPosition[0]*blocksize,spawnPosition[1]*blocksize};
        this.spawnPositionScreen=new int[]{this.currentPositionScreen[0],this.currentPositionScreen[1]};
        this.bitmaps=new Bitmap[4][fpm];
        this.loadBitmaps(blocksize,res,packageName);
        this.currentBitmapArray=this.bitmaps[0];

    }

    public int getSpawnPositionMapX(){
        return this.spawnPositionScreen[0]/this.blocksize;
    }
    public int getSpawnPositionMapY(){
        return this.spawnPositionScreen[1]/this.blocksize;
    }

    public String getName(){
        return this.name;
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

    protected void loadBitmaps(int blocksize, Resources res, String packageName){
        //fpm: frames per movement; pacman 4; ghosts 2
        int idBm;
        String[] positions;

        positions=new String[]{"up","right","left","down"};

        for (int i=0;i<4;i++){
            for (int j=0;j<fpm;j++){
                idBm=res.getIdentifier(prefix+name + "_"+positions[i] + j, "drawable", packageName);
                this.bitmaps[i][j]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        res, idBm), blocksize, blocksize, false);
                //Log.i("Load Bitmap", "\""+prefix+name + "_"+positions[i] + j+"\" loaded");
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


    protected void changePositionScreen(char direction,int movementFluencyLevel){
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

    public void setFpm(int fpm) {
        this.fpm = fpm;
        this.currentFrame=this.currentFrame%fpm;
    }

    protected void usePortal(int mapLength, int movementFluencyLevel){
        int posXMap, limitWidth;
        posXMap=this.currentPositionScreen[0]/this.blocksize;
        limitWidth=mapLength*this.blocksize;

        if (posXMap==-1) {
            //to use left portal
            this.currentPositionScreen[0]=(limitWidth-this.blocksize);
        }else if(posXMap==mapLength){
            //Use right portal
            this.currentPositionScreen[0]=0;
        }
    }

}
