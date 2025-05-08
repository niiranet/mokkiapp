package com.mokki.mokkiapp.model;

import java.math.BigDecimal;

public class Mokki {
    private int mokkiId;
    private String nimi, katuosoite, kuvaus;
    private BigDecimal hinta;
    private Postialue postialue;

    public Mokki(int mokkiId, String nimi, String katuosoite, BigDecimal hinta, String kuvaus, Postialue postialue) {
        this.mokkiId = mokkiId;
        this.nimi = nimi;
        this.katuosoite = katuosoite;
        this.hinta = hinta;
        this.kuvaus = kuvaus;
        this.postialue = postialue;
    }

    public int getMokkiId() { return mokkiId; }
    public void setMokkiId(int mokkiId) { this.mokkiId = mokkiId; }
    public String getNimi() { return nimi; }
    public void setNimi(String nimi) { this.nimi = nimi; }
    public String getKatuosoite() { return katuosoite; }
    public void setKatuosoite(String katuosoite) { this.katuosoite = katuosoite; }
    public BigDecimal getHinta() { return hinta; }
    public void setHinta(BigDecimal hinta) { this.hinta = hinta; }
    public String getKuvaus() { return kuvaus; }
    public void setKuvaus(String kuvaus) { this.kuvaus = kuvaus; }
    public Postialue getPostialue() { return postialue; }
    public void setPostialue(Postialue postialue) { this.postialue = postialue; }

    @Override
    public String toString() {
        return nimi; // Näytetään nimi ComboBoxissa
    }
}