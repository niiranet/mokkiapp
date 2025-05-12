package com.mokki.mokkiapp.testaus;

import java.util.Random;

public class Misc {

    public static String generoiYTunnus() {
        Random rand = new Random();

        // 7 ensimmäistä numeroa (satunnaisesti väliltä 1000000 - 9999999)
        int alkuosa = 1000000 + rand.nextInt(9000000);

        // Tarkistusnumero (viimeinen numero), yksinkertaisena versiona satunnainen 0–9
        int tarkistusosa = rand.nextInt(10);

        return alkuosa + "-" + tarkistusosa;
    }
}

