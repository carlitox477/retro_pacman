package com.example.pacman;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import Activities.PlayActivity;
import Behaviour_Chase.*;
import Character_package.Pacman;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private static final float SWIPE_THRESHOLD = 2;
    private static final float SWIPE_VELOCITY = 2;
    public static final int totalLevels=256;
    private GestureDetector gestureDetector;
    private PlayActivity playActivity;
    private GameMap gameMap;

    private Thread thread; //game thread
    private boolean fruitHasBeenInTheLevel, hasLifes;
    private Pacman pacman;

    private TextView scoreTv;
    private double score;
    private int[] bonusPos;

    private int bonusResetTime = 5000;
    private boolean bonusAvailable;

    private SurfaceHolder holder;
    private boolean canDraw = false;


    private int blockSize, level;                // Ancho de la pantalla, ancho del bloque
    private static int movementFluencyLevel=8; //this movement should be a multiple of the blocksize, if note the pacman will pass walls



    private CountdownGhostsState stateCounter;


    private Ghost[] ghosts = new Ghost[4];

    private Bitmap[] pacmanRight, pacmanDown, pacmanLeft, pacmanUp;

    int[] pacmanPos;

    private int totalFrame = 4;             // Cantidad total de animation frames por direccion
    private int currentPacmanFrame = 0;     // animation frame de pacman actual
    private int currentArrowFrame = 0;      // animation frame de arrow actual
    private long frameTicker;               // tiempo desde que el ultimo frame fue dibujado

    //----------------------------------------------------------------------------------------------
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
        this.gestureDetector = new GestureDetector(this);
        this.setFocusable(true);
        this.holder = getHolder();
        this.holder.addCallback(this);
        this.frameTicker = (long) (1000.0f / totalFrame);

        this.fruitHasBeenInTheLevel=false;
        this.hasLifes=true;

        this.gameMap=new GameMap();
        this.gameMap.loadMap1();

        int screenWidth=getResources().getDisplayMetrics().widthPixels;
        this.blockSize = ((screenWidth/this.gameMap.getMapWidth())/movementFluencyLevel)*movementFluencyLevel;
        this.holder.setFixedSize(blockSize*this.gameMap.getMapWidth(),blockSize*this.gameMap.getMapHeight());
        this.gameMap=new GameMap();
        this.gameMap.loadMap1();

        this.pacman=new Pacman("pacman","",this,this.movementFluencyLevel,this.blockSize);

        Draw.inicialize(this.blockSize,super.getResources());
        Ghost.loadCommonBitmaps(super.getContext().getResources(),this.blockSize);

        this.score=0;
        loadBitmapImages();
    }
    //----------------------------------------------------------------------------------------------
    public Pacman getPacman(){
        return this.pacman;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void loseGame(){
        //if the player have lost ol his lifes
    }

    public void onLose(){
        //if the player has been eaten by a ghost

    }

    public void checkWinLevel(){
        //player win the level if he has eaten all the pallet
        if(this.gameMap.countPallets()==0){
            Log.i("Game","WIN");
            this.level++;

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


    private void initGhosts() {
        int[][]spawnPositions=this.gameMap.getGhostsSpawnPositions();
        //start position
        // 5 blinky spawn [13, 11]
        // 6 pinky spawn [15,11]
        // 7 inky spawn [13,16]
        // 8 clyde spawn [15,16]
        ghosts[0] = new Ghost(this, "Blinky",this.blockSize,spawnPositions[0], new int[]{0, this.gameMap.getMapWidth()-1},new ChaseAgressive());
        ghosts[1] = new Ghost(this, "Pinky",this.blockSize,spawnPositions[1],new int[]{0,0},new ChaseAmbush());
        ghosts[2] = new Ghost(this, "Inky",this.blockSize,spawnPositions[2],new int[]{this.gameMap.getMapHeight()-1,0},new ChasePatrol());
        ghosts[3] = new Ghost(this, "Clyde",this.blockSize,spawnPositions[3],new int[]{this.gameMap.getMapHeight()-1,this.gameMap.getMapWidth()-1},new ChaseRandom());
        stateCounter = new CountdownGhostsState(this.ghosts, 0);
        stateCounter.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        Canvas canvas;

        initGhosts();
        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                Draw.drawMap(canvas, Color.BLUE,this.gameMap.getMap());
                updateFrame(System.currentTimeMillis());
                //Draw.drawBonus(canvas,this,gameMap.getMap(),this.bonusPos,this.bonusAvailable);
                //moveGhosts();
                Draw.drawGhosts(this.ghosts,canvas);
                this.pacman.move(this.gameMap.getMap(),this.ghosts,this,canvas);
                holder.unlockCanvasAndPost(canvas);


                //For test
                //this.canDraw=false;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void moveGhosts() {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].move();
        }


    }

    public double getScore() {
        return score;
    }

    public void eatPallet(int posXMap,int posYMap){
        this.score+=10;
        this.gameMap.getMap()[posYMap][posXMap]=0;
    }

    public void eatBonus(int posXMap,int posYMap){
        this.score+=200;
        this.gameMap.getMap()[posYMap][posXMap]=0;

    }

    public void eatSuperPallet(int posXMap,int posYMap){
        this.score+=50;
        this.gameMap.getMap()[posYMap][posXMap]=0;

        //Si hay un timer andando lo cancelo y ejecuto otro
        /*if (stateCounter != null)
            stateCounter.cancelTimer();

        stateCounter = new CountdownGhostsState(this, 2);
        stateCounter.start();*/
    }

    public void tryCreateBonus(){
        //only if pacman has eaten 20 pallets we should allow the fruit appear
        if(this.gameMap.getEatenPallets()>=20 && !this.fruitHasBeenInTheLevel){
            //to not allow the fruit be again in the level
            this.fruitHasBeenInTheLevel=true;
            new CountdownBonusThread(this).start();
        }
    }


    private void checkGhostDetection() {
        boolean shouldRespawn;
        //check if ghost have to respawn
        for (int i = 0; i < ghosts.length; i++) {
            shouldRespawn=ghosts[i].getState().isFrightened() &&
                    (Math.abs(this.pacmanPos[0]) <= ghosts[i].getxPos() + 5) &&
                    (Math.abs(this.pacmanPos[1]) <= ghosts[i].getyPos() + 5) &&
                    (Math.abs(this.pacmanPos[0]) >= ghosts[i].getxPos() - 5) &&
                    (Math.abs(this.pacmanPos[1]) >= ghosts[i].getyPos() - 5);
            if (shouldRespawn){
                ghosts[i].setRespawnBehaviour();
            }
        }


    }

    public Ghost getGhost(int i) {
        return ghosts[i];
    }

    public void setBonusAvailable() {
        //MODIFICAR
        //Se determina en que posicion del mapa se generara el bonus
        int[] spawn = this.gameMap.generateMapSpawn();
        this.bonusPos=spawn;

        this.gameMap.getMap()[this.bonusPos[1]][this.bonusPos[0]] = 9;
        this.bonusAvailable = true;
    }

    /*
    public void drawPath(Canvas canvas) {
        Node currentNode,nextNode;
        if (path != null) {
            paint = new Paint();
            paint.setColor(Color.RED);
            for (int i = 0; i < path.size() - 1; i++) {
                currentNode = path.get(i);
                nextNode = path.get(i + 1);
                canvas.drawLine(currentNode.x * blockSize, currentNode.y * blockSize, nextNode.x * blockSize, nextNode.y * blockSize, paint);
            }
        }
    }
     */

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
            currentPacmanFrame%=totalFrame;
        }
        if (gameTime > frameTicker + (50)) {
            currentArrowFrame++;
            currentArrowFrame%=7;
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


        if (Math.abs(diffX) > Math.abs(diffY)) {
            //right or left swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                if (diffX > 0) {
                    //right
                    this.pacman.setNextDirection('r');
                } else {
                    //left
                    this.pacman.setNextDirection('l');
                }
            }

        } else {
            //up or down swipe
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY) {
                if (diffY > 0) {
                    //down
                    this.pacman.setNextDirection('d');
                } else {
                    //up
                    this.pacman.setNextDirection('u');
                }
            }
        }


        return true;
    }

        private void passLevelAnimation(Canvas c) throws InterruptedException {
        for(int i=0; i<10;i++){
            Draw.drawMap(c, Color.WHITE,this.gameMap.getMap());
            Thread.sleep(500);
            Draw.drawMap(c, Color.BLUE,this.gameMap.getMap());
            Thread.sleep(500);
        }
        this.gameMap.resetMap();

        this.pacman.respawn();

        this.gameMap.resetMap();
        //we need to restart sound
        Thread.sleep(1000);
    }
}

