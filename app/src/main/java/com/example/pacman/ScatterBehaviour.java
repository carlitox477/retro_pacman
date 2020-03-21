package com.example.pacman;

interface ScatterBehaviour {
    void scatter(GameView dv, int ghostDirection, int xPos, int yPos);
    void moveOutOfBase(GameView dv, int ghostDirection, int xPos, int yPos);
}

