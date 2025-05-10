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

public class MokitController {

    // TextFields (Kentät)
    @FXML private TextField mokinNimiKentta;
    @FXML private TextField osoiteKentta;
    @FXML private TextField postinumeroKentta;
    @FXML private TextField hintaKentta;
    @FXML private TextField kuvausKentta;

    // TableView and Columns
    @FXML private TableView<?> mokitTaulukko;
    @FXML private TableColumn<?, ?> mokkiIdSarake;
    @FXML private TableColumn<?, ?> mokinNimiSarake;
    @FXML private TableColumn<?, ?> katuosoiteSarake;
    @FXML private TableColumn<?, ?> postinumeroSarake;
    @FXML private TableColumn<?, ?> hintaSarake;
    @FXML private TableColumn<?, ?> kuvausSarake;

    @FXML
    public void onTakaisinButtonClick(ActionEvent event) throws IOException {;
        Parent etusivuParent = FXMLLoader.load(getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);

        // Tämä rivi koodia palauttaa Stagen tiedot
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(etusivuScene);
        window.show();
    }

    // Placeholder methods for buttons (logic to be added later)
    @FXML
    private void handleLisaa(ActionEvent event) {
        System.out.println("Lisätään mökki...");
    }

    @FXML
    private void handleMuokkaa(ActionEvent event) {
        System.out.println("Muokataan mökkiä...");
    }

    @FXML
    private void handlePoista(ActionEvent event) {
        System.out.println("Poistetaan mökki...");
    }

}
