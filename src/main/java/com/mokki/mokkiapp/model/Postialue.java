package com.mokki.mokkiapp.model;


public class Postialue {
    private String postinumero;
    private String kunta;
    private String maa;

    public Postialue(String postinumero, String kunta, String maa) {
        this.postinumero = postinumero;
        this.kunta = kunta;
        this.maa = maa;
    }

    public String getPostinumero() { return postinumero; }
    public String getKunta() { return kunta; }
    public String getMaa() { return maa; }
}
