package Character_package;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Character {
    protected String characterName;
    protected Bitmap[] rightBmp, leftBmp, downBmp, upBmp, currentBmp;
    protected int[] currentPosition, respawnPosition; //(X,Y)
    protected char movement; //'r' is right, 'l' is left, 'u' is up, 'd' is down

    public Character (String characterName, int[]respawnPosition){ //fpm
        this.characterName=characterName;
        this.currentPosition=new int[2];
    }

    public Bitmap[] getCurrentBmp() {
        return currentBmp;
    }

    public void move(char moveTo){
        movement=moveTo;
        switch (moveTo){
            case 'r':
                currentPosition[0]++;
                currentBmp=rightBmp;
                break;
            case 'l':
                currentPosition[0]--;
                currentBmp=leftBmp;
                break;
            case 'u':
                currentPosition[1]--;
                currentBmp=upBmp;
                break;
            case 'd':
                currentPosition[1]++;
                currentBmp=downBmp;
                break;
            default:
                break;
        }
    }

    public void setMovement(char direction){
        movement=direction;
    }

    public void setPosition(int x, int y){
        currentPosition[0]=x;
        currentPosition[1]=y;
    }
    protected void loadBitmap(Context context, int spriteSize, int fpm){
        //fpm: frames per movement
        Resources res=context.getResources();
        int idRight, idLeft, idDown, idUp;

        //We create Bitmap arrays deppending in fpm value
        //We should
        rightBmp = new Bitmap[fpm];
        downBmp = new Bitmap[fpm];
        leftBmp = new Bitmap[fpm];
        upBmp = new Bitmap[fpm];

        // We add pacman's bitmap looking to the right
        for (int i=0; i<fpm; i++){
            //pacman movement image should be png with the name as "pacman_direction#
            // # is a number; direction must be "right", "left", "up" or "down"
            idRight=res.getIdentifier(characterName+"_right"+i, "drawable", context.getPackageName());
            idLeft=res.getIdentifier(characterName+"_left"+i, "drawable", context.getPackageName());
            idUp=res.getIdentifier(characterName+"_up"+i, "drawable", context.getPackageName());
            idDown=res.getIdentifier(characterName+"_down"+i, "drawable", context.getPackageName());

            //we add the bitmaps
            rightBmp[i]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idRight), spriteSize, spriteSize, false);
            leftBmp[i]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idLeft), spriteSize, spriteSize, false);
            upBmp[i]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idUp), spriteSize, spriteSize, false);
            downBmp[i]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idDown), spriteSize, spriteSize, false);
        }
    }
}
