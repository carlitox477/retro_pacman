package com.example.pacman;

public class Juego {
    private static final int CANTIDAD_NIVELES=256;
    private int cantidad_de_pallets_inicio;

    private int vidas;
    private int puntuacion;
    private int nivel_actual;
    private int cantidad_de_pallets_actual;
    private int[][] mapa;
    private Character pacman;
    private Ghost[] fantasmas;


    public void cambiarDireccionPacman(char direccion){

    }

    private void moverPacman(){
        //Cambiar posición pacman
        //Cambiar posición fantasma

        //Si coincide con posicion de fantasmas y esta en estado super matar fantasma y sumar puntos
        //Si coincide con posicion de fantasmas y no esta en estado super matar
        //Si coincide con pallet normal sumar puntos
        //Si coincide con super pallet normal sumar puntos y cambiar comportamiento fantasma


    }

    private void inciarJuego(){

    }

    private void iniciarNivel(){

    }

    private void pasarNivel(){

    }

    private void ganar(){

    }

    private void perderVida(){

    }

    private void cargar_mapa(){
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
        //99 casilla no jugable
        //0 casilla jugable
        this.mapa= new int[][]{
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
    }
}
