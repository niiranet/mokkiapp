package com.mokki.mokkiapp; // Varmista, että pakkaus on oikein

import java.time.LocalDate;

public class Varaustiedot {
    private int varausId;
    private int asiakasId;
    private int mokkiId;
    private LocalDate alkupvm;
    private LocalDate loppupvm;
    private LocalDate varausPvm;

    // Luo oletuskonstruktori
    public Varaustiedot() {
    }

    // Luo konstruktori kenttien asettamista varten (voit lisätä tarpeen mukaan)
    public Varaustiedot(LocalDate alkupvm, LocalDate loppupvm, int asiakasId) {
        this.alkupvm = alkupvm;
        this.loppupvm = loppupvm;
        this.asiakasId = asiakasId;
    }

    // UUSI KONSTRUKTORI LISÄTTY TÄHÄN:
    public Varaustiedot(int varausId, int asiakasId, int mokkiId, LocalDate alkupvm, LocalDate loppupvm, LocalDate varausPvm) {
        this.varausId = varausId;
        this.asiakasId = asiakasId;
        this.mokkiId = mokkiId;
        this.alkupvm = alkupvm;
        this.loppupvm = loppupvm;
        this.varausPvm = varausPvm;
    }

    // Getterit
    public int getVarausId() {
        return varausId;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public int getMokkiId() {
        return mokkiId;
    }

    public LocalDate getAlkupvm() {
        return alkupvm;
    }

    public LocalDate getLoppupvm() {
        return loppupvm;
    }

    public LocalDate getVarausPvm() {
        return varausPvm;
    }

    // Setterit
    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    public void setAlkupvm(LocalDate alkupvm) {
        this.alkupvm = alkupvm;
    }

    public void setLoppupvm(LocalDate loppupvm) {
        this.loppupvm = loppupvm;
    }

    public void setVarausPvm(LocalDate varausPvm) {
        this.varausPvm = varausPvm;
    }

    @Override
    public String toString() {
        return "Varaustiedot{" +
                "varausId=" + varausId +
                ", asiakasId=" + asiakasId +
                ", mokkiId=" + mokkiId +
                ", alkupvm=" + alkupvm +
                ", loppupvm=" + loppupvm +
                ", varausPvm=" + varausPvm +
                '}';
    }
}