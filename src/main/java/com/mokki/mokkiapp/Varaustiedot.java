package com.mokki.mokkiapp;

import java.time.LocalDate;

public class Varaustiedot {
    private LocalDate alkupvm;
    private LocalDate loppupvm;
    private int asiakasId;

    // Luo oletuskonstruktori
    public Varaustiedot() {
    }

    // Luo konstruktori kenttien asettamista varten
    public Varaustiedot(LocalDate alkupvm, LocalDate loppupvm, int asiakasId) {
        this.alkupvm = alkupvm;
        this.loppupvm = loppupvm;
        this.asiakasId = asiakasId;
    }

    // Getterit
    public LocalDate getAlkupvm() {
        return alkupvm;
    }

    public LocalDate getLoppupvm() {
        return loppupvm;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    // Setterit (jos tarvitset niitä)
    public void setAlkupvm(LocalDate alkupvm) {
        this.alkupvm = alkupvm;
    }

    public void setLoppupvm(LocalDate loppupvm) {
        this.loppupvm = loppupvm;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }
}