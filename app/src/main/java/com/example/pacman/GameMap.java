package com.example.pacman;

import java.util.Random;

public class GameMap {
    private int[][]ghostsSpawnPositions,ghostsScatterTarget,ghostsSpawnMovements,map, resetMap;
    private int[]pacmanSpawnPosition;
    private int initialPallets;

    public void loadMap1(){
        //31 * 28
        // 1 paredes
        // 2 pildoras
        // 3 superpildoras
        // 4 pacman spawn [24, 15]
        // 5 blinky spawn [14, 12]
        // 6 pinky spawn [16,12]
        // 7 inky spawn [14,17]
        // 8 clyde spawn [16,17]
        // 9 bonus
        //10 white bar
        this.resetMap= new int[][]{
                //1   2   3   4   5   6   7   8   9  10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27  28
                { 1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1}, //1
                { 1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1}, //2
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //3
                { 1,  3,  1, 99, 99,  1,  2,  1, 99, 99, 99,  1,  2,  1,  1,  2,  1, 99, 99, 99,  1,  2,  1, 99, 99,  1,  3,  1}, //4
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //5
                { 1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1}, //6
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //7
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //8
                { 1,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  1}, //9
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  0,  1,  1,  0,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //10
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  1,  1,  1,  0,  1,  1,  0,  1,  1,  1,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //11
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //12
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  1,  1,  1, 10, 10,  1,  1,  1,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //13
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  0,  1,  5, 99, 99, 99, 99,  7,  1,  0,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //14
                { 0,  0,  0,  0,  0,  0,  2,  0,  0,  0,  1, 99, 99, 99, 99, 99, 99,  1,  0,  0,  0,  2,  0,  0,  0,  0,  0,  0}, //15
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  0,  1,  6, 99, 99, 99, 99,  8,  1,  0,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //16
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  1,  1,  1,  1,  1,  1,  1,  1,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //17
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //18
                {99, 99, 99, 99, 99,  1,  2,  1,  1,  0,  1,  1,  1,  1,  1,  1,  1,  1,  0,  1,  1,  2,  1, 99, 99, 99, 99, 99}, //19
                { 1,  1,  1,  1,  1,  1,  2,  1,  1,  0,  1,  1,  1,  1,  1,  1,  1,  1,  0,  1,  1,  2,  1,  1,  1,  1,  1,  1}, //20
                { 1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1}, //21
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //22
                { 1,  2,  1,  1,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  2,  1,  1,  1,  1,  2,  1}, //23
                { 1,  3,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  2,  0,  4,  2,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  3,  1}, //24
                { 1,  1,  1,  2,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  2,  1,  1,  1}, //25
                { 1,  1,  1,  2,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  2,  1,  1,  1}, //26
                { 1,  2,  2,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  1,  1,  2,  2,  2,  2,  2,  2,  1}, //27
                { 1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1}, //28
                { 1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1,  1,  2,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  2,  1}, //29 (GG)
                { 1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1}, //30 (GG)
                { 1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1}  //31 (GG)
        };
        this.resetMap();
        this.initialPallets=this.countPallets();
        this.pacmanSpawnPosition=new int[]{14, 23};
        this.ghostsSpawnPositions=new int[][]{
                {13,11},
                {15,11},
                {13,16},
                {15,13}
        };
        this.ghostsScatterTarget =new int[][]{
                {0,this.map[0].length-1},
                {0,0},
                {this.map.length-1,0},
                {this.map.length-1,this.map[0].length-1}
        };
    }

    public int[] getPacmanSpawnPosition() {
        return pacmanSpawnPosition;
    }

    public int[][] getGhostsSpawnPositions() {
        return ghostsSpawnPositions;
    }

    public int[][] getGhostsScatterTarget() {
        return ghostsScatterTarget;
    }

    public int[] generateMapSpawn() {
        //Se genera una posicion aleatoria valida en la cual pacman pueda moverse
        //para ubicar el bonus
        int[] spawn;
        int randomX, randomY;

        spawn = new int[2];

        do{
            randomX = new Random().nextInt(this.map[0].length);
            randomY = new Random().nextInt(this.map.length);
        }while (this.map[randomY][randomX] != 0);

        spawn[0] = randomY;
        spawn[1] = randomX;

        return spawn;
    }

    public int[][]getMap(){
        return this.map;
    }

    public int getMapWidth(){
        return this.map[0].length;
    }

    public int getMapHeight(){
        return this.map.length;
    }

    public void resetMap(){
        for (int i=0;i<this.resetMap.length;i++){
            for(int j=0;j<this.resetMap[0].length;j++){
                this.map[i][j]=this.resetMap[i][j];
            }
        }
    }

    public int countPallets(){
        //Good
        int count,currentValue;

        count=0;
        for(int i=0; i<this.map.length;i++){
            for (int j=0;j<this.map[0].length;j++){
                currentValue=this.map[i][j];
                if(currentValue==2 || currentValue==3){
                    count++;
                }
            }
        }
        return count;
    }

    public int getEatenPallets(){
        return this.initialPallets-this.countPallets();
    }
}
