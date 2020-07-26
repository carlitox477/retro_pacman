package Game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.pacman.R;

import Game.Character_package.Character;
import Game.Character_package.Ghost;

public abstract class Draw {
    private static Paint paint;
    private static int blockSize;
    private static Bitmap cherryBitmap;

    public static void inicialize(int blockSize, Resources resources){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        Draw.blockSize=blockSize;
        cherryBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.cherry), blockSize, blockSize, false);

    }
    public static void drawMap(Canvas canvas, int colorId, int[][]map) {
        //Log.i("info", "Drawing map");

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
                    case 10:
                        paint.setStrokeWidth(2.5f);
                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect((y * blockSize) + blockSize/2, ((x+1) * blockSize),((y+1) * blockSize),(x * blockSize), paint);
                        break;
                    default:
                        break;
                }
            }
        }
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5f);
        canvas.drawLine(9 * blockSize, ((8 * blockSize) + (blockSize / 2)), (9 * blockSize) + blockSize, ((8 * blockSize) + (blockSize / 2)), paint);
    }

    public static void drawGhosts(Ghost[] ghosts, Canvas canvas) {
        for (int i = 0; i < ghosts.length; i++) {
            Log.i("Draw Ghost "+i,"At "+ghosts[i].getxPos()+" "+ghosts[i].getyPos());
            canvas.drawBitmap(ghosts[i].getCurrentBitmap(), ghosts[i].getyPos(),ghosts[i].getxPos(), paint); //invertir x e y
        }
    }

    public static void drawCharacter(Character character, Canvas canvas){
        Bitmap bMap;
        int xPosScreen,yPosScreen;

        bMap=character.getCurrentBitmap();
        xPosScreen=character.getPositionScreenX();
        yPosScreen=character.getPositionScreenY();
        canvas.drawBitmap(bMap,xPosScreen,yPosScreen,paint);
    }
    
    public static void drawBonus(Canvas canvas, int[][]map, int[] bonusPos,boolean bonusAvailable) {
        int value = map[bonusPos[1]][bonusPos[0]];
        if ((value == 9) && bonusAvailable) {
            canvas.drawBitmap(cherryBitmap, bonusPos[0] * blockSize, bonusPos[1] * blockSize, null);
        }
    }
}
