package com.mokki.mokkiapp.model;


public class Raportti {
    private Varaus varaus;
    private Asiakas asiakas;
    private Mokki mokki;
    private Lasku lasku;

    public Raportti(Varaus varaus, Asiakas asiakas, Mokki mokki, Lasku lasku) {
        this.varaus = varaus;
        this.asiakas = asiakas;
        this.mokki = mokki;
        this.lasku = lasku;
    }

    public Varaus getVaraus() {
        return varaus;
    }

    public Asiakas getAsiakas() {
        return asiakas;
    }

    public Mokki getMokki() {
        return mokki;
    }

    public Lasku getLasku() {
        return lasku;
    }


    /**
     * Lähinnä testaus tarkoituksiin, mutta tätä voi käyttää myös
     * tekstimuotoisen raportin palauttamiseen.
     * @return String
     */
    @Override
    public String toString() {
        String asiakasNimi = asiakas instanceof Yksityishenkilo yksityis ?
                yksityis.getEtunimi() + " " + yksityis.getSukunimi() :
                ((Yritys) asiakas).getYrityksenNimi() + " (" + ((Yritys) asiakas).getYtunnus() + ")";

        String asiakasOsoite = asiakas.getKatuosoite() + ", " +
                asiakas.getPostialue().getPostinumero() + " " +
                asiakas.getPostialue().getKunta();

        String mokkiOsoite = mokki.getKatuosoite() + ", " +
                mokki.getPostialue().getPostinumero() + " " +
                mokki.getPostialue().getKunta();

        return """
        --- Majoituksen raportointi ---
        Varaus ID: %d
        Tehty: %s
        Alku: %s
        Loppu: %s

        Mökki: %s
        Osoite: %s

        Asiakas: %s
        Osoite: %s
        Sähköposti: %s
        Puhelin: %s

        Hinta: %.2f €
        Tilinumero: %s
        Eräpäivä: %s
        Maksettu: %s %s
        """.formatted(
                varaus.getVarausId(),
                varaus.getVarausPvm(),
                varaus.getAlku(),
                varaus.getLoppu(),

                mokki.getNimi(),
                mokkiOsoite,

                asiakasNimi,
                asiakasOsoite,
                asiakas.getEmail(),
                asiakas.getPuhelin(),

                lasku.getSumma(),
                lasku.getTilinro(),
                lasku.getErapaiva(),
                lasku.isMaksettu() ? "Kyllä" : "Ei",
                lasku.getMaksupvm() != null ? "(" + lasku.getMaksupvm() + ")" : ""
        );
    }



}

