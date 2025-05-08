package com.mokki.mokkiapp.model;


public class Yksityishenkilo extends Asiakas {
    private String etunimi, sukunimi;

    public Yksityishenkilo(int asiakasId, String katuosoite, String email, String puhelin, Postialue postialue,
                           String etunimi, String sukunimi) {
        super(asiakasId, katuosoite, email, puhelin, postialue);
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
    }

    public String getEtunimi() { return etunimi; }
    public String getSukunimi() { return sukunimi; }
}

