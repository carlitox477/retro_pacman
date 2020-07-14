package com.example.pacman;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Random;

import Activities.PlayActivity;
import path.Node;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private static final float SWIPE_THRESHOLD = 2;
    private static final float SWIPE_VELOCITY = 2;
    public static final int pacmanXSpawn=14;
    public static final int pacmanYSpawn=23;
    public static final int totalLevels=256;
    private GestureDetector gestureDetector;
    private PlayActivity playActivity;

    private Thread thread; //game thread
    private boolean fruitHasBeenInTheLevel, hasLifes;

    private TextView scoreTv;
    private double score;

    private int[][] map;
    private int[] bonusPos;

    private int bonusResetTime = 5000;
    private boolean bonusAvailable;
    private int totalPallettInStart;
    private int totalPallettEatenInLevel;

    private SurfaceHolder holder;
    private boolean canDraw = false;

    private Paint paint;

    private List<Node> path;

    private int screenWidth, blockSize, level;                // Ancho de la pantalla, ancho del bloque
    private static int movementFluencyLevel=8; //this movement should be a multiple of the blocksize, if note the pacman will pass walls



    private CountdownGhostsState stateCounter;


    private Ghost[] ghosts = new Ghost[4];

    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp;
    private Bitmap cherryBitmap;

    int[] pacmanPos;

    private int totalFrame = 4;             // Cantidad total de animation frames por direccion
    private int currentPacmanFrame = 0;     // animation frame de pacman actual
    private int currentArrowFrame = 0;      // animation frame de arrow actual
    private long frameTicker;               // tiempo desde que el ultimo frame fue dibujado

    private char direction = ' ';              // direccion del movimiento, movimiento inicial nulo
    private char nextDirection = ' ';          // Buffer para la siguiente direccion de movimiento tactil
    private char viewDirection = 'd';          // Direccion en la que pacman esta mirando

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

    public void loseGame(){
        //if the player have lost ol his lifes
    }

    public void onLose(){
        //if the player has been eaten by a ghost

    }

    public void checkWinLevel(){
        //player win the level if he has eaten all the pallet
        if(this.totalPallettEatenInLevel==totalPallettInStart){
            Log.i("Game","WIN");
            this.level++;
            this.totalPallettEatenInLevel=0;

            if(this.level<=totalLevels){
                //if it isn't the final level reboot
            }else{
                //Start new level
            }

        }
    }

    public void onWin(){
        //pacman has 256 levels, if the player wins level 256 he wins the game
    }


    public void startLevel(){

    }

    private void constructorHelper() {
        
        this.gestureDetector = new GestureDetector(this);
        this.setFocusable(true);
        this.holder = getHolder();
        this.holder.addCallback(this);
        this.frameTicker = (long) (1000.0f / totalFrame);


        this.map=this.loadOriginalMap();
        this.totalPallettInStart=this.countPallets();
        this.totalPallettEatenInLevel=0;
        this.fruitHasBeenInTheLevel=false;
        this.hasLifes=true;

        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
        this.blockSize = ((this.screenWidth/this.map[0].length)/movementFluencyLevel)*movementFluencyLevel;
        this.holder.setFixedSize(blockSize*this.map[0].length,blockSize*this.map.length);
        this.map=loadOriginalMap();

        paint = new Paint();
        paint.setColor(Color.WHITE);

        this.score=0;

        this.pacmanPos=new int[2];
        this.pacmanPos[0]=pacmanXSpawn*blockSize; //posX
        this.pacmanPos[1]=pacmanYSpawn*blockSize; //posY

        loadBitmapImages();


    }

    private void initGhosts() {
        int[][]spawnPositions={
                {14,12},
                {16,12},
                {14,17},
                {16,17}
        };

        //start position
        // 5 blinky spawn [14, 12]
        // 6 pinky spawn [16,12]
        // 7 inky spawn [14,17]
        // 8 clyde spawn [16,17]
        ghosts[0] = new Ghost(this, "Blinky",this.blockSize,spawnPositions[0]);
        ghosts[1] = new Ghost(this, "Pinky",this.blockSize,spawnPositions[1]);
        ghosts[2] = new Ghost(this, "Inky",this.blockSize,spawnPositions[2]);
        ghosts[3] = new Ghost(this, "Clyde",this.blockSize,spawnPositions[3]);
        stateCounter = new CountdownGhostsState(this, 0);
        stateCounter.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        initGhosts();
        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                drawMap(canvas, Color.BLUE);
                updateFrame(System.currentTimeMillis());
                drawBonus(canvas);
                moveGhosts();
                drawGhosts(canvas);
                movePacman(canvas);
                holder.unlockCanvasAndPost(canvas);


                //For test
                //this.canDraw=false;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


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
        int valueInMapPosition, posXinMap, posYinMap;
        if (this.pacmanPos[0]/this.blockSize==-1) {
            //to use left portal
            this.pacmanPos[0]=this.map[0].length*this.blockSize-this.blockSize-movementFluencyLevel;
        }else if(this.pacmanPos[0]==this.map[0].length*this.blockSize){
            this.pacmanPos[0]=0;
        }
        Log.i("Pacman position","["+this.pacmanPos[1]/blockSize+", "+this.pacmanPos[0]/blockSize+"]");




        //checkGhostDetection();



        posXinMap=this.pacmanPos[0] / blockSize;
        posYinMap=this.pacmanPos[1] / blockSize;
        if ((this.pacmanPos[1] % blockSize == 0) && (this.pacmanPos[0] % blockSize == 0)) {
            //If you are in the start of block pacman may took a decision
            /*
            AStar a = new AStar(this, 9, 8);
            path = a.findPathTo(this.pacmanPos[0] / blockSize, this.pacmanPos[1] / blockSize);
            */

            valueInMapPosition = this.map[posYinMap][posXinMap];

            if (valueInMapPosition == 2) {
                //pacman eat a  pallet
                this.score+=10;
                this.totalPallettEatenInLevel++;

                //Add sound

                //Add score
                this.map[posYinMap][posXinMap] = 0;
                //this.playActivity.score.setText(Score.doubleToStringScore(this.score));

            }
            if (valueInMapPosition == 3) {
                //pacman eat a super pallet

                //Add sound

                //Add score
                this.score+=50;
                this.totalPallettEatenInLevel++;
                /*
                this.map[posYinMap][posXinMap] = 0;
                if (stateCounter != null)
                    stateCounter.cancelTimer();

                stateCounter = new CountdownGhostsState(this, 2);
                stateCounter.start();
                */
            }
            if (valueInMapPosition == 9) {
                //pacman eat fruit
                this.map[posYinMap][posXinMap] = 0;

            }

            //only if pacman hace score 200 we should alow the fruit appear
            if(this.totalPallettEatenInLevel>=20 && !this.fruitHasBeenInTheLevel){
                //to not allow the fruit be again in the level
                this.fruitHasBeenInTheLevel=true;
                new CountdownBonusThread(this).start();
            }


            if ((posXinMap) > 0 * blockSize && (posXinMap) < this.map[0].length * blockSize) {
                Log.i("Move pacman","try");
                //if pacman move inside a valid position in X axis
                /* direction
                0 = up
                1 = right
                2 = down
                3 = left
                ' '= no movement
                 */

                if (!((nextDirection == 'l' && (this.map[posYinMap][(posXinMap - 1)]) == 1) || //check if it is a wall
                        (nextDirection == 'r' && (this.map[posYinMap][(posXinMap + 1)%this.map[1].length]) == 1) || //check if it is a wall
                        (nextDirection == 'u' && (this.map[posYinMap - 1][posXinMap]) == 1) || //check if it is a wall
                        (nextDirection == 'd' && (this.map[posYinMap + 1][posXinMap] == 1 || this.map[posYinMap + 1][posXinMap]==10) //check if it is a wall or the door of the ghost spawn point
                         ))) {
                    viewDirection = direction = nextDirection;
                }
                if ((direction == 'l' && (this.map[posYinMap][posXinMap - 1]) == 1) || //check if it is a wall
                        (direction == 'r' && (this.map[posYinMap][(posXinMap + 1)%this.map[1].length]) == 1) || //check if it is a wall
                        (direction == 'u' && (this.map[posYinMap - 1][posXinMap]) == 1) || //check if it is a wall
                        (direction == 'd' &&  (this.map[posYinMap + 1][posXinMap] == 1 || this.map[posYinMap + 1][posXinMap]==10) //check if it is a wall or the door of the ghost spawn point
                                )) {
                    direction = ' '; //set no move
                }


            }


        }

        if (posXinMap < 0) {
            //id we move previously and the position is out of range
            this.pacmanPos[0] = blockSize * this.map[0].length;
        }
        drawPacman(canvas);
        /*
        0 = up
        1 = right
        2 = down
        3 = left
        ' '= no movement
            */

        // Depending on the direction move the position of pacman
        switch (direction){
            case 'u':
                this.pacmanPos[1] -= movementFluencyLevel;
                break;
            case 'd':
                this.pacmanPos[1]+= movementFluencyLevel;
                break;
            case 'r':
                this.pacmanPos[0] += movementFluencyLevel;
                break;
            case 'l':
                this.pacmanPos[0] -= movementFluencyLevel;
                break;
            default:
                break;
        }

    }

    private void checkGhostDetection() {
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i].getState() == 2) {
                if (Math.abs(this.pacmanPos[0]) <= ghosts[i].getxPos() + 5) {
                    if (Math.abs(this.pacmanPos[1]) <= ghosts[i].getyPos() + 5) {
                        if (Math.abs(this.pacmanPos[0]) >= ghosts[i].getxPos() - 5) {
                            if (Math.abs(this.pacmanPos[1]) >= ghosts[i].getyPos() - 5) {
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
        int posX, posY;

        posX=this.pacmanPos[0];
        posY=this.pacmanPos[1];

        Log.i("Draw pacman", "position ["+posX+", "+posY+"]");
        switch (viewDirection) {
            case 'u':
                Log.i("Pacman draw", "draw up frame");
                canvas.drawBitmap(pacmanUp[currentPacmanFrame], posX, posY, paint);
                break;
            case 'r':
                Log.i("Pacman draw", "draw right frame");
                canvas.drawBitmap(pacmanRight[currentPacmanFrame], posX, posY, paint);
                break;
            case 'l':
                Log.i("Pacman draw", "draw left frame");
                canvas.drawBitmap(pacmanLeft[currentPacmanFrame], posX, posY, paint);
                break;
            case 'd':
                Log.i("Pacman draw", "draw down frame");
                canvas.drawBitmap(pacmanDown[currentPacmanFrame], posX, posY, paint);
                break;
            case ' ':
                canvas.drawBitmap(pacmanRight[currentPacmanFrame], posX, posY, paint);
                break;
            default:
                break;
        }
    }

    public Ghost getGhost(int i) {
        return ghosts[i];
    }

    public void drawMap(Canvas canvas, int colorId) {
        //Log.i("info", "Drawing map");
        float offset = 0;

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[0].length; x++) {
                int value = this.map[y][x];
                switch (value){
                    case 1:

                        paint.setStrokeWidth(2.5f);
                        paint.setColor(colorId);
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect((x * blockSize) + offset, (y * blockSize) + offset, (x * blockSize + blockSize) + offset, (y * blockSize + blockSize) + offset, paint);
                        break;
                    case 2:
                        paint.setColor(Color.WHITE);
                        canvas.drawCircle((x * blockSize + (blockSize / 2)) + offset, (y * blockSize + (blockSize / 2)) + offset, (float)0.15*blockSize, paint);
                        break;
                    case 3:
                        paint.setColor(Color.WHITE);
                        canvas.drawCircle((x * blockSize + (blockSize / 2)) + offset, (y * blockSize + (blockSize / 2)) + offset, (float)0.35*blockSize, paint);
                        break;
                    case 10:
                        paint.setStrokeWidth(2.5f);
                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawRect((x * blockSize) + offset, (y * blockSize) + blockSize/2, (x * blockSize + blockSize) + offset, (y * blockSize + blockSize) + offset, paint);
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

    private void drawBonus(Canvas canvas) {
        int value = this.map[this.bonusPos[1]][this.bonusPos[0]];
        if ((value == 9) && bonusAvailable) {
            canvas.drawBitmap(cherryBitmap, this.bonusPos[0] * blockSize, this.bonusPos[1] * blockSize, null);
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
        //MODIFICAR
        //Se determina en que posicion del mapa se generara el bonus
        int[] spawn = generateMapSpawn();
        this.bonusPos=spawn;

        this.map[this.bonusPos[1]][this.bonusPos[0]] = 9;
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
        //para ubicar el bonus
        int[] spawn = new int[2];
        int randomX, randomY;

        do{
            randomX = new Random().nextInt(this.map[0].length) + 1;
            randomY = new Random().nextInt(this.map.length) + 1;
        }while (this.map[randomY][randomX] != 0);

        spawn[0] = randomY;
        spawn[1] = randomX;

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
        this.loadBitmap(this.blockSize, 4, "pacman");
        //Añadir bitmap de cerezas bonus
        cherryBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.cherry), blockSize, blockSize, false);
    }


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


    //Callback methods
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        canDraw = true;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //change blocksize
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

    public int[][] getMap() {
        return this.map;
    }

    public int getPacmanDirection(){
        return direction;
    }

    public int getxPosPacman() {
        return this.pacmanPos[0];
    }

    public int getyPosPacman() {
        return this.pacmanPos[1];
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
        //Good
        //To swipe
        //https://www.youtube.com/watch?v=32rSs4tE-mc
        float diffX, diffY;

        diffX = moveEvent.getX() - downEvent.getX();
        diffY = moveEvent.getY() - downEvent.getY();


        if (Math.abs(diffX) > Math.abs(diffY)) {
            //right or left swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                if (diffX > 0) {
                    //right
                    nextDirection = 'r';
                } else {
                    //left
                    nextDirection = 'l';
                }
            }

        } else {
            //up or down swipe
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY) {
                if (diffY > 0) {
                    //down
                    nextDirection = 'd';
                } else {
                    //up
                    nextDirection = 'u';
                }
            }
        }


        return true;
    }


    private int[][] loadOriginalMap(){
        //Good
        int [][]sal;
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
        sal= new int[][]{
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
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //12
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  1,  1,  1, 10, 10,  1,  1,  1,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //13
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  0,  1,  5, 99, 99, 99, 99,  7,  1,  0,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //14
                { 0,  0,  0,  0,  0,  0,  2,  0,  0,  0,  1, 99, 99, 99, 99, 99, 99,  1,  0,  0,  0,  2,  0,  0,  0,  0,  0,  0}, //15
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  0,  1,  6, 99, 99, 99, 99,  8,  1,  0,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //16
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
        return sal;
    }

    public int countPallets(){
        //Good
        int count=0;
        for(int i=0; i<this.map.length;i++){
            for (int j=0;j<this.map[0].length;j++){
                if(this.map[i][j]==2 || this.map[i][j]==3){
                    count++;
                }
            }
        }
        return count;
    }

    private void passLevelAnimation(Canvas c) throws InterruptedException {
        for(int i=0; i<10;i++){
            this.drawMap(c, Color.WHITE);
            Thread.sleep(500);
            this.drawMap(c, Color.BLUE);
            Thread.sleep(500);
        }
        this.totalPallettEatenInLevel=0;

        this.pacmanPos[0]=pacmanXSpawn*blockSize; //posX
        this.pacmanPos[1]=pacmanYSpawn*blockSize; //posY

        this.map=this.loadOriginalMap();
        //we need to restart sound
        Thread.sleep(1000);
    }


}
