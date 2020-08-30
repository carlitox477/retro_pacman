package Game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

import Game.Character_package.Pacman;

public class GameMap {
    private int[][]ghostsSpawnPositions,ghostsScatterTarget,map, resetMap,notUpDownDecisionPositions,defaultGhostTargets;
    private int[]pacmanSpawnPosition;
    private int initialPallets;
    private Bitmap[] bonusBitmaps;

    public void loadMap1(){
        //31 * 28
        // 1 paredes
        // 2 pildoras
        // 3 superpildoras
        // 4 pacman spawn [24, 15]
        // 5 blinky spawn [14, 12]
        // 6 pinky spawn [16,12]
        // 7 inky spawn [14,17]
        // 8 clyde spawn [16,17]
        // 9 bonus
        //10 white bar
        //98 Front gates positions
        //99 Cells not playable by the Pacman
        this.resetMap= new int[][]{
                //1   2   3   4   5   6   7   8   9  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28
                { 1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1}, //1
                { 1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1}, //2
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //3
                { 1,  3,  1, 99, 99,  1,  2,  1, 99, 99, 99,  1,  2,  1,  1,  2,  1, 99, 99, 99,  1,  2,  1, 99, 99,  1,  3,  1}, //4
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //5
                { 1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1}, //6
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //7
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //8
                { 1,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  1}, //9
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  0,  1,  1,  0,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //10
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  1,  1,  1,  0,  1,  1,  0,  1,  1,  1,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //11
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  0,  0,  0, 98, 98,  0,  0,  0,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //12
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  1,  1,  1, 10, 10,  1,  1,  1,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //13
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  0,  1, 99, 99, 99, 99, 99, 99,  1,  0,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //14
                { 0,  0,  0,  0,  0,  0,  2,  0,  0,  0,  1, 99, 99, 99, 99, 99, 99,  1,  0,  0,  0,  2,  0,  0,  0,  0,  0,  0}, //15
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  0,  1, 99, 99, 99, 99, 99, 99,  1,  0,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //16
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  1,  1,  1,  1,  1,  1,  1,  1,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //17
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //18
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  1,  1,  1,  1,  1,  1,  1,  1,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //19
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  0,  1,  1,  1,  1,  1,  1,  1,  1,  0,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //20
                { 1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1}, //21
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //22
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //23
                { 1,  3,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  2,  0,  4,  2,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  3,  1}, //24
                { 1,  1,  1,  2,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  2,  1,  1,  1}, //25
                { 1,  1,  1,  2,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  2,  1,  1,  1}, //26
                { 1,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  1}, //27
                { 1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1}, //28
                { 1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1}, //29 (GG)
                { 1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1}, //30 (GG)
                { 1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1}  //31 (GG)
        };
        this.notUpDownDecisionPositions=new int[][]{{11,12},{11,13},{11,14},{11,15},{23,12},{23,13},{23,14},{23,15}}; // YX
        this.defaultGhostTargets=new int[][]{{11,13},{11,14}};//YX
        this.resetMap();
        this.initialPallets=this.countPallets();
        this.pacmanSpawnPosition=new int[]{14, 23};//XY
        this.ghostsSpawnPositions=new int[][]{
                {11,13},//XY
                {16,13},//XY
                {11,15},//XY
                {16,15}//XY
        };
        this.ghostsScatterTarget =new int[][]{
                {0,this.map[0].length-1},//YX
                {0,0},//YX
                {this.map.length-1,0},//YX
                {this.map.length-1,this.map[0].length-1}//YX
        };
    }

    public int[] getPacmanSpawnPosition() {
        return pacmanSpawnPosition;
    }
    public int[][] getGhostsSpawnPositions() {
        return ghostsSpawnPositions;
    }
    public int[][] getGhostsScatterTarget() {
        return ghostsScatterTarget;
    }
    public int[][]getMap(){
        return this.map;
    }
    public int getMapWidth(){
        return this.map[0].length;
    }
    public int getMapHeight(){
        return this.map.length;
    }
    public int[][] getNotUpDownDecisionPositions(){return this.notUpDownDecisionPositions;}
    public int[][] getDefaultGhostTarget() {
        return defaultGhostTargets;
    }

