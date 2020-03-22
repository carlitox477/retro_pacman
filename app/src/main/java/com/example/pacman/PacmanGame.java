package com.example.pacman;

import java.util.concurrent.locks.ReentrantLock;

//map playable spots states: pacman, ghost, pallet, super pallet, empty

public class PacmanGame implements Runnable{
    private PacmanGameView view;
    private int[][] map;
    private int  [] bonusFruitPosition;
    private int fpm, currentFrame; //frame per movement
    double score;
    private short level;
    private boolean bonusHaveAppeared, bonusAppear, keepPlaying, winLevel,winGame;
    private Pacman pacman;
    private ReentrantLock gameLock;

    public PacmanGame(){
        this.currentFrame=0;
        this.gameLock=new ReentrantLock(true);
        this.map=newMap();

    }

    @Override
    public void run() {
        while (keepPlaying){
            this.gameLock.lock();
            if(this.currentFrame%4==0){
                //We update
            }
            //we draw


            this.gameLock.unlock();

            this.currentFrame++;
            this.currentFrame%=fpm;
        }

    }

    public void resume(){
        this.gameLock.unlock();
    }


    public void pause(){
        this.gameLock.lock();
    }

    private void update(){
        //pacman said where it move

        //Ghost receive pacman

    }

    private void draw(){

    }

    private static int [][]newMap(){
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
        int[][] levellayout= {
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
        return levellayout;
    }

}
