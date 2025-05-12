package com.mokki.mokkiapp.viewmodel;

import com.mokki.mokkiapp.model.Asiakas;
import com.mokki.mokkiapp.model.Yksityishenkilo;
import com.mokki.mokkiapp.model.Yritys;
import javafx.beans.property.*;

public class AsiakasUnifiedViewModel {
    private final IntegerProperty asiakasId;
    private final StringProperty nimi;
    private final StringProperty tyyppi;
    private final StringProperty email;
    private final StringProperty katuosoite;
    private final StringProperty ytunnus;
    private final StringProperty puhelin;
    private final StringProperty postialue;


    public AsiakasUnifiedViewModel(Asiakas asiakas) {
        this.asiakasId = new SimpleIntegerProperty(asiakas.getAsiakasId());
        this.katuosoite = new SimpleStringProperty(asiakas.getKatuosoite());
        this.email = new SimpleStringProperty(asiakas.getEmail());
        this.puhelin = new SimpleStringProperty(asiakas.getPuhelin());
        this.postialue = new SimpleStringProperty(
                asiakas.getPostialue().getPostinumero() + " " + asiakas.getPostialue().getKunta());

        if (asiakas instanceof Yksityishenkilo yksityinen) {
            this.nimi = new SimpleStringProperty(yksityinen.getEtunimi() + " " + yksityinen.getSukunimi());
            this.tyyppi = new SimpleStringProperty("Yksityishenkilö");
            this.ytunnus = new SimpleStringProperty(""); // not applicable
        } else if (asiakas instanceof Yritys yritys) {
            this.nimi = new SimpleStringProperty(yritys.getYrityksenNimi());
            this.tyyppi = new SimpleStringProperty("Yritys");
            this.ytunnus = new SimpleStringProperty(yritys.getYtunnus());
        } else {
            this.nimi = new SimpleStringProperty("Tuntematon");
            this.tyyppi = new SimpleStringProperty("?");
            this.ytunnus = new SimpleStringProperty("");
        }
    }

    public int getAsiakasId() { return asiakasId.get(); }
    public String getNimi() { return nimi.get(); }
    public String getTyyppi() { return tyyppi.get(); }
    public String getEmail() { return email.get(); }
    public String getKatuosoite() { return katuosoite.get(); }
    public String getYtunnus() { return ytunnus.get(); }
    public String getPuhelin() { return puhelin.get(); }
    public String getPostialue() { return postialue.get(); }

    public StringProperty ytunnusProperty() { return ytunnus; }
    public IntegerProperty asiakasIdProperty() { return asiakasId; }
    public StringProperty nimiProperty() { return nimi; }
    public StringProperty tyyppiProperty() { return tyyppi; }
    public StringProperty emailProperty() { return email; }
    public StringProperty katuosoiteProperty() { return katuosoite; }
    public StringProperty puhelinProperty() { return puhelin; }
    public StringProperty postialueProperty() { return postialue; }
}
