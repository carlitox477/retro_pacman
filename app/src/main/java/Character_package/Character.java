package Character_package;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.pacman.GameView;

public abstract class Character {
    protected String name,prefix;
    protected Bitmap[][]bitmaps;
    protected char currentDirection;
    protected int fpm, blocksize, currentFrame;
    protected int[] spawnPositionScreen,currentPositionScreen;

    public Character(String name,String prefix, GameView gv, int fpm, int blocksize){
        this.name=name;
        this.prefix=prefix;
        this.blocksize=gv.getBlockSize();
        this.currentFrame=0;
        this.currentDirection=' ';
        this.fpm=fpm;
        this.currentPositionScreen=new int[]{gv.getGameMap().getPacmanSpawnPosition()[0]*blocksize,gv.getGameMap().getPacmanSpawnPosition()[1]*blocksize};
        this.spawnPositionScreen[0]=this.currentPositionScreen[0];
        this.spawnPositionScreen[1]=this.currentPositionScreen[1];
        this.bitmaps=new Bitmap[4][fpm];
        this.loadBitmaps(gv);
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
        this.currentPositionScreen[0]=this.spawnPositionScreen[1];
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
                this.bitmaps[i][j]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                        gv.getContext().getResources(), idBm), spriteSize, spriteSize, false);
            }
        }
    }

    public char getDirection(){
        return this.currentDirection;
    }

}