    public void setBonusAvailable() {
        //Se determina en que posicion del mapa se generara el bonus
        int[] spawn = this.generateMapSpawn();
        this.map[spawn[0]][spawn[1]] = 9;
    }

    public int[] generateMapSpawn() {
        int randomX, randomY;

        do{
            randomX = new Random().nextInt(this.map[0].length);
            randomY = new Random().nextInt(this.map.length);
        }while (this.map[randomY][randomX]!= 0);
        //Log.i("Value ["+randomY+","+randomX+"]",""+this.map[randomY][randomX] );

        return new int[]{randomY,randomX};
    }

    public void resetMap(){
        this.map=new int[this.resetMap.length][this.resetMap[0].length];
        for (int i=0;i<this.resetMap.length;i++){
            for(int j=0;j<this.resetMap[0].length;j++){
                this.map[i][j]=this.resetMap[i][j];
            }
        }
    }

    public int countPallets(){
        //Good
        int count,currentValue;

        count=0;
        for(int i=0; i<this.map.length;i++){
            for (int j=0;j<this.map[0].length;j++){
                currentValue=this.map[i][j];
                if(currentValue==2 || currentValue==3){
                    count++;
                }
            }
        }
        return count;
    }

    public int getEatenPallets(){
        return this.initialPallets-this.countPallets();
    }

    public void draw(Canvas canvas, int colorId, int blockSize, int level) {
        //Log.i("info", "Drawing map");
        Paint paint;
        paint = new Paint();
        paint.setColor(Color.WHITE);

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                int value = map[x][y];
                switch (value){
                    case 1:
                        paint.setStrokeWidth(2.5f);
                        paint.setColor(colorId);
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect((y * blockSize),(x * blockSize), (y * blockSize + blockSize),(x * blockSize + blockSize), paint);
                        break;
                    case 2:
                        paint.setColor(Color.WHITE);
                        canvas.drawCircle((y * blockSize + (blockSize / 2)),(x * blockSize + (blockSize / 2)), (float)0.15*blockSize, paint);
                        break;
                    case 3:
                        paint.setColor(Color.WHITE);
                        canvas.drawCircle((y * blockSize + (blockSize / 2)),(x * blockSize + (blockSize / 2)), (float)0.35*blockSize, paint);
                        break;
                    case 9:
                        this.drawBonus(canvas,new int[]{x,y},blockSize,level);
                        break;
                    case 10:
                        paint.setStrokeWidth(2.5f);
                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect((y * blockSize), ((x+1) * blockSize),((y+1) * blockSize),(x * blockSize+blockSize/2), paint);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void loadBonusBitmaps(int spriteSize,Resources res, String packageName){
        int idBm, totalBonusBitmaps;

        totalBonusBitmaps=7; //Cambiar esto si se agregan más bitmaps para otros niveles
        this.bonusBitmaps=new Bitmap[totalBonusBitmaps];

        for (int i=0;i<this.bonusBitmaps.length;i++){
            if(i<8){
                idBm=res.getIdentifier("bonus_0"+(i+1), "drawable", packageName);
            }else{
                idBm=res.getIdentifier("bonus_"+(i+1), "drawable", packageName);
            }
            this.bonusBitmaps[i]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idBm), spriteSize, spriteSize, false);
        }
    }

    private void drawBonus(Canvas canvas, int[] bonusPos, int blockSize, int level) {
        int bitmapId;
        if(level<=this.bonusBitmaps.length){
            bitmapId=level-1;
        }else{
            bitmapId=this.bonusBitmaps.length-1;
        }
        canvas.drawBitmap(this.bonusBitmaps[bitmapId], (bonusPos[1]) * blockSize, (bonusPos[0]) * blockSize, null);
    }

/*
    public void passLevelAnimation(Canvas c, int blockSize, Pacman pacman, int level) throws InterruptedException {
        for(int i=0; i<10;i++){
            this.draw(c, Color.WHITE,blockSize,level);
            Thread.sleep(500);
            this.draw(c, Color.BLUE,blockSize,level);
            Thread.sleep(500);
        }
        this.resetMap();
        pacman.respawn();

        //we need to restart sound
        Thread.sleep(1000);
    }

 */
}
