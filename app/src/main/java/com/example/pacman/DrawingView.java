package com.example.pacman;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Random;

public class DrawingView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    //SurfaceView actualiza lo que tiene mediante hilo
    private Thread thread;

    private Thread bonusCounter;
    private int xPosBonus;
    private int yPosBonus;
    private int bonusResetTime = 1000;
    private boolean bonusAvailable;

    private SurfaceHolder holder;
    private boolean canDraw = false;

    private Paint paint;

    List<Node> path;

    private int screenWidth;                // Ancho de la pantalla


    private Block[] blocks = new Block[19 * 21];
    private int blockSize;



    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp;
    private Bitmap cherryBitmap;


    private int xPosPacman;
    private int yPosPacman;
    private int totalFrame = 4;             // Cantidad total de animation frames por direccion
    private int currentPacmanFrame = 0;     // animation frame de pacman actual
    private int currentArrowFrame = 0;      // animation frame de arrow actual
    private long frameTicker;               // tiempo desde que el ultimo frame fue dibujado


    private float x1, x2, y1, y2;           // Initial/Final positions of swipe
    private int direction = 4;              // direccion del movimiento, movimiento inicial es a la derecha
    private int nextDirection = 4;          // Buffer para la siguiente direccion de movimiento tactil


    private int viewDirection = 2;          // Direccion en la que pacman esta mirando

    public static int LONG_PRESS_TIME = 750;  // Time in milliseconds
    final Handler handler = new Handler();


    public DrawingView(Context context) {

        super(context);
        setFocusable(true);
        holder = getHolder();
        holder.addCallback(this);
        frameTicker = 1000 / totalFrame;


        paint = new Paint();
        paint.setColor(Color.WHITE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;


        blockSize = screenWidth / 18;
        blockSize = (blockSize / 9) * 9;

        xPosPacman = 9 * blockSize;
        yPosPacman = 15 * blockSize;

        bonusCounter = new CountdownBonusThread(this);
        bonusCounter.start();

        loadBitmapImages();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {


        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();

            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                drawMap(canvas);
                updateFrame(System.currentTimeMillis());
                drawBonus(canvas);
                drawPath(canvas);
                movePacman(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void movePacman(Canvas canvas) {

        int value;

        if (xPosPacman >= blockSize * 19) {
            xPosPacman = 0;
        }

        if ((xPosPacman % blockSize == 0) && (yPosPacman % blockSize == 0)) {

            AStar a = new AStar(this, 1,1);
            path = a.findPathTo(xPosPacman / blockSize,yPosPacman / blockSize);

            value = levellayout[yPosPacman / blockSize][xPosPacman / blockSize];

            if (value == 2) {
                levellayout[yPosPacman / blockSize][xPosPacman / blockSize] = 0;
            }
            if (value == 3) {
                levellayout[yPosPacman / blockSize][xPosPacman / blockSize] = 0;
            }
            if (value == 9) {
                levellayout[yPosPacman / blockSize][xPosPacman / blockSize] = 0;
            }


            if ((xPosPacman) > 0 * blockSize && (xPosPacman) < 18 * blockSize) {
                if (!((nextDirection == 3 && (levellayout[(yPosPacman / blockSize)][(xPosPacman / blockSize) - 1]) == 1) ||
                        (nextDirection == 1 && (levellayout[yPosPacman / blockSize][(xPosPacman / blockSize) + 1]) == 1) ||
                        (nextDirection == 0 && (levellayout[(yPosPacman / blockSize) - 1][xPosPacman / blockSize]) == 1) ||
                        (nextDirection == 2 && (levellayout[(yPosPacman / blockSize) + 1][xPosPacman / blockSize] == 1) ||
                                (nextDirection == 2 && levellayout[(yPosPacman / blockSize) + 1][xPosPacman / blockSize] == 5)))) {
                    viewDirection = direction = nextDirection;
                }
                if ((direction == 3 && (levellayout[(yPosPacman / blockSize)][(xPosPacman / blockSize) - 1]) == 1) ||
                        (direction == 1 && (levellayout[yPosPacman / blockSize][(xPosPacman / blockSize) + 1]) == 1) ||
                        (direction == 0 && (levellayout[(yPosPacman / blockSize) - 1][xPosPacman / blockSize]) == 1) ||
                        (direction == 2 && (levellayout[(yPosPacman / blockSize) + 1][xPosPacman / blockSize] == 1))) {
                    direction = 4;
                }


            }


        }

        if (xPosPacman < 0) {
            xPosPacman = blockSize * 19;
        }
        drawPacman(canvas);

        // Depending on the direction move the position of pacman
        if (direction == 0) {
            yPosPacman += -blockSize / 15;
        } else if (direction == 1) {
            xPosPacman += blockSize / 15;
        } else if (direction == 2) {
            yPosPacman += blockSize / 15;
        } else if (direction == 3) {
            xPosPacman += -blockSize / 15;
        }

    }

    // Method that draws pacman based on his viewDirection
    public void drawPacman(Canvas canvas) {
        Log.i("info", "Drawing pacman");
        switch (viewDirection) {
            case (0):
                canvas.drawBitmap(pacmanUp[currentPacmanFrame], xPosPacman, yPosPacman, paint);
                break;
            case (1):
                canvas.drawBitmap(pacmanRight[currentPacmanFrame], xPosPacman, yPosPacman, paint);
                break;
            case (3):
                canvas.drawBitmap(pacmanLeft[currentPacmanFrame], xPosPacman, yPosPacman, paint);
                break;
            default:
                canvas.drawBitmap(pacmanDown[currentPacmanFrame], xPosPacman, yPosPacman, paint);
                break;
        }
    }




    public void drawMap(Canvas canvas) {
        float offset = 0;
        for (int y = 0; y < 21; y++) {
            for (int x = 0; x < 19; x++) {
                int value = levellayout[y][x];
                if (value == 1) {
                    paint.setStrokeWidth(2.5f);
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect((x * blockSize) + offset, (y * blockSize) + offset, (x * blockSize + blockSize) + offset, (y * blockSize + blockSize) + offset, paint);
                } else if (value == 2) {
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle((x * blockSize + (blockSize / 2)) + offset, (y * blockSize + (blockSize / 2)) + offset, 5f, paint);
                } else if (value == 3) {
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle((x * blockSize + (blockSize / 2)) + offset, (y * blockSize + (blockSize / 2)) + offset, 8f, paint);
                }

            }
        }
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5f);
        canvas.drawLine(9 * blockSize, ((8 * blockSize) + (blockSize / 2)), (9 * blockSize) + blockSize, ((8 * blockSize) + (blockSize / 2)), paint);
    }

    private void drawBonus(Canvas canvas) {

        int value = levellayout[yPosBonus][xPosBonus];
        if ((value == 9) && bonusAvailable) {
            canvas.drawBitmap(cherryBitmap, xPosBonus * blockSize, yPosBonus * blockSize, null);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setBonusAvailable() {
        //Se determina en que posicion del mapa se generara el bonus
        int[] spawn = generateMapSpawn();
        int y = spawn[0];
        int x = spawn[1];
        int ch = levellayout[y][x];
        levellayout[y][x] = 9;
        this.bonusAvailable = true;




    }
    public void drawPath(Canvas canvas){
        if(path != null){
            paint = new Paint();
            paint.setColor(Color.RED);
            for (int i = 0; i < path.size() - 1; i++) {
                Node currentNode = path.get(i);
                Node nextNode = path.get(i + 1);
                canvas.drawLine(currentNode.x * blockSize, currentNode.y * blockSize, nextNode.x * blockSize, nextNode.y * blockSize, paint);
            }
        }



    }
    public int[] generateMapSpawn() {
        //Se genera una posicion aleatoria valida en la cual pacman pueda moverse
        int[] spawn = new int[2];
        xPosBonus = new Random().nextInt(17) + 1;
        yPosBonus = new Random().nextInt(18) + 1;
        int value = levellayout[yPosBonus][xPosBonus];

        //Si en la posicion generada no es posible moverse
        if (value == 1 || value == 2 || value == 5 || value == 6 || value == 7 || value == 8)
            spawn = generateMapSpawn();
        else {
            spawn[0] = yPosBonus;
            spawn[1] = xPosBonus;
        }

        return spawn;
    }

    private void loadBitmapImages() {
        // Escala los sprites en base al tamaño de la pantalla
        int spriteSize = screenWidth / 18;
        spriteSize = (spriteSize / 9) * 9;// Tamaño de pacman y fantasmas


        // Añadir bitmap de pacman mirando a la derecha
        pacmanRight = new Bitmap[totalFrame];
        pacmanRight[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right1), spriteSize, spriteSize, false);
        pacmanRight[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right2), spriteSize, spriteSize, false);
        pacmanRight[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right3), spriteSize, spriteSize, false);
        pacmanRight[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_right), spriteSize, spriteSize, false);
        // Añadir bitmap de pacman mirando a la abajo
        pacmanDown = new Bitmap[totalFrame];
        pacmanDown[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down1), spriteSize, spriteSize, false);
        pacmanDown[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down2), spriteSize, spriteSize, false);
        pacmanDown[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down3), spriteSize, spriteSize, false);
        pacmanDown[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_down), spriteSize, spriteSize, false);
        // Añadir bitmap de pacman mirando a la izquierda
        pacmanLeft = new Bitmap[totalFrame];
        pacmanLeft[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left1), spriteSize, spriteSize, false);
        pacmanLeft[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left2), spriteSize, spriteSize, false);
        pacmanLeft[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left3), spriteSize, spriteSize, false);
        pacmanLeft[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_left), spriteSize, spriteSize, false);
        // Añadir bitmap de pacman mirando a la arriba
        pacmanUp = new Bitmap[totalFrame];
        pacmanUp[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up1), spriteSize, spriteSize, false);
        pacmanUp[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up2), spriteSize, spriteSize, false);
        pacmanUp[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up3), spriteSize, spriteSize, false);
        pacmanUp[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.pacman_up), spriteSize, spriteSize, false);


        //Añadir bitmap de cerezas bonus
        cherryBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.cherry), spriteSize, spriteSize, false);
    }

    Runnable longPressed = new Runnable() {
        public void run() {
            Log.i("info", "LongPress");
            Intent pauseIntent = new Intent(getContext(), PauseActivity.class);
            getContext().startActivity(pauseIntent);
        }
    };


    // Methodo para captar touchEvents
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN): {
                x1 = event.getX();
                y1 = event.getY();
                handler.postDelayed(longPressed, LONG_PRESS_TIME);
                break;
            }
            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                calculateSwipeDirection();
                handler.removeCallbacks(longPressed);
                break;
            }
        }
        return true;
    }

    // Calcula la direccion en la que el jugador realiza el swipe
    // basado en la calculacion de las diferencias en
    // la posicion inicial y la posicion final del swipe
    private void calculateSwipeDirection() {
        float xDiff = (x2 - x1);
        float yDiff = (y2 - y1);

        // Direcciones
        // 0 = arriba
        // 1 = derecha
        // 2 = abajo
        // 3 = izquierda
        // 4 = sin movimiento

        //Chequea que eje tiene la mayor distancia
        //en orden para saber en que direccion el swipe
        //va a ser
        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            if (yDiff < 0) {
                nextDirection = 0;
            } else if (yDiff > 0) {
                nextDirection = 2;
            }
        } else {
            if (xDiff < 0) {
                nextDirection = 3;
            } else if (xDiff > 0) {
                nextDirection = 1;
            }
        }
    }

    //Chequea si se deberia actualizar el frame actual basado en el
    // tiempo que a transcurrido asi la animacion
    //no se ve muy rapida y mala
    private void updateFrame(long gameTime) {

        // Si el tiempo suficiente a transcurrido, pasar al siguiente frame
        if (gameTime > frameTicker + (totalFrame * 30)) {
            frameTicker = gameTime;

            // incrementar el frame
            currentPacmanFrame++;
            // ciclar al principio al frame 0 si han ocurrido todos
            if (currentPacmanFrame >= totalFrame) {
                currentPacmanFrame = 0;
            }
        }
        if (gameTime > frameTicker + (50)) {
            currentArrowFrame++;
            if (currentArrowFrame >= 7) {
                currentArrowFrame = 0;
            }
        }
    }

    //Cada bloque es representado con 16 bits, el cual nos dice informacion tal como la posicion de la pared,
    // si hay una pastilla, si hay una fruta, si pacman esta atravesandolo o si un fantasma se encuentra
    // atravesandolo, por eso es necesario descomponer cada uno de estos numeros en la composicion binaria
    //para entender las operaciones que estan ocurriendo a la hora de realizar los chequeos
    /*
     * 2 ^ 0 : Pared izquierda
     * 2 ^ 1 : Pared arriba
     * 2 ^ 2 : Pared derecha
     * 2 ^ 3 : Pared abajo
     * 2 ^ 4 : Pastilla
     * 2 ^ 5 : Bonus
     * 2 ^ 6 : Super pastilla
     * 2 ^ 7 : Fantasma
     * 2 ^ 8 : Pared de base fantasma (Se reduce la anchura de las paredes)
     * */

    //19 * 21
    // 1 paredes
    // 2 pildoras
    // 3 superpildoras
    // 4 pacman spawn
    // 5 blinky spawn
    // 6 pinky spawn
    // 7 inky spawn
    // 8 clyde spawn
    // 9 bonus
    final int[][] levellayout= new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 3, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 3, 1},
            {1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 2, 1, 1, 5, 1, 1, 2, 1, 2, 1, 1, 1, 1},
            {2, 2, 2, 2, 2, 2, 2, 1, 6, 7, 8, 1, 2, 2, 2, 2, 2, 2, 2},
            {1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
            {1, 2, 2, 1, 2, 2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 1, 2, 2, 1},
            {1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1},
            {1, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };


    /*
    final short leveldata1[][] = new short[][]{

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {67, 26, 26, 18, 26, 26, 26, 22, 0, 19, 26, 26, 26, 18, 26, 26, 70},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {17, 26, 26, 16, 26, 18, 26, 24, 26, 24, 26, 18, 26, 16, 26, 26, 20},
            {25, 26, 26, 20, 0, 25, 26, 22, 0, 19, 26, 28, 0, 17, 26, 26, 28},
            {0, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 19, 26, 24, 18, 24, 26, 22, 0, 21, 0, 0, 0},
            {26, 26, 26, 16, 26, 20, 0, 0, 512, 0, 0, 17, 26, 16, 26, 26, 26},
            {0, 0, 0, 21, 0, 21, 0, 267, 1288, 270, 0, 21, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0},
            {19, 26, 26, 16, 26, 24, 26, 22, 0, 19, 26, 24, 26, 16, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {25, 22, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 19, 28}, // "2" in this line is for
            {0, 21, 0, 17, 26, 26, 18, 24, 26, 24, 18, 26, 26, 20, 0, 21, 0}, // pacman's spawn
            {19, 24, 26, 28, 0, 0, 25, 18, 26, 18, 28, 0, 0, 25, 26, 24, 22},
            {21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21},
            {73, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 26, 76},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

     */



    /* 0 pared inferior
     *     */

    //Callback methods
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getBlockSize() {
        return blockSize;
    }


    public void resume() {
        canDraw = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        canDraw = false;
        while (true) {
            try {
                thread.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }

    public int getBonusResetTime() {
        return bonusResetTime;
    }
    public int[][] getLevellayout(){
        return levellayout;
    }
}

class Block {


    private int x;
    private int y;
    private int value;

    private int blockSize;


    private boolean isWalkable;


    public Block(int x, int y, int blockSize, int value) {
        this.x = x;
        this.y = y;
        this.blockSize = blockSize;
        this.value = value;
    }

    public void draw(Canvas canvas) {


    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public int getValue() {
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}