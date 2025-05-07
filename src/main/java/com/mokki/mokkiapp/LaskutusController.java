package com.mokki.mokkiapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class LaskutusController {

    @FXML
    private RadioButton rbKaikki;
    @FXML
    private RadioButton rbAvoimet;
    @FXML
    private RadioButton rbMaksetut;
    @FXML
    private ToggleGroup tgNaytaLaskut;
    @FXML
    private TableView twLaskutus;

    @FXML
    public void onTakaisinButtonClick(ActionEvent event) throws IOException {;
        Parent etusivuParent = FXMLLoader.load(getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);

        // Tämä rivi koodia palauttaa Stagen tiedot
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(etusivuScene);
        window.show();
    }

    @FXML
    private void onRadioButtonClick(ActionEvent event) {
        if(rbKaikki.isSelected() == true) {
            //Haetaan twLaskutus table view näkymään tiedot kaikista laskuista
        }

        if(rbAvoimet.isSelected() == true) {
            //Haetaan twLaskutus table view näkymään tiedot kaikista laskuista
        }

        if(rbMaksetut.isSelected() == true) {
            //Haetaan twLaskutus table view näkymään tiedot kaikista laskuista
        }

    }

    @FXML
    private void onUusiLaskuButtonClick(ActionEvent event) throws IOException {;
        //Luo uuden Pop up ikkunan, jossa voi tehdä uusia laskuja
    }

    @FXML
    private void onUusiMaksuButtonClick(ActionEvent event) throws IOException {;
        //Luo uuden Pop up ikkunan, jossa voi tehdä uusia laskuja
    }



}
