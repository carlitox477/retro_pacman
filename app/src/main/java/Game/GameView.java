package Game;

import android.content.Context;
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
import Game.Character_package.Ghost;
import Game.Character_package.Pacman;

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private static final float SWIPE_THRESHOLD = 2;
    private static final float SWIPE_VELOCITY = 2;
    private boolean GHOST_INICIALIZED=false;
    private GestureDetector gestureDetector;
    private PlayActivity playActivity;
    private GameManager gameManager;
    private Thread thread; //game thread
    private TextView scoreTv;
    private SurfaceHolder holder;
    private boolean canDraw = false;
    private int blockSize;                // Ancho de la pantalla, ancho del bloque
    private static int movementFluencyLevel=8; //this movement should be a multiple of the blocksize and multiple of 4, if note the pacman will pass walls

    private int totalFrame = 4;             // Cantidad total de animation frames por direccion
    private int currentArrowFrame = 0;      // animation frame de arrow actual
    private long frameTicker;               // tiempo desde que el ultimo frame fue dibujado

    //----------------------------------------------------------------------------------------------
    //Constructors
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

        this.gameManager=new GameManager();

        int screenWidth=getResources().getDisplayMetrics().widthPixels;
        this.blockSize = ((((screenWidth/this.gameManager.getGameMap().getMapWidth())/movementFluencyLevel)*movementFluencyLevel)/4)*4;
        this.holder.setFixedSize(blockSize*this.gameManager.getGameMap().getMapWidth(),blockSize*this.gameManager.getGameMap().getMapHeight());

        this.gameManager.getGameMap().loadBonusBitmaps(this);
        this.gameManager.setPacman(new Pacman("pacman","",this,this.movementFluencyLevel));

        Ghost.loadCommonBitmaps(this);
    }
    //----------------------------------------------------------------------------------------------
    //Getters and setters
    public int getBlockSize() {
        return blockSize;
    }
    public GameManager getGameManager() {
        return gameManager;
    }
    public int getMovementFluencyLevel(){return movementFluencyLevel;}
    //----------------------------------------------------------------------------------------------

    private synchronized void initGhost(){
        if(!GHOST_INICIALIZED){
            GHOST_INICIALIZED=true;
            this.gameManager.initGhosts(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        Canvas canvas;
        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            this.initGhost();
            canvas = holder.lockCanvas();
            if (canvas != null) {
                if(updateFrame(System.currentTimeMillis(), canvas)){
                    try {
                        Thread.sleep(3000);
                    }catch (Exception e){}
                }
                holder.unlockCanvasAndPost(canvas);

                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    /*
    public void drawPath(Canvas canvas) {
        Paint paint;
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

        pacmanIsDeath=false;
        pacman=this.gameManager.getPacman();
        ghosts=this.gameManager.getGhosts();

        // Si el tiempo suficiente a transcurrido, pasar al siguiente frame
        if (gameTime > frameTicker + (totalFrame * 15)) {
            frameTicker = gameTime;
            canvas.drawColor(Color.BLACK);
            this.gameManager.getGameMap().draw(canvas, Color.BLUE,this.blockSize,this.gameManager.getLevel());
            this.gameManager.moveGhosts(canvas,this.blockSize);
            pacmanIsDeath=pacman.move(this.gameManager,canvas);
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

        }
        return pacmanIsDeath;
    }

    //----------------------------------------------------------------------------------------------
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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    //----------------------------------------------------------------------------------------------
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
        Pacman pacman;

        diffX = moveEvent.getX() - downEvent.getX();
        diffY = moveEvent.getY() - downEvent.getY();
        pacman=this.gameManager.getPacman();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            //right or left swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                if (diffX > 0) {
                    //right
                    pacman.setNextDirection('r');
                } else {
                    //left
                    pacman.setNextDirection('l');
                }
            }

        } else {
            //up or down swipe
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY) {
                if (diffY > 0) {
                    //down
                    pacman.setNextDirection('d');
                } else {
                    //up
                    pacman.setNextDirection('u');
                }
            }
        }
        return true;
    }

}

