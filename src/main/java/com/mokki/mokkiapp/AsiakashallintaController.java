package com.mokki.mokkiapp;

import com.mokki.mokkiapp.dao.AsiakasDAO;
import com.mokki.mokkiapp.dao.MiscDAO;
import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.viewmodel.AsiakasUnifiedViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class AsiakashallintaController {

    // TableView ja Columns

    @FXML
    private TableView<AsiakasUnifiedViewModel> asiakasTable;
    @FXML
    private TableColumn<AsiakasUnifiedViewModel, Integer> idColumn;
    @FXML
    private TableColumn<AsiakasUnifiedViewModel, String> nimiColumn;
    @FXML
    private TableColumn<AsiakasUnifiedViewModel, String> tyyppiColumn;
    @FXML
    private TableColumn<AsiakasUnifiedViewModel, String> emailColumn;
    @FXML
    private TableColumn<AsiakasUnifiedViewModel, String> osoiteColumn;
    @FXML
    private TableColumn<AsiakasUnifiedViewModel, String> ytunnusColumn;
    @FXML
    private TableColumn<AsiakasUnifiedViewModel, String> puhelinColumn;
    @FXML
    private TableColumn<AsiakasUnifiedViewModel, String> postialueColumn;

    // Tab pane kamat
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab lisaaAsiakasTab;
    @FXML
    private Tab poistaAsiakasTab;
    @FXML
    private Tab muokkaaAsiakasTab;


    //Lisää asiakas tabin matskut
    @FXML
    private ComboBox<String> lisaaAsiakasComboBox;
    @FXML
    private Button lisaaAsiakasButton;

    //TextFieldit. Loppunumero viittaa taulukon indeksiin -> eka numero sarake, toinen numero rivi. Kiinteät tekstit lomakkeen täyttöä varten.
    @FXML
    private TextField lisaaAsiakasTextField01;
    @FXML
    private TextField lisaaAsiakasTextField02;
    @FXML
    private TextField lisaaAsiakasTextField03;
    @FXML
    private TextField lisaaAsiakasTextField04;
    @FXML
    private TextField lisaaAsiakasTextField05;
    @FXML
    private TextField lisaaAsiakasTextField06;
    @FXML
    private TextField lisaaAsiakasTextField07;
    @FXML
    private TextField lisaaAsiakasTextField08;

    //TextFiedlit, johon täytetään nimi, osoite, jne. Loppunumero viittaa taulukon indeksiin -> eka numero sarake, toinen numero rivi. Kiinteät tekstit lomakkeen täyttöä varten.
    @FXML
    private TextField lisaaAsiakasTextField11;
    @FXML
    private TextField lisaaAsiakasTextField12;
    @FXML
    private TextField lisaaAsiakasTextField13;
    @FXML
    private TextField lisaaAsiakasTextField14;
    @FXML
    private TextField lisaaAsiakasTextField15;
    @FXML
    private TextField lisaaAsiakasTextField16;
    @FXML
    private TextField lisaaAsiakasTextField17;
    @FXML
    private TextField lisaaAsiakasTextField18;


    private ObservableList<AsiakasUnifiedViewModel> asiakasData = FXCollections.observableArrayList();


    @FXML public void initialize() {

        // ALUSTUKSET TABLE VIEW //
        idColumn.setCellValueFactory(cell -> cell.getValue().asiakasIdProperty().asObject());
        nimiColumn.setCellValueFactory(cell -> cell.getValue().nimiProperty());
        tyyppiColumn.setCellValueFactory(cell -> cell.getValue().tyyppiProperty());
        emailColumn.setCellValueFactory(cell -> cell.getValue().emailProperty());
        osoiteColumn.setCellValueFactory(cell -> cell.getValue().katuosoiteProperty());
        ytunnusColumn.setCellValueFactory(cell -> cell.getValue().ytunnusProperty());
        puhelinColumn.setCellValueFactory(cell -> cell.getValue().puhelinProperty());
        postialueColumn.setCellValueFactory(cell -> cell.getValue().postialueProperty());

        AsiakasDAO dao = new AsiakasDAO();
        dao.haeKaikkiYksityishenkilot().forEach(y -> asiakasData.add(new AsiakasUnifiedViewModel(y)));
        dao.haeKaikkiYritykset().forEach(y -> asiakasData.add(new AsiakasUnifiedViewModel(y)));
        asiakasTable.setItems(asiakasData);

        // Alustetaan "lisää asiakas" tabin matskut
        lisaaAsiakasComboBox.getItems().addAll("Yksityishenkilö", "Yritys");
        // Asetetaan TextField, joissa ohjeet käyttäjälle lomakkeen täyttämiseksi uneditable.
        lisaaAsiakasTextField01.setEditable(false);
        lisaaAsiakasTextField02.setEditable(false);
        lisaaAsiakasTextField03.setEditable(false);
        lisaaAsiakasTextField04.setEditable(false);
        lisaaAsiakasTextField05.setEditable(false);
        lisaaAsiakasTextField06.setEditable(false);
        lisaaAsiakasTextField07.setEditable(false);
        lisaaAsiakasTextField08.setEditable(false);

        // Asetetaan listener "lisää asiakas" tabin comboboxiin
        lisaaAsiakasComboBox.setOnAction(event -> {
            String selected = lisaaAsiakasComboBox.getValue();
            if (selected != null) {
                switch (selected) {
                    case "Yksityishenkilö":
                        // Muutetaan valikkoon näkyviin yksityishenkilön lomake
                        System.out.println("Selected: Yksityishenkilö");
                        lisaaAsiakasTextField01.setText("Etunimi");
                        lisaaAsiakasTextField02.setText("Sukunimi");
                        lisaaAsiakasTextField03.setText("Puhelin");
                        lisaaAsiakasTextField04.setText("Email");
                        lisaaAsiakasTextField05.setText("Katuosoite");
                        lisaaAsiakasTextField06.setText("Postinumero");
                        lisaaAsiakasTextField07.setText("Kunta");
                        lisaaAsiakasTextField08.setText("Maa");

                        break;
                    case "Yritys":
                        // Muutetaan valikkoon näkyviin yrityksen lomake
                        System.out.println("Selected: Yritys");
                        lisaaAsiakasTextField01.setText("Yrityksen nimi");
                        lisaaAsiakasTextField02.setText("Y-tunnus");
                        lisaaAsiakasTextField03.setText("Puhelin");
                        lisaaAsiakasTextField04.setText("Email");
                        lisaaAsiakasTextField05.setText("Katuosoite");
                        lisaaAsiakasTextField06.setText("Postinumero");
                        lisaaAsiakasTextField07.setText("Kunta");
                        lisaaAsiakasTextField08.setText("Maa");

                        break;
                    default:
                        System.out.println("Unknown selection");
                }
            }
        });

        // Asetetaan listener "lisää asiakas" -nappiin, jotta näkyvillä ainoastaan kun lisää asiakas tab auki
        lisaaAsiakasTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Tab valittuna, näytä näppäin
                lisaaAsiakasButton.setVisible(true);
            } else {
                // Tab ei valittuna, piilota näppäin
                lisaaAsiakasButton.setVisible(false);
            }
        });


    }

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
    private void onLisaaAsiakasButtonClick(ActionEvent event) {
        try {
            // Poistetaan ylimääräiset välilyönnit, puhelinnumerosta ja sähköpostista poistetaan kaikki white space
            String tyyppi = lisaaAsiakasComboBox.getValue(); // "Yksityishenkilö" tai "Yritys"
            String katuosoite = normalizeWhitespace(lisaaAsiakasTextField15.getText());
            String email = normalizeWhitespace(lisaaAsiakasTextField14.getText()).replaceAll("\\s+", "");
            String puhelin = normalizeWhitespace(lisaaAsiakasTextField13.getText()).replaceAll("\\s+", "");
            String postinumero = normalizeWhitespace(lisaaAsiakasTextField16.getText());
            String kunta = normalizeWhitespace(lisaaAsiakasTextField17.getText());
            String maa = normalizeWhitespace(lisaaAsiakasTextField18.getText());

            // Tarkastetaan, että kentät eivät ole tyhjänä
            if (tyyppi == null || katuosoite.isBlank() || email.isBlank() || puhelin.isBlank()
                    || postinumero.isBlank() || kunta.isBlank() || maa.isBlank()) {
                showError("Virhe", "Kaikki kentät ovat pakollisia.");
                return;
            }

            // Laaditaan postialue objekti
            Postialue postialue = new Postialue(postinumero, kunta, maa);
            Asiakas uusiAsiakas = null;

            // Haetaan yksityishenkilöspesifit tiedot lomakkeelta
            if (tyyppi.equals("Yksityishenkilö")) {
                String etunimi = normalizeWhitespace(lisaaAsiakasTextField11.getText());
                String sukunimi = normalizeWhitespace(lisaaAsiakasTextField12.getText());

                if (etunimi.isBlank() || sukunimi.isBlank()) {
                    showError("Virhe", "Etunimi ja sukunimi ovat pakollisia.");
                    return;
                }

                uusiAsiakas = new Yksityishenkilo(0, katuosoite, email, puhelin, postialue, etunimi, sukunimi);

            } else if (tyyppi.equals("Yritys")) { // Haetaan yritysspesifit tiedot lomakkeelta
                String yrityksenNimi = normalizeWhitespace(lisaaAsiakasTextField11.getText());
                String ytunnus = normalizeWhitespace(lisaaAsiakasTextField12.getText());

                if (yrityksenNimi.isBlank() || ytunnus.isBlank()) {
                    showError("Virhe", "Yrityksen nimi ja Y-tunnus ovat pakollisia.");
                    return;
                }

                // Tarkastetaan regexpatternilla, että käyttäjä on täyttänyt oikein y-tunnuksen XXXXXXX-X ja annetaan virheilmoitus muutoin
                if (!ytunnus.matches("\\d{7}-\\d")) {
                    showError("Virhe", "Virheellinen Y-tunnus. Varmista, että se on muotoa XXXXXXXX-X.");
                    return;
                }

                uusiAsiakas = new Yritys(0, katuosoite, email, puhelin, postialue, yrityksenNimi, ytunnus);
            }

            // Kaikki inputit validoituna --> yritetään puskea tiedot kantaan
            AsiakasDAO dao = new AsiakasDAO();

            try {
                dao.lisaaAsiakas(uusiAsiakas);
                showInfo("Onnistui", "Asiakas lisättiin onnistuneesti.");
                paivitaAsiakastaulukko();   // Päivitä taulukko
                clearForm();                // Tyhjennä lomake
            } catch (SQLIntegrityConstraintViolationException e) {
                showError("Tietokantavirhe", "Y-tunnus on jo olemassa. Käytä toista.");
            } catch (SQLException e) {
                showError("Virhe", "Tietokantavirhe asiakasta lisätessä.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Virhe", "Tuntematon virhe asiakasta lisätessä.");
        }
    }

    // Metodi jolla poistetaan ylimääräiset välilyönnit
    private String normalizeWhitespace(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void paivitaAsiakastaulukko() {
        asiakasData.clear();
        AsiakasDAO dao = new AsiakasDAO();
        dao.haeKaikkiYksityishenkilot().forEach(y -> asiakasData.add(new AsiakasUnifiedViewModel(y)));
        dao.haeKaikkiYritykset().forEach(y -> asiakasData.add(new AsiakasUnifiedViewModel(y)));
        asiakasTable.setItems(asiakasData);
    }

    private void clearForm() {
        //Lomakkeen tyhjennys lisää asiakaslomakkeelta
        lisaaAsiakasTextField11.clear();
        lisaaAsiakasTextField12.clear();
        lisaaAsiakasTextField13.clear();
        lisaaAsiakasTextField14.clear();
        lisaaAsiakasTextField15.clear();
        lisaaAsiakasTextField16.clear();
        lisaaAsiakasTextField17.clear();
        lisaaAsiakasTextField18.clear();
        lisaaAsiakasTextField01.clear();
        lisaaAsiakasTextField02.clear();
        lisaaAsiakasTextField03.clear();
        lisaaAsiakasTextField04.clear();
        lisaaAsiakasTextField05.clear();
        lisaaAsiakasTextField06.clear();
        lisaaAsiakasTextField07.clear();
        lisaaAsiakasTextField08.clear();
        lisaaAsiakasComboBox.setValue(null);
    }


}

