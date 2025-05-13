package com.mokki.mokkiapp.viewmodel;

import com.mokki.mokkiapp.model.Mokki;
import com.mokki.mokkiapp.model.Postialue;
import javafx.beans.property.*;

import java.math.BigDecimal;

public class MokkiViewModel {

    private final IntegerProperty mokkiId = new SimpleIntegerProperty();
    private final StringProperty nimi = new SimpleStringProperty();
    private final StringProperty katuosoite = new SimpleStringProperty();
    private final StringProperty kuvaus = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> hinta = new SimpleObjectProperty<>();
    private final StringProperty postinumero = new SimpleStringProperty();

    public MokkiViewModel(Mokki mokki) {
        this.mokkiId.set(mokki.getMokkiId());
        this.nimi.set(mokki.getNimi());
        this.katuosoite.set(mokki.getKatuosoite());
        this.kuvaus.set(mokki.getKuvaus());
        this.hinta.set(mokki.getHinta());
        this.postinumero.set(mokki.getPostialue().getPostinumero());
    }

    // Getters for Properties (needed for bindings)
    public IntegerProperty mokkiIdProperty() { return mokkiId; }
    public StringProperty nimiProperty() { return nimi; }
    public StringProperty katuosoiteProperty() { return katuosoite; }
    public StringProperty kuvausProperty() { return kuvaus; }
    public ObjectProperty<BigDecimal> hintaProperty() { return hinta; }
    public StringProperty postinumeroProperty() { return postinumero; }

    // Optional: convert ViewModel back to domain object
    public Mokki toMokki(Postialue postialue) {
        return new Mokki(mokkiId.get(), nimi.get(), katuosoite.get(), hinta.get(), kuvaus.get(), postialue);
    }
}
