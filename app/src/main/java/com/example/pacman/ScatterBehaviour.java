package com.example.pacman;

interface ScatterBehaviour {
    void scatter(DrawingView dv, int ghostDirection, int xPos, int yPos);
    void moveOutOfBase(DrawingView dv, int ghostDirection, int xPos, int yPos);
}

