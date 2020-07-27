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

}
