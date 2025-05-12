package com.mokki.mokkiapp.util;

import java.util.Random;


public class Maksutapahtuma {
    // Todennäköisyys palauttaa null (0.05 = 5 %)
    private static final double NULL_PROBABILITY = 0.05;


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(suoritaMaksu());
        }
    }


    /**
     * Simuloi maksutapahtumaa.
     * Palauttaa tilinumeron IBAN muodossa.
     * Voi myös palauttaa "null", eli käteismaksu.
     */
    public static String suoritaMaksu() {
        Random rand = new Random();

        // Tarkistetaan palautetaanko null
        if (rand.nextDouble() < NULL_PROBABILITY) {
            return null;
        }

        // FI + satunnaiset 2-numeroinen tarkiste
        String checkDigits = String.format("%02d", rand.nextInt(100));

        // Loput 14 numeroa satunnaisesti
        StringBuilder bban = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            bban.append(rand.nextInt(10));
        }

        return "FI" + checkDigits + bban.toString();
    }


}
