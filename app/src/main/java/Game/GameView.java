package Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

import com.example.pacman.R;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import Game.Character_package.Ghost;
import Game.Character_package.Pacman;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private static final float SWIPE_THRESHOLD = 2;
    private static final float SWIPE_VELOCITY = 2;
    private static boolean CAN_DRAW = true;
    private static boolean GHOST_INICIALIZED=false;
    private GestureDetector gestureDetector;
    private GameManager gameManager;
    private Thread thread; //game thread
    private SurfaceHolder holder;
    private int blockSize;                // Ancho de la pantalla, ancho del bloque
    private static int movementFluencyLevel=8; //this movement should be a multiple of the blocksize and multiple of 4, if note the pacman will pass walls

    private int totalFrame = 4;             // Cantidad total de animation frames por direccion
    private int currentArrowFrame = 0;      // animation frame de arrow actual
    private long frameTicker;               // tiempo desde que el ultimo frame fue dibujado
    private boolean surfaceFirstCreation=false;
    private SoundPool soundPool;
    private int[] soundsId;
    private MediaPlayer mediaPlayer;
    private ReentrantLock surfaceLock;
    private static char WIN_LOSE_KEY='P';
    private static Semaphore WIN_LOSE_MUTEX;

    //----------------------------------------------------------------------------------------------
    //Constructors
    public GameView(Context context) {
        super(context);
        this.constructorHelper(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.constructorHelper(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.constructorHelper(context);

    }

    private void constructorHelper(Context context) {
        this.gestureDetector = new GestureDetector(this);
        setFocusable(true);
        this.holder = getHolder();
        this.holder.addCallback(this);
        this.frameTicker = (long) (1000.0f / totalFrame);

        this.gameManager=new GameManager();

        int screenWidth=getResources().getDisplayMetrics().widthPixels;
        this.blockSize = ((((screenWidth/this.gameManager.getGameMap().getMapWidth())/movementFluencyLevel)*movementFluencyLevel)/4)*4;
        this.holder.setFixedSize(blockSize*this.gameManager.getGameMap().getMapWidth(),blockSize*this.gameManager.getGameMap().getMapHeight());

        this.gameManager.getGameMap().loadBonusBitmaps(this.getBlockSize(),this.getResources(),this.getContext().getPackageName());
        this.gameManager.setPacman(new Pacman("pacman","",movementFluencyLevel,this.gameManager.getGameMap().getPacmanSpawnPosition(),this.blockSize,this.getResources(),this.getContext().getPackageName()));

        Ghost.loadCommonBitmaps(this.blockSize,this.getResources(),this.getContext().getPackageName());
        this.soundsId=new int[4];
        this.mediaPlayer=null;
        this.surfaceLock=new ReentrantLock(true);
    }

    //----------------------------------------------------------------------------------------------
    //Getters and setters
    public int getBlockSize() {
        return blockSize;
    }
    public GameManager getGameManager() {
        return gameManager;
    }
    public boolean isDrawing(){
        return CAN_DRAW;
    }
    //----------------------------------------------------------------------------------------------

    private synchronized void initGhost(){
        if(!GHOST_INICIALIZED){
            GHOST_INICIALIZED=true;
            this.gameManager.initGhosts(this.blockSize,this.getResources(),this.getContext().getPackageName(),movementFluencyLevel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        long gameTime;
        Canvas canvas;
        while (CAN_DRAW) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            this.initGhost();
            this.setFocusable(true);
            gameTime=System.currentTimeMillis();
            if(gameTime > frameTicker + (totalFrame * 15)){
                canvas = holder.lockCanvas();
                if(canvas!=null){
                    if(this.updateFrame(gameTime,canvas)){
                        try {
                            Thread.sleep(3000);
                        }catch (Exception e){}
                    }
                    holder.unlockCanvasAndPost(canvas);
                    this.checkWinLose();
                }
            }
        }

    }

    public char getWinLoseKey(){
        return WIN_LOSE_KEY;
    }

    private void checkWinLose(){
        if(this.gameManager.checkWinLevel()){
            CAN_DRAW=false;
            this.gameManager.cancelThreads();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
            //animation
            Log.i("Game","You win");
            WIN_LOSE_KEY='W';
            WIN_LOSE_MUTEX.release();
            //release
        }else if(!this.gameManager.getPacman().hasLifes()){
            //we lost

            CAN_DRAW=false;
            this.gameManager.cancelThreads();
            WIN_LOSE_KEY='L';
            WIN_LOSE_MUTEX.release();
            //animation
            Log.i("Game","You lose");
        }
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean updateFrame(long gameTime, Canvas canvas) {
        Pacman pacman;
        Ghost[] ghosts;
        boolean pacmanIsDeath;

        pacman=this.gameManager.getPacman();
        ghosts=this.gameManager.getGhosts();

        // Si el tiempo suficiente a transcurrido, pasar al siguiente frame
        frameTicker = gameTime;
        canvas.drawColor(Color.BLACK);
        this.gameManager.getGameMap().draw(canvas, Color.BLUE,this.blockSize,this.gameManager.getLevel());
        this.gameManager.moveGhosts(canvas,this.blockSize);
        pacmanIsDeath=pacman.move(this.gameManager,canvas,this.soundPool,this.soundsId);

        if(!pacmanIsDeath){
            // incrementar el frame
            pacman.changeFrame();
            for(int i=0; i<ghosts.length;i++){
                ghosts[i].changeFrame();
            }
            currentArrowFrame++;
            currentArrowFrame%=7;
        }else{
            pacman.setNextDirection(' ');
            for(int i=0; i<ghosts.length;i++){
                ghosts[i].respawn();
            }
        }
        return pacmanIsDeath;
    }

    public int getScore(){
        return this.getGameManager().getScore();
    }

    public void setSemaphores(Semaphore changeScoreSemaphore, Semaphore changeDirectionSemaphore, Semaphore winLoseMutex){
        this.gameManager.setChangeScoreSemaphore(changeScoreSemaphore);
        this.gameManager.getPacman().setChangeDirectionSemaphore(changeDirectionSemaphore);
        WIN_LOSE_MUTEX=winLoseMutex;
        Log.i("Semaphore", "setted");
    }

    private void createSoundPool(){
        //https://www.youtube.com/watch?v=fIWPSni7kUk&t=214s
        this.soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        this.soundsId[0]=this.soundPool.load(getContext(), R.raw.eatball,1);
        this.soundsId[1]=this.soundPool.load(getContext(), R.raw.pacman_eatfruit,1);
        this.soundsId[2]=this.soundPool.load(getContext(), R.raw.pacman_eatghost,1);
        this.soundsId[3]=this.soundPool.load(getContext(), R.raw.pacman_death,1);
    }

    //----------------------------------------------------------------------------------------------
    //Callback methods
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //on resume method
        this.surfaceLock.lock();
        Log.i("Surface","created");
        this.createSoundPool();
        CAN_DRAW = true;
        this.thread= new Thread(this);
        this.thread.start();

        if(this.surfaceFirstCreation){
            this.gameManager.onResume();
        }else{
            this.surfaceFirstCreation=true;
        }
        this.surfaceLock.unlock();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("Surface","Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //on pause method
        this.surfaceLock.lock();
        Log.i("Surface","destroyed");
        CAN_DRAW = false;
        this.gameManager.onPause();

        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // retry
            }
            break;
        }
        this.thread=null;
        this.soundPool.release();
        this.soundPool = null;
        this.surfaceLock.unlock();
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
        boolean result;
        float diffX, diffY;
        Pacman pacman;
        //Log.i("Fling", "detected");

        result=false;
        diffX = moveEvent.getX() - downEvent.getX();
        diffY = moveEvent.getY() - downEvent.getY();
        pacman=this.gameManager.getPacman();

        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY){
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX > 0) {
                    //right
                    pacman.setNextDirection('r');
                } else {
                    //left
                    pacman.setNextDirection('l');
                }
            }else{
                if (diffY > 0) {
                    //down
                    pacman.setNextDirection('d');
                } else {
                    //up
                    pacman.setNextDirection('u');
                }
            }
            result=true;
        }
        return result;
    }

}

