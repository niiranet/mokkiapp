package com.mokki.mokkiapp;

import com.mokki.mokkiapp.dao.MokkiDAO;
import com.mokki.mokkiapp.dao.MiscDAO;
import com.mokki.mokkiapp.model.Mokki;
import com.mokki.mokkiapp.model.Postialue;
import com.mokki.mokkiapp.viewmodel.MokkiViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class MuokkaaMokkiController {

    @FXML private TextField nimiField;
    @FXML private TextField osoiteField;
    @FXML private TextField postinumeroField;
    @FXML private TextField hintaField;
    @FXML private TextField kuvausField;

    private MokkiViewModel mokkiViewModel;
    private final MokkiDAO mokkiDAO = new MokkiDAO();
    private final MiscDAO miscDAO = new MiscDAO();

    public void setMokki(MokkiViewModel viewModel) {
        this.mokkiViewModel = viewModel;

        nimiField.setText(viewModel.getNimi());
        osoiteField.setText(viewModel.getKatuosoite());
        postinumeroField.setText(viewModel.getPostinumero());
        hintaField.setText(viewModel.getHinta().toString());
        kuvausField.setText(viewModel.getKuvaus());
    }

    @FXML
    private void handleTallenna() {
        try {
            String nimi = nimiField.getText();
            String osoite = osoiteField.getText();
            String postinumero = postinumeroField.getText();
            BigDecimal hinta = new BigDecimal(hintaField.getText());
            String kuvaus = kuvausField.getText();

            Postialue postialue = new Postialue(postinumero, "", "Suomi");
            miscDAO.lisaaPostialue(postialue);

            Mokki paivitetty = new Mokki(mokkiViewModel.getMokkiId(), nimi, osoite, hinta, kuvaus, postialue);
            MokkiDAO.paivitaMokki(paivitetty);

            // Sulje ikkuna
            Stage stage = (Stage) nimiField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePeruuta() {
        Stage stage = (Stage) nimiField.getScene().getWindow();
        stage.close();
    }
}
