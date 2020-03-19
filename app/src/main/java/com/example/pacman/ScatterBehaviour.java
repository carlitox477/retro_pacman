package com.example.pacman;

interface ScatterBehaviour {
    int[] scatter(DrawingView dv, int ghostDirection, int xPos, int yPos);
    int[] moveOutOfBase(DrawingView dv, int ghostDirection, int xPos, int yPos);
}

