package com.mokki.mokkiapp.model;

import java.sql.Date;


public class Varaus {
    private int varausId, asiakasId, mokkiId;
    private Date alku, loppu, varausPvm;

    // JDBC vaatii java.sql.Date
    public Varaus(int varausId, int asiakasId, int mokkiId, Date alku, Date loppu, Date varausPvm) {
        this.varausId = varausId;
        this.asiakasId = asiakasId;
        this.mokkiId = mokkiId;
        this.alku = alku;
        this.loppu = loppu;
        this.varausPvm = varausPvm;
    }

    public int getVarausId() { return varausId; }
    public int getAsiakasId() { return asiakasId; }
    public int getMokkiId() { return mokkiId; }
    public Date getAlku() { return alku; }
    public Date getLoppu() { return loppu; }
    public Date getVarausPvm() { return varausPvm; }
}

