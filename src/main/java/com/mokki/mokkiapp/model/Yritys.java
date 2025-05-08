package com.mokki.mokkiapp.model;


public class Yritys extends Asiakas {
    private String yrityksenNimi, ytunnus;

    public Yritys(int asiakasId, String katuosoite, String email, String puhelin, Postialue postialue,
                  String yrityksenNimi, String ytunnus) {
        super(asiakasId, katuosoite, email, puhelin, postialue);
        this.yrityksenNimi = yrityksenNimi;
        this.ytunnus = ytunnus;
    }

    public String getYrityksenNimi() { return yrityksenNimi; }
    public String getYtunnus() { return ytunnus; }
}

