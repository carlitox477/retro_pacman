package com.example.pacman;

public class PacmanGame implements Runnable{
    private double actualScore;
    private short level;
    private  int [] pacmanPosition, pinkPosition, bluePosition,redPosition, orangePosition;

    public PacmanGame(){
        this.actualScore=0;
        this.level=1;

    }

    @Override
    public void run() {
        boolean stillPlaying=true;
        while (stillPlaying){

        }


    }
    //here we manage the logic of the game
}
