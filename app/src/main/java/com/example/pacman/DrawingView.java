package com.example.pacman;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class DrawingView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private Thread thread;
    private Thread bonusCounter;

    private SurfaceHolder holder;
    private boolean canDraw = false;

    private Paint paint = new Paint();
    private int screenWidth;                // Ancho de la pantalla
    private int blockSize;

    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp;
    private Bitmap ghostBitmap;
    private Bitmap cherryBitmap;
    private int xPosPacman;
    private int yPosPacman;
    private int totalFrame = 4;             // Cantidad total de animation frames por direccion
    private int currentPacmanFrame = 0;     // animation frame de pacman actual
    private int currentArrowFrame = 0;      // animation frame de arrow actual
    private long frameTicker;               // tiempo desde que el ultimo frame fue dibujado

    private int bonusResetTime = 5;
    private boolean bonusAvailable = false;
    private CountDownTimer bonusTimer;
    private int xPosBonus;
    private int yPosBonus;

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
        blockSize = screenWidth / 17; //
        blockSize = (blockSize / 5) * 5;
        xPosPacman = 8 * blockSize;
        yPosPacman = 13 * blockSize;
        xPosBonus = 9 ;
        yPosBonus = 14 ;
        bonusCounter = new CountdownBonusThread(this);
        bonusCounter.start();

        loadBitmapImages();


    }


    @Override
    public void run() {
        Log.i("info", "Run");
        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();

            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                drawMap(canvas);
                updateFrame(System.currentTimeMillis());
                //moveGhosts(canvas)
                movePacman(canvas);
                drawPellets(canvas);
                drawSuperPellets(canvas);

                drawBonus(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }




    public void movePacman(Canvas canvas) {
        short ch;


        //Chequeamos si xPos y yPos de pacman son multiplos del tamaño del bloque (blocksize)
        if ((xPosPacman % blockSize == 0) && (yPosPacman % blockSize == 0)) {

            //Cuando pacman entra por el tunel de la derecha reaparece por la izquierda
            if (xPosPacman >= blockSize * 17) {
                xPosPacman = 0;
            }
            //Cuando pacman entra por el tunel de la izquierda reaparece por la derecha
            if (xPosPacman < 0) {
                xPosPacman = blockSize * 16;
            }

            //Es utilizado para buscar el numero en el arreglo del nivel para chequear la posicion de las paredes
            //las pastillas y los caramelos
            ch = leveldata1[yPosPacman / blockSize][xPosPacman / blockSize];

            // Si hay una pastilla, pacman la come
            if ((ch & 16) != 0) {
                // Invisibilizamos la pastilla asi no se renderiza
                leveldata1[yPosPacman / blockSize][xPosPacman / blockSize] = (short) (ch ^ 16);
            }
            // Si hay un bonus, pacman la come
            if ((ch & 32) != 0) {
                Log.i("info", "Bonus has been eaten");
                // Invisibilizamos el bonus asi no se renderiza
                leveldata1[yPosPacman / blockSize][xPosPacman / blockSize] = (short) (ch ^ 32);
                //Indicamos que el bonus ya no se encuentra disponible
                bonusAvailable = false;
                //Comenzamos el countdown nuevamente
                bonusCounter = new CountdownBonusThread(this);
                bonusCounter.start();
            }
            // Si hay una super pastilla, pacman la come y los fantasmas se vuelven vulnerables
            if ((ch & 64) != 0) {
                // Invisibilizamos la pastilla asi no se renderiza
                leveldata1[yPosPacman / blockSize][xPosPacman / blockSize] = (short) (ch ^ 64);
            }

            // Chequeamos el buffering de la direccion
            if (!((nextDirection == 3 && (ch & 1) != 0) ||
                    (nextDirection == 1 && (ch & 4) != 0) ||
                    (nextDirection == 0 && (ch & 2) != 0) ||
                    (nextDirection == 2 && (ch & 8) != 0))) {
                viewDirection = direction = nextDirection;
            }

            // Chequeamos por la colision de las paredes
            if ((direction == 3 && (ch & 1) != 0) ||
                    (direction == 1 && (ch & 4) != 0) ||
                    (direction == 0 && (ch & 2) != 0) ||
                    (direction == 2 && (ch & 8) != 0)) {
                direction = 4;
            }
        }


        drawPacman(canvas);

        //Dependiendo de la direccion, movemos la posicion de pacman
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

    // Metodo que dibuja a pacman segun su direccion
    public void drawPacman(Canvas canvas) {
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

    // Metodos que dibuja las pastillas y las actualiza cuando son comidas
    public void drawPellets(Canvas canvas) {
        float x;
        float y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;
                // Dibuja pastilla en el medio del bloque
                if ((leveldata1[i][j] & 16) != 0)
                    canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 10, paint);
            }
        }
    }
    private void drawSuperPellets(Canvas canvas) {
        float x;
        float y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;
                // Dibuja pastilla en el medio del bloque
                if ((leveldata1[i][j] & 64) != 0)
                    canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 6, paint);
            }
        }
    }
    private void drawBonus(Canvas canvas) {

        short ch = leveldata1[yPosBonus][xPosBonus];
        if ((ch & 32) != 0  && bonusAvailable) {
            canvas.drawBitmap(cherryBitmap, xPosBonus * blockSize, yPosBonus * blockSize, null);
        }

    }
    public void setBonusAvailable() {
    //Se determina en que posicion del mapa se generara el bonus
        int[] spawn = generateMapSpawn();
        int y = spawn[0];
        int x = spawn[1];
        int ch = leveldata1[y][x];
        leveldata1[y][x] = (short) (ch ^ 32);
        this.bonusAvailable = true;
        Log.i("info", "bonus now available at" + xPosBonus +" , " + yPosBonus);
    }
    public int[] generateMapSpawn(){
        //Se genera una posicion aleatoria valida en la cual pacman pueda moverse
        int[] spawn = new int[2];
        xPosBonus = new Random().nextInt(17);
        yPosBonus = new Random().nextInt(17);
        short ch = leveldata1[yPosBonus][xPosBonus];

        //Si la posicion generada no es posible moverse
        if(ch == 0)
            spawn = generateMapSpawn();
        else{
            spawn[0] = yPosBonus;
            spawn[1] = xPosBonus;
        }

        return spawn;
    }
    public void drawMap(Canvas canvas) {
        Log.i("info", "Drawing map");
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2.5f);
        int x;
        int y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 17; j++) {
                x = j * blockSize;
                y = i * blockSize;
                if ((leveldata1[i][j] & 1) != 0) { // dibuja izquierda
                    canvas.drawLine(x, y, x, y + blockSize - 1, paint);
                    Log.i("info", "Drawing map");
                }
                if ((leveldata1[i][j] & 2) != 0) { // dibuja arriba
                    canvas.drawLine(x, y, x + blockSize - 1, y, paint);
                }
                if ((leveldata1[i][j] & 4) != 0) { // dibuja derecha
                    canvas.drawLine(
                            x + blockSize, y, x + blockSize, y + blockSize - 1, paint);
                }
                if ((leveldata1[i][j] & 8) != 0) { // dibuja abajo
                    canvas.drawLine(
                            x, y + blockSize, x + blockSize - 1, y + blockSize, paint);
                }
            }
        }
        paint.setColor(Color.WHITE);
    }

    private void loadBitmapImages() {
        // Escala los sprites en base al tamaño de la pantalla
        int spriteSize = screenWidth / 17;        // Tamaño de pacman y fantasmas
        spriteSize = (spriteSize / 5) * 5;      // Los mantenemos multiplos de 5

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

        //Añadir bitmap de fantasma
        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.ghost), spriteSize, spriteSize, false);

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

    //Cada bloque es representado con 6 bits, el cual nos dice informacion tal como la posicion de la pared, si hay una pastilla, si hay una fruta,
    //si pacman esta atravesandolo o si un fantasma se encuentra atravesandolo, por eso es necesario descomponer cada uno de estos numeros en la composicion binaria
    //para entender las operaciones que estan ocurriendo a la hora de realizar los chequeos
    /*
    * 2 ^ 0 : Pared
    * 2 ^ 1 : Pared
    * 2 ^ 2 : Pared
    * 2 ^ 3 : Pared
    * 2 ^ 4 : Pastilla
    * 2 ^ 5 : Bonus
    * 2 ^ 6 : Super pastilla
    * 2 ^ 7 : Fantasma
    * 2 ^ 8 : Disponible para spawn de bonus
    * */
    final short leveldata1[][] = new short[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {67, 26, 26, 18, 26, 26, 26, 22, 0, 19, 26, 26, 26, 18, 26, 26, 70},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {17, 26, 26, 16, 26, 18, 26, 24, 26, 24, 26, 18, 26, 16, 26, 26, 20},
            {25, 26, 26, 20, 0, 25, 26, 22, 0, 19, 26, 28, 0, 17, 26, 26, 28},
            {0, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 19, 26, 24, 26, 24, 26, 22, 0, 21, 0, 0, 0},
            {26, 26, 26, 16, 26, 20, 0, 0, 0, 0, 0, 17, 26, 16, 26, 26, 26},
            {0, 0, 0, 21, 0, 17, 26, 26, 26, 26, 26, 20, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0},
            {19, 26, 26, 16, 26, 24, 26, 22, 0, 19, 26, 24, 26, 16, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {25, 22, 0, 21, 0, 0, 0, 17, 2, 20, 0, 0, 0, 21, 0, 19, 28}, // "2" es el spawn de pacman
            {0, 21, 0, 17, 26, 26, 18, 24, 24, 24, 18, 26, 26, 20, 0, 21, 0},
            {19, 24, 26, 28, 0, 0, 25, 18, 26, 18, 28, 0, 0, 25, 26, 24, 22},
            {21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21},
            {73, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 26, 76},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    /*
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



    public boolean isBonusAvailable() {
        return bonusAvailable;
    }


}