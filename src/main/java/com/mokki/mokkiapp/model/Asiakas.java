package com.mokki.mokkiapp.model;


public abstract class Asiakas {
    private int asiakasId;
    private String katuosoite, email, puhelin;
    private Postialue postialue;

    public Asiakas(int asiakasId, String katuosoite, String email, String puhelin, Postialue postialue) {
        this.asiakasId = asiakasId;
        this.katuosoite = katuosoite;
        this.email = email;
        this.puhelin = puhelin;
        this.postialue = postialue;
    }


    public int getAsiakasId() { return asiakasId; }
    public String getKatuosoite() { return katuosoite; }
    public String getEmail() { return email; }
    public String getPuhelin() { return puhelin; }
    public Postialue getPostialue() { return postialue; }


    public void setKatuosoite(String katuosoite) {
        this.katuosoite = katuosoite;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPuhelin(String puhelin) {
        this.puhelin = puhelin;
    }

    public void setPostialue(Postialue postialue) {
        this.postialue = postialue;
    }
}

