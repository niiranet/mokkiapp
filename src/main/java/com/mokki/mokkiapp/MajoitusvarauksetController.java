package com.mokki.mokkiapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.time.Month;

public class MajoitusvarauksetController {

    @FXML
    public void onTakaisinButtonClick(ActionEvent event) throws IOException {
        Parent etusivuParent = FXMLLoader.load(getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);

        // Tämä rivi koodia palauttaa Stagen tiedot
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(etusivuScene);
        window.show();
    }

    @FXML
    private ComboBox<Integer> cottageComboBox;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private DatePicker alkupvmDatePicker;

    @FXML
    private DatePicker loppupvmDatePicker;

    @FXML
    private TextField asiakasIdTextField;

    @FXML
    private TableView<Varaustiedot> tableView;

    @FXML
    private TableColumn<Varaustiedot, LocalDate> alkuColumn;

    @FXML
    private TableColumn<Varaustiedot, LocalDate> loppuColumn;

    @FXML
    private TableColumn<Varaustiedot, Integer> varaajaColumn;

    @FXML
    private Button VaraaMokkiButton;

    @FXML
    public void initialize() {
        // Tähän tulee myöhemmin initialisointikoodia (esim. ComboBoxien täyttö)
        // ilman tietokantayhteyttä, voimme alustaa ne kovakoodatuilla arvoilla
        cottageComboBox.getItems().addAll(101, 102, 103);
        monthComboBox.getItems().addAll(Month.JANUARY.name(), Month.FEBRUARY.name(), Month.MARCH.name(),
                Month.APRIL.name(), Month.MAY.name(), Month.JUNE.name(), Month.JULY.name(),
                Month.AUGUST.name(), Month.SEPTEMBER.name(), Month.OCTOBER.name(),
                Month.NOVEMBER.name(), Month.DECEMBER.name());

        // Asetetaan oletusarvot
        cottageComboBox.setValue(101);
        monthComboBox.setValue(Month.JANUARY.name());

        // Initialisoidaan taulukon sarakkeet (ilman dataa vielä)
        alkuColumn.setCellValueFactory(new PropertyValueFactory<>("alkupvm"));
        loppuColumn.setCellValueFactory(new PropertyValueFactory<>("loppupvm"));
        varaajaColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));

        // Alustetaan tyhjä ObservableList taulukkoa varten
        ObservableList<Varaustiedot> tyhjatVaraukset = FXCollections.observableArrayList();
        tableView.setItems(tyhjatVaraukset);
    }

    @FXML
    private void handleVaraaMokkiButtonClick(ActionEvent event) {
        // Tähän tulee myöhemmin varauslogiikka
        System.out.println("Varaa mökki -painike painettu (ei vielä toiminnallisuutta).");
        // Voit tässä vaiheessa esimerkiksi vain tulostaa syöttökenttien arvot
        if (cottageComboBox.getValue() != null) {
            System.out.println("Valittu mökki: " + cottageComboBox.getValue());
        }
        if (alkupvmDatePicker.getValue() != null) {
            System.out.println("Alkupvm: " + alkupvmDatePicker.getValue());
        }
        if (loppupvmDatePicker.getValue() != null) {
            System.out.println("Loppupvm: " + loppupvmDatePicker.getValue());
        }
        if (asiakasIdTextField.getText() != null && !asiakasIdTextField.getText().isEmpty()) {
            System.out.println("Asiakas ID: " + asiakasIdTextField.getText());
        } else {
            System.out.println("Asiakas ID ei ole syötetty.");
        }
    }

    @FXML
    private void filterReservationsByMonth() {
        // Tähän tulee myöhemmin suodatuslogiikka
        if (monthComboBox.getValue() != null) {
            System.out.println("Valittu kuukausi: " + monthComboBox.getValue() + " (ei vielä suodatusta).");
        }
    }

    @FXML
    private void refreshReservations() {
        // Tähän tulee myöhemmin varauslistan päivityslogiikka
        if (cottageComboBox.getValue() != null) {
            System.out.println("Päivitetään varauksia mökille: " + cottageComboBox.getValue() + " (ei vielä tietokantayhteyttä).");
        }
    }
}
