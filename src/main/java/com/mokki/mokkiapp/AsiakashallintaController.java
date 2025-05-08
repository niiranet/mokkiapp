package com.mokki.mokkiapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class AsiakashallintaController {

    // TextFields (Kentät)
    @FXML private TextField asiakasIdKentta;
    @FXML private TextField katuosoiteKentta;
    @FXML private TextField postinumeroKentta;
    @FXML private TextField puhelinKentta;
    @FXML private TextField emailKentta;
    @FXML private TextField etunimiKentta;
    @FXML private TextField sukunimiKentta;
    @FXML private TextField yrityksenNimiKentta;
    @FXML private TextField yTunnusKentta;

    // TableView and Columns
    @FXML private TableView<?> asiakkaatTaulukko;
    @FXML private TableColumn<?, ?> asiakasIdSarake;
    @FXML private TableColumn<?, ?> katuosoiteSarake;
    @FXML private TableColumn<?, ?> postinumeroSarake;
    @FXML private TableColumn<?, ?> puhelinSarake;
    @FXML private TableColumn<?, ?> emailSarake;
    @FXML private TableColumn<?, ?> etunimiSarake;
    @FXML private TableColumn<?, ?> sukunimiSarake;
    @FXML private TableColumn<?, ?> yrityksennimiSarake;
    @FXML private TableColumn<?, ?> ytunnusSarake;

    @FXML
    public void onTakaisinButtonClick(ActionEvent event) throws IOException {
        Parent etusivuParent = FXMLLoader.load(getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);

        // Tämä rivi koodia palauttaa Stagen tiedot
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(etusivuScene);
        window.show();
    }

    // Lisää-asiakas-napin toiminta
    @FXML
    private void lisaaAsiakas(ActionEvent event) {
        // TODO: Lisää asiakkaan lisäystoiminto tähän
        System.out.println("Lisätään asiakas...");
    }

    // Muokkaa-asiakas-napin toiminta
    @FXML
    private void muokkaaAsiakas(ActionEvent event) {
        // TODO: Lisää asiakkaan muokkaustoiminto tähän
        System.out.println("Muokataan asiakasta...");
    }

    // Poista-asiakas-napin toiminta
    @FXML
    private void poistaAsiakas(ActionEvent event) {
        // TODO: Lisää asiakkaan poistotoiminto tähän
        System.out.println("Poistetaan asiakas...");
    }
}
