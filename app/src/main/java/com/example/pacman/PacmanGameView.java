package com.example.pacman;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PacmanGameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private PacmanGame pacmanGame;

    public PacmanGameView(Context context) {
        super(context);
    }

    public PacmanGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PacmanGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PacmanGameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void constructorHelper(){

    }

    public void drawMap(int[][]map){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //on touch event we calculate the swipe direction to order the pacman
        //where to move in next intersection or now if it is possible
        float[] previousSwipePosition,currentSwipePosition;
        previousSwipePosition=new float[2];
        currentSwipePosition=new float[2];


        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                previousSwipePosition[0] = event.getX();
                previousSwipePosition[1] = event.getY();
                //handler.postDelayed(longPressed, LONG_PRESS_TIME);
                break;

            case (MotionEvent.ACTION_UP):
                currentSwipePosition[0]= event.getX();
                currentSwipePosition[1] = event.getY();
                calculateSwipeDirection(previousSwipePosition, currentSwipePosition);
                //handler.removeCallbacks(longPressed);
                break;
            default:
                break;
        }
        return true;
    }

    private char calculateSwipeDirection(float[]previous, float[]current) {
        char pacmanNextDirection;
        float xDiff, yDiff;

        pacmanNextDirection=' ';
        xDiff = current[0]-previous[0];
        yDiff = current[1]-previous[2];
        // Direcciones
        // 'u' = up
        // 'r' = right
        // 'd' = down
        // 'l' = left
        // ' ' = no movement

        //Check which axis got higher distance
        //in order to know which direction the
        //swipe would be
        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            if (yDiff < 0) {
                pacmanNextDirection = 'u';
            } else if (yDiff > 0) {
                pacmanNextDirection = 'd';
            }
        } else {
            if (xDiff < 0) {
                pacmanNextDirection = 'l';
            } else if (xDiff > 0) {
                pacmanNextDirection = 'r';
            }
        }
        return pacmanNextDirection;
    }

}
