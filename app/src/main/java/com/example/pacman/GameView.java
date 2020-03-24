package com.example.pacman;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private static final float SWIPE_THRESHOLD = 2;
    private static final float SWIPE_VELOCITY = 2;

    GestureDetector gestureDetector;
    //SurfaceView actualiza lo que tiene mediante hilo
    private ReentrantLock gameLock; //Lock to draw or pause the game
    private Thread thread;

    private CountdownBonusThread bonusCounter;
    private int xPosBonus; //Added
    private int yPosBonus; //Added
    private int bonusResetTime = 5000;
    private boolean bonusAvailable;

    private SurfaceHolder holder;
    private boolean canDraw = false;

    private Paint paint;

    List<Node> path;

    private int screenWidth;                // Ancho de la pantalla


    private int blockSize;


    private CountdownGhostsState stateCounter;


    private Ghost[] ghosts = new Ghost[4];

    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp;
    private Bitmap cherryBitmap;


    private int xPosPacman;
    private int yPosPacman;
    private int totalFrame = 4;             // Cantidad total de animation frames por direccion
    private int currentPacmanFrame = 0;     // animation frame de pacman actual
    private int currentArrowFrame = 0;      // animation frame de arrow actual
    private long frameTicker;               // tiempo desde que el ultimo frame fue dibujado

    private int direction = 4;              // direccion del movimiento, movimiento inicial es a la derecha
    private int nextDirection = 4;          // Buffer para la siguiente direccion de movimiento tactil


    private int viewDirection = 2;          // Direccion en la que pacman esta mirando

    public static int LONG_PRESS_TIME = 750;  // Time in milliseconds
    final Handler handler = new Handler();


    public GameView(Context context) {
        super(context);
        this.constructorHelper();

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.constructorHelper();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.constructorHelper();

    }

    private void constructorHelper() {
        this.gameLock = new ReentrantLock(true);

        this.gestureDetector = new GestureDetector(this);
        setFocusable(true);
        holder = getHolder();
        holder.addCallback(this);
        frameTicker = (long) (1000.0f / totalFrame);


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

    private void initGhosts() {
        ghosts[0] = new Ghost(this, "Blinky");
        ghosts[1] = new Ghost(this, "Pinky");
        ghosts[2] = new Ghost(this, "Inky");
        ghosts[3] = new Ghost(this, "Clyde");
        stateCounter = new CountdownGhostsState(this, 0);
        stateCounter.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        initGhosts();
        while (canDraw) {
            //this.gameLock.lock();
            // update

            //draw

            //sleep

            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                drawMap(canvas);
                updateFrame(System.currentTimeMillis());
                drawBonus(canvas);
                moveGhosts();
                drawGhosts(canvas);
                movePacman(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawGhosts(Canvas canvas) {
        for (int i = 0; i < ghosts.length; i++) {
            canvas.drawBitmap(ghosts[i].getBitmap(), ghosts[i].getxPos(), ghosts[i].getyPos(), paint);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void moveGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].move();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void movePacman(Canvas canvas) {
        int value;
        if (xPosPacman >= blockSize * 19) {
            xPosPacman = 0;
        }
        checkGhostDetection();


        if ((xPosPacman % blockSize == 0) && (yPosPacman % blockSize == 0)) {

           /* AStar a = new AStar(this, 9, 8);
            path = a.findPathTo(xPosPacman / blockSize, yPosPacman / blockSize);
            */
            value = levellayout[yPosPacman / blockSize][xPosPacman / blockSize];

            if (value == 2) {
                levellayout[yPosPacman / blockSize][xPosPacman / blockSize] = 0;
            }
            if (value == 3) {
                levellayout[yPosPacman / blockSize][xPosPacman / blockSize] = 0;
                if (stateCounter != null)
                    stateCounter.cancelTimer();

                stateCounter = new CountdownGhostsState(this, 2);
                stateCounter.start();
            }
            if (value == 9) {
                levellayout[yPosPacman / blockSize][xPosPacman / blockSize] = 0;
                bonusCounter = new CountdownBonusThread(this);
                bonusCounter.start();

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
            yPosPacman += -blockSize / 9;
        } else if (direction == 1) {
            xPosPacman += blockSize / 9;
        } else if (direction == 2) {
            yPosPacman += blockSize / 9;
        } else if (direction == 3) {
            xPosPacman += -blockSize / 9;
        }

    }

    private void checkGhostDetection() {


        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i].getState() == 2) {
                if (Math.abs(xPosPacman) <= ghosts[i].getxPos() + 5) {
                    if (Math.abs(yPosPacman) <= ghosts[i].getyPos() + 5) {
                        if (Math.abs(xPosPacman) >= ghosts[i].getxPos() - 5) {
                            if (Math.abs(yPosPacman) >= ghosts[i].getyPos() - 5) {
                                ghosts[i].setRespawnBehaviour();
                            }
                        }
                    }


                }
            }

        }


    }


    // Method that draws pacman based on his viewDirection
    public void drawPacman(Canvas canvas) {
        //Log.i("info", "Drawing pacman");
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

    public Ghost getGhost(int i) {
        return ghosts[i];
    }

    public void drawMap(Canvas canvas) {
        //Log.i("info", "Drawing map");
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

    public void frightenGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setFrightenedBehaviour();
        }
    }

    public void scatterGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].setScatterBehaviour();
        }
    }

    public void resetGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            if(!(ghosts[i].getState() == 3))
                ghosts[i].setChaseBehaviour();
        }
    }

    public void setBonusAvailable() {
        //Se determina en que posicion del mapa se generara el bonus
        int[] spawn = generateMapSpawn();

        yPosBonus = spawn[0];
        xPosBonus = spawn[1];

        levellayout[yPosBonus][xPosBonus] = 9;
        this.bonusAvailable = true;


    }

    /*
    public void drawPath(Canvas canvas) {
        if (path != null) {
            paint = new Paint();
            paint.setColor(Color.RED);
            for (int i = 0; i < path.size() - 1; i++) {
                Node currentNode = path.get(i);
                Node nextNode = path.get(i + 1);
                canvas.drawLine(currentNode.x * blockSize, currentNode.y * blockSize, nextNode.x * blockSize, nextNode.y * blockSize, paint);
            }
        }
    }
     */

    public int[] generateMapSpawn() {
        //Se genera una posicion aleatoria valida en la cual pacman pueda moverse
        int[] spawn = new int[2];
        int randomX = new Random().nextInt(17) + 1;
        int randomY = new Random().nextInt(18) + 1;
        int value = levellayout[randomY][randomX];

        //Si en la posicion generada no es posible moverse
        if (value == 1 || value == 5 || value == 6 || value == 7 || value == 8)
            spawn = generateMapSpawn();
        else {
            spawn[0] = randomY;
            spawn[1] = randomX;
        }

        return spawn;
    }

    protected void loadBitmap(int spriteSize, int fpm, String characterName) {
        //fpm: frames per movement
        String packageName = getContext().getPackageName();
        Resources res = getResources();
        int idRight, idLeft, idDown, idUp;

        //We create Bitmap arrays deppending in fpm value
        //We should
        pacmanRight = new Bitmap[fpm];
        pacmanDown = new Bitmap[fpm];
        pacmanLeft = new Bitmap[fpm];
        pacmanUp = new Bitmap[fpm];

        // We add pacman's bitmap looking to the right
        for (int i = 0; i < fpm; i++) {
            //pacman movement image should be png with the name as "pacman_direction#
            // # is a number; direction must be "right", "left", "up" or "down"
            idRight = res.getIdentifier(characterName + "_right" + i, "drawable", packageName);
            idLeft = res.getIdentifier(characterName + "_left" + i, "drawable", packageName);
            idUp = res.getIdentifier(characterName + "_up" + i, "drawable", packageName);
            idDown = res.getIdentifier(characterName + "_down" + i, "drawable", packageName);

            //we add the bitmaps
            pacmanRight[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idRight), spriteSize, spriteSize, false);
            pacmanLeft[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idLeft), spriteSize, spriteSize, false);
            pacmanUp[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idUp), spriteSize, spriteSize, false);
            pacmanDown[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    res, idDown), spriteSize, spriteSize, false);
        }
    }

    private void loadBitmapImages() {
        // Escala los sprites en base al tamaño de la pantalla
        int spriteSize = screenWidth / 18;
        spriteSize = (spriteSize / 9) * 9;// Tamaño de pacman y fantasmas

        this.loadBitmap(spriteSize, 4, "pacman");
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


    // Method to capture touchEvents
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //To swipe
        //https://www.youtube.com/watch?v=32rSs4tE-mc
        this.gestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
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
    final int[][] levellayout = new int[][]{
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //this.holder=holder;
        canDraw = true;
        Thread t = new Thread(this);
        t.start();
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
        //Now
        //this.gameLock.unlock();

        //Previous

        canDraw = true;
        thread = new Thread(this);
        thread.start();


    }

    public void pause() {
        //New code
        //canDraw=false;
        //this.gameLock.lock();

        //Previous code

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

    public int[][] getLevellayout() {
        return levellayout;
    }

    public int getPacmanDirection(){
        return direction;
    }
    public int getxPosPacman() {
        return xPosPacman;
    }

    public int getyPosPacman() {
        return yPosPacman;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        //To swipe
        //https://www.youtube.com/watch?v=32rSs4tE-mc
        float diffX, diffY;

        diffX = moveEvent.getX() - downEvent.getX();
        diffY = moveEvent.getY() - downEvent.getY();

        // Directions
        // 0 = up
        // 1 = right
        // 2 = down
        // 3 = left
        // 4 = no movement

        if (Math.abs(diffX) > Math.abs(diffY)) {
            //right or left swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                if (diffX > 0) {
                    //right
                    nextDirection = 1;
                } else {
                    //left
                    nextDirection = 3;
                }
            }

        } else {
            //up or down swipe
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY) {
                if (diffY > 0) {
                    //down
                    nextDirection = 2;
                } else {
                    //up
                    nextDirection = 0;
                }
            }
        }


        return true;
    }
}
