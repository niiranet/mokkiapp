package com.mokki.mokkiapp.model;

import java.math.BigDecimal;
import java.sql.Date;


public class Lasku {
    private int laskuId;
    private int varausId;
    private BigDecimal summa;
    private String tilinro;
    private Date erapaiva;
    private Date maksupvm;
    private boolean maksettu;

    public Lasku(int laskuId, int varausId, BigDecimal summa, String tilinro, Date erapaiva, Date maksupvm, boolean maksettu) {
        this.laskuId = laskuId;
        this.varausId = varausId;
        this.summa = summa;
        this.tilinro = tilinro;
        this.erapaiva = erapaiva;
        this.maksupvm = maksupvm;
        this.maksettu = maksettu;
    }

    // Getterit
    public int getLaskuId() { return laskuId; }
    public int getVarausId() { return varausId; }
    public BigDecimal getSumma() { return summa; }
    public String getTilinro() { return tilinro; }
    public Date getErapaiva() { return erapaiva; }
    public Date getMaksupvm() { return maksupvm; }
    public boolean isMaksettu() { return maksettu; }

    // Setterit (valinnainen, jos tarvitset muokattavuutta)
    public void setMaksettu(boolean maksettu) { this.maksettu = maksettu; }
    public void setMaksupvm(Date maksupvm) { this.maksupvm = maksupvm; }
}

