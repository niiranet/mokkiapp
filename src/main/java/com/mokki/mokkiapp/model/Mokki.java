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
    public String getNimi() { return nimi; }
    public String getKatuosoite() { return katuosoite; }
    public BigDecimal getHinta() { return hinta; }
    public String getKuvaus() { return kuvaus; }
    public Postialue getPostialue() { return postialue; }
}



/*
public class Mokki {

private int MokkiId;
private String nimi;
private String katuosoite;
private String postinumero;
private double hinta;
private String kuvaus;

    // Konstruktori
    public Mokki(int mokkiId, String nimi, String katuosoite, String postinumero, double hinta, String kuvaus) {
        this.MokkiId = mokkiId;
        this.nimi = nimi;
        this.katuosoite = katuosoite;
        this.postinumero = postinumero;
        this.hinta = hinta;
        this.kuvaus = kuvaus;
    }

    // Getterit ja setterit...
    public int getMokkiId() { return MokkiId; }
    public String getNimi() { return nimi; }
    public void setNimi(String nimi) { this.nimi = nimi; }
    // Lisää muut getterit ja setterit

}
*/

