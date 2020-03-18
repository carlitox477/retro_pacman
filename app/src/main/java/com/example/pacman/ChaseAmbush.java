package com.example.pacman;

public class ChaseAmbush implements ChaseBehaviour {


    @Override
    public int[] chase(DrawingView dv, int currentGhostDirection, int currentX, int currentY) {

        int xDistance;
        int yDistance;
        switch (dv.getPacmanDirection()) {
            case 0:
                xDistance = dv.getxPosPacman() - currentX;
                yDistance = dv.getyPosPacman() - currentY - (4 * dv.getBlockSize());
                break;
            case 1:
                xDistance = dv.getxPosPacman() - currentX + (4 * dv.getBlockSize());
                yDistance = dv.getyPosPacman() - currentY;
                break;
            case 2:
                xDistance = dv.getxPosPacman() - currentX;
                yDistance = dv.getyPosPacman() - currentY + (4 * dv.getBlockSize());
                break;
            case 3:
                xDistance = dv.getxPosPacman() - currentX - (4 * dv.getBlockSize());
                yDistance = dv.getyPosPacman() - currentY;
                break;
        }


        if (dv.getPacmanDirection() == 0) {
            xDistance = dv.getxPosPacman() - currentX;
            yDistance = dv.getyPosPacman() - currentY + (2 * dv.getBlockSize());
        }
        xDistance = dv.getxPosPacman() - currentX;
        yDistance = dv.getyPosPacman() - currentY;

        short ch;

        int xPosGhost = currentX;
        int yPosGhost = currentY;
        int ghostDirection = currentGhostDirection;


        if ((xPosGhost % dv.getBlockSize() == 0) && (yPosGhost % dv.getBlockSize() == 0)) {
            ch = dv.getLevelData()[yPosGhost / dv.getBlockSize()][xPosGhost / dv.getBlockSize()];

            if (xPosGhost >= dv.getBlockSize() * 17) {
                xPosGhost = 0;
            }
            if (xPosGhost < 0) {
                xPosGhost = dv.getBlockSize() * 17;
            }


            if (xDistance >= 0 && yDistance >= 0) { // Move right and down
                if ((ch & 4) == 0 && (ch & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 1;
                    } else {
                        ghostDirection = 2;
                    }
                } else if ((ch & 4) == 0) {
                    ghostDirection = 1;
                } else if ((ch & 8) == 0) {
                    ghostDirection = 2;
                } else
                    ghostDirection = 3;
            }
            if (xDistance >= 0 && yDistance <= 0) { // Move right and up
                if ((ch & 4) == 0 && (ch & 2) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 1;
                    } else {
                        ghostDirection = 0;
                    }
                } else if ((ch & 4) == 0) {
                    ghostDirection = 1;
                } else if ((ch & 2) == 0) {
                    ghostDirection = 0;
                } else ghostDirection = 2;
            }
            if (xDistance <= 0 && yDistance >= 0) { // Move left and down
                if ((ch & 1) == 0 && (ch & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 3;
                    } else {
                        ghostDirection = 2;
                    }
                } else if ((ch & 1) == 0) {
                    ghostDirection = 3;
                } else if ((ch & 8) == 0) {
                    ghostDirection = 2;
                } else ghostDirection = 1;
            }
            if (xDistance <= 0 && yDistance <= 0) { // Move left and up
                if ((ch & 1) == 0 && (ch & 2) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 3;
                    } else {
                        ghostDirection = 0;
                    }
                } else if ((ch & 1) == 0) {
                    ghostDirection = 3;
                } else if ((ch & 2) == 0) {
                    ghostDirection = 0;
                } else ghostDirection = 2;
            }
            // Handles wall collisions
            if ((ghostDirection == 3 && (ch & 1) != 0) ||
                    (ghostDirection == 1 && (ch & 4) != 0) ||
                    (ghostDirection == 0 && (ch & 2) != 0) ||
                    (ghostDirection == 2 && (ch & 8) != 0)) {
                ghostDirection = 4;
            }
        }

        if (ghostDirection == 0) {
            yPosGhost += -dv.getBlockSize() / 20;
        } else if (ghostDirection == 1) {
            xPosGhost += dv.getBlockSize() / 20;
        } else if (ghostDirection == 2) {
            yPosGhost += dv.getBlockSize() / 20;
        } else if (ghostDirection == 3) {
            xPosGhost += -dv.getBlockSize() / 20;
        }

        int[] nextPos = {xPosGhost, yPosGhost, ghostDirection};

        return nextPos;
    }
}
