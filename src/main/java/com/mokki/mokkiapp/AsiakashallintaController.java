package com.mokki.mokkiapp;

import com.mokki.mokkiapp.dao.AsiakasDAO;
import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.viewmodel.AsiakasUnifiedViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
    private Tab lisaaAsiakasTab;
    @FXML
    private Tab poistaAsiakasTab;
    @FXML
    private Tab muokkaaAsiakasTab;

    // Lisää asiakas tabin matskut
    @FXML
    private ComboBox<String> lisaaAsiakasComboBox;
    @FXML
    private Button lisaaAsiakasButton;

    // TextFieldit (lisää asiakas)
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
    // Poistettu: @FXML private TextField lisaaAsiakasTextField08;
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

    // TextFieldit (muokkaa asiakastietoja)
    @FXML
    private TextField muokkaaAsiakasTextField01;
    @FXML
    private TextField muokkaaAsiakasTextField02;
    @FXML
    private TextField muokkaaAsiakasTextField03;
    @FXML
    private TextField muokkaaAsiakasTextField04;
    @FXML
    private TextField muokkaaAsiakasTextField05;
    @FXML
    private TextField muokkaaAsiakasTextField06;
    @FXML
    private TextField muokkaaAsiakasTextField07;
    // Poistettu: @FXML private TextField muokkaaAsiakasTextField08;

    // Muokkaa asiakastietoja tabin matskut
    @FXML
    private Button muokkaaButton;

    @FXML
    private Button peruutaButton;

    // Poista asiakas tabin matskut
    @FXML
    private Button poistaAsiakasButton; // Lisätty poista-painike

    private ObservableList<AsiakasUnifiedViewModel> asiakasData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // ALUSTUKSET TABLE VIEW //
        idColumn.setCellValueFactory(cell -> cell.getValue().asiakasIdProperty().asObject());
        nimiColumn.setCellValueFactory(cell -> cell.getValue().nimiProperty());
        tyyppiColumn.setCellValueFactory(cell -> cell.getValue().tyyppiProperty());
        emailColumn.setCellValueFactory(cell -> cell.getValue().emailProperty());
        osoiteColumn.setCellValueFactory(cell -> cell.getValue().katuosoiteProperty());
        ytunnusColumn.setCellValueFactory(cell -> cell.getValue().ytunnusProperty());
        puhelinColumn.setCellValueFactory(cell -> cell.getValue().puhelinProperty());
        postialueColumn.setCellValueFactory(cell -> cell.getValue().postialueProperty());

        paivitaAsiakastaulukko();

        // Alustetaan "lisää asiakas" tabin matskut (säilytetään ennallaan)
        lisaaAsiakasComboBox.getItems().addAll("Yksityishenkilö", "Yritys");
        lisaaAsiakasTextField01.setEditable(false);
        lisaaAsiakasTextField02.setEditable(false);
        lisaaAsiakasTextField03.setEditable(false);
        lisaaAsiakasTextField04.setEditable(false);
        lisaaAsiakasTextField05.setEditable(false);
        lisaaAsiakasTextField06.setEditable(false);
        lisaaAsiakasTextField07.setEditable(false);
        // Poistettu: lisaaAsiakasTextField08.setEditable(false);

        lisaaAsiakasComboBox.setOnAction(event -> {
            String selected = lisaaAsiakasComboBox.getValue();
            if (selected != null) {
                // ... (säilytetään ennallaan) ...
            }
        });

        lisaaAsiakasTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            lisaaAsiakasButton.setVisible(newValue);
        });

        // Asetetaan listener "poista asiakas" -nappiin
        poistaAsiakasTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            poistaAsiakasButton.setVisible(newValue);
        });

        // Asetetaan listener "muokkaa asiakastietoja" -välilehdelle
        muokkaaAsiakasTab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            muokkaaButton.setVisible(newValue);
            peruutaButton.setVisible(newValue);
            AsiakasUnifiedViewModel valittuAsiakas = asiakasTable.getSelectionModel().getSelectedItem();
            if (newValue && valittuAsiakas != null) {
                taytaMuokkaaLomake(valittuAsiakas);
            } else if (newValue) {
                tyhjennaMuokkaaLomake(); // Tyhjennetään lomake, jos ei valittu asiakasta
            }
        });

        // Lisätään kuuntelija taulukon valintaan
        asiakasTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AsiakasUnifiedViewModel>() {
            @Override
            public void changed(ObservableValue<? extends AsiakasUnifiedViewModel> observable, AsiakasUnifiedViewModel oldValue, AsiakasUnifiedViewModel newValue) {
                if (newValue != null && muokkaaAsiakasTab.isSelected()) {
                    taytaMuokkaaLomake(newValue);
                }
            }
        });

        poistaAsiakasButton.setVisible(false); // Piilotetaan aluksi
        muokkaaButton.setVisible(false); // Piilotetaan aluksi
        peruutaButton.setVisible(false); // Piilotetaan aluksi
    }

    @FXML
    public void onTakaisinButtonClick(ActionEvent event) throws IOException {
        Parent etusivuParent = FXMLLoader.load(getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(etusivuScene);
        window.show();
    }

    @FXML
    private void onLisaaAsiakasButtonClick(ActionEvent event) {
        try {
            String tyyppi = lisaaAsiakasComboBox.getValue();
            String katuosoite = normalizeWhitespace(lisaaAsiakasTextField15.getText());
            String email = normalizeWhitespace(lisaaAsiakasTextField14.getText()).replaceAll("\\s+", "");
            String puhelin = normalizeWhitespace(lisaaAsiakasTextField13.getText()).replaceAll("\\s+", "");
            String postinumero = normalizeWhitespace(lisaaAsiakasTextField16.getText());
            String kunta = normalizeWhitespace(lisaaAsiakasTextField17.getText());
            String maa = normalizeWhitespace(lisaaAsiakasTextField18.getText());

            if (tyyppi == null || katuosoite.isBlank() || email.isBlank() || puhelin.isBlank()
                    || postinumero.isBlank() || kunta.isBlank() || maa.isBlank()) {
                showError("Virhe", "Kaikki kentät ovat pakollisia.");
                return;
            }

            Postialue postialue = new Postialue(postinumero, kunta, maa);
            Asiakas uusiAsiakas = null;

            if (tyyppi.equals("Yksityishenkilö")) {
                String etunimi = normalizeWhitespace(lisaaAsiakasTextField11.getText());
                String sukunimi = normalizeWhitespace(lisaaAsiakasTextField12.getText());

                if (etunimi.isBlank() || sukunimi.isBlank()) {
                    showError("Virhe", "Etunimi ja sukunimi ovat pakollisia.");
                    return;
                }
                uusiAsiakas = new Yksityishenkilo(0, katuosoite, email, puhelin, postialue, etunimi, sukunimi);

            } else if (tyyppi.equals("Yritys")) {
                String yrityksenNimi = normalizeWhitespace(lisaaAsiakasTextField11.getText());
                String ytunnus = normalizeWhitespace(lisaaAsiakasTextField12.getText());

                if (yrityksenNimi.isBlank() || ytunnus.isBlank()) {
                    showError("Virhe", "Yrityksen nimi ja Y-tunnus ovat pakollisia.");
                    return;
                }
                if (!ytunnus.matches("\\d{7}-\\d")) {
                    showError("Virhe", "Virheellinen Y-tunnus. Varmista, että se on muotoa XXXXXXXX-X.");
                    return;
                }
                uusiAsiakas = new Yritys(0, katuosoite, email, puhelin, postialue, yrityksenNimi, ytunnus);
            }

            AsiakasDAO dao = new AsiakasDAO();
            try {
                dao.lisaaAsiakas(uusiAsiakas);
                showInfo("Onnistui", "Asiakas lisättiin onnistuneesti.");
                paivitaAsiakastaulukko();
                clearForm();
            } catch (SQLIntegrityConstraintViolationException e) {
                showError("Tietokantavirhe", "Y-tunnus on jo olemassa. Käytä toista.");
            } catch (SQLException e) {
                showError("Virhe", "Tietokantavirhe asiakasta lisättäessä.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Virhe", "Tuntematon virhe asiakasta lisättäessä.");
        }
    }

    @FXML
    private void onPoistaAsiakasButtonClick(ActionEvent event) {
        AsiakasUnifiedViewModel valittuAsiakas = asiakasTable.getSelectionModel().getSelectedItem();

        if (valittuAsiakas == null) {
            showError("Virhe", "Valitse ensin asiakas taulukosta, jonka haluat poistaa.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Vahvista poisto");
        alert.setHeaderText("Oletko varma, että haluat poistaa asiakkaan?");
        alert.setContentText("Asiakkaan ID: " + valittuAsiakas.getAsiakasId() + ", Nimi: " + valittuAsiakas.getNimi());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                AsiakasDAO dao = new AsiakasDAO();
                try {
                    dao.poistaAsiakas(valittuAsiakas.getAsiakasId());
                    showInfo("Onnistui", "Asiakas poistettiin onnistuneesti.");
                    paivitaAsiakastaulukko();
                } catch (SQLException e) {
                    showError("Virhe", "Tietokantavirhe asiakasta poistettaessa.");
                    e.printStackTrace();
                }
            }
        });
    }

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
        lisaaAsiakasTextField11.clear();
        lisaaAsiakasTextField12.clear();
        lisaaAsiakasTextField13.clear();
        lisaaAsiakasTextField14.clear();
        lisaaAsiakasTextField15.clear();
        lisaaAsiakasTextField16.clear();
        lisaaAsiakasTextField17.clear();
        lisaaAsiakasTextField18.clear();
        muokkaaAsiakasTextField01.clear();
        muokkaaAsiakasTextField02.clear();
        muokkaaAsiakasTextField03.clear();
        muokkaaAsiakasTextField04.clear();
        muokkaaAsiakasTextField05.clear();
        muokkaaAsiakasTextField06.clear();
        muokkaaAsiakasTextField07.clear();
        // Poistettu: muokkaaAsiakasTextField08.clear();
        lisaaAsiakasComboBox.setValue(null);
    }

    private void taytaMuokkaaLomake(AsiakasUnifiedViewModel asiakas) {
        if (asiakas.getTyyppi().equals("Yksityishenkilö")) {
            String[] nimiOsat = asiakas.getNimi().split(" ", 2);
            muokkaaAsiakasTextField01.setText(nimiOsat[0]);
            muokkaaAsiakasTextField02.setText(nimiOsat.length > 1 ? nimiOsat[1] : "");
        } else if (asiakas.getTyyppi().equals("Yritys")) {
            muokkaaAsiakasTextField01.setText(asiakas.getNimi());
            muokkaaAsiakasTextField02.setText(asiakas.getYtunnus());
        }
        muokkaaAsiakasTextField03.setText(asiakas.getPuhelin());
        muokkaaAsiakasTextField04.setText(asiakas.getEmail());
        muokkaaAsiakasTextField05.setText(asiakas.getKatuosoite());
        String[] postialueOsat = asiakas.getPostialue().split(" ", 2);
        muokkaaAsiakasTextField06.setText(postialueOsat[0]);
        muokkaaAsiakasTextField07.setText(postialueOsat.length > 1 ? postialueOsat[1] : "");
        // Poistettu: muokkaaAsiakasTextField08.setText(""); // Maa ei ole eritelty ViewModelissä
    }

    @FXML
    private void onPeruutaButtonClick(ActionEvent event) {
        // Palautetaan muokkauslomakkeen tiedot siihen asiakkaaseen, joka oli valittuna
        // taulukossa ennen muokkaamisen aloittamista.
        AsiakasUnifiedViewModel valittuAsiakas = asiakasTable.getSelectionModel().getSelectedItem();
        if (valittuAsiakas != null) {
            taytaMuokkaaLomake(valittuAsiakas);
        } else {
            // Jos mitään asiakasta ei ole valittu (mikä voisi olla outoa tässä kohtaa),
            // tyhjennetään lomake varmuuden vuoksi.
            tyhjennaMuokkaaLomake();
        }
        // Poistetaan myös taulukon valinta, jotta seuraava muokkaus alkaa puhtaalta pöydältä.
        asiakasTable.getSelectionModel().clearSelection();
    }

    private void tyhjennaMuokkaaLomake() {
        muokkaaAsiakasTextField01.clear();
        muokkaaAsiakasTextField02.clear();
        muokkaaAsiakasTextField03.clear();
        muokkaaAsiakasTextField04.clear();
        muokkaaAsiakasTextField05.clear();
        muokkaaAsiakasTextField06.clear();
        muokkaaAsiakasTextField07.clear();
        // Poistettu: muokkaaAsiakasTextField08.clear();
        asiakasTable.getSelectionModel().clearSelection(); // Poistetaan valinta taulukosta
    }

    @FXML
    private void onMuokkaaAsiakasButtonClick(ActionEvent event) {
        AsiakasUnifiedViewModel valittuAsiakasViewModel = asiakasTable.getSelectionModel().getSelectedItem();

        if (valittuAsiakasViewModel == null) {
            showError("Virhe", "Valitse ensin asiakas taulukosta.");
            return;
        }

        String katuosoite = normalizeWhitespace(muokkaaAsiakasTextField05.getText());
        String email = normalizeWhitespace(muokkaaAsiakasTextField04.getText()).replaceAll("\\s+", "");
        String puhelin = normalizeWhitespace(muokkaaAsiakasTextField03.getText()).replaceAll("\\s+", "");
        String postinumero = normalizeWhitespace(muokkaaAsiakasTextField06.getText());
        String kunta = normalizeWhitespace(muokkaaAsiakasTextField07.getText());

        if (katuosoite.isBlank() || email.isBlank() || puhelin.isBlank() || postinumero.isBlank() || kunta.isBlank()) {
            showError("Virhe", "Kaikki kentät ovat pakollisia.");
            return;
        }

        String[] postialueOsat = valittuAsiakasViewModel.getPostialue().split(" ");
        String maa = "";
        if (postialueOsat.length > 2) {
            maa = postialueOsat[2];
        } else if (postialueOsat.length == 2) {
            // Joskus maa saattaa puuttua tai olla tyhjä ViewModelissä
            // Voit päättää, miten tällaisessa tilanteessa toimitaan.
            // Tässä esimerkissä jätetään maa tyhjäksi.
            maa = "";
        }

        Postialue postialue = new Postialue(postinumero, kunta, maa);
        AsiakasDAO dao = new AsiakasDAO();
        Asiakas muokattuAsiakas = null;

        if (valittuAsiakasViewModel.getTyyppi().equals("Yksityishenkilö")) {
            String etunimi = normalizeWhitespace(muokkaaAsiakasTextField01.getText());
            String sukunimi = normalizeWhitespace(muokkaaAsiakasTextField02.getText());
            if (etunimi.isBlank() || sukunimi.isBlank()) {
                showError("Virhe", "Etunimi ja sukunimi ovat pakollisia.");
                return;
            }
            muokattuAsiakas = new Yksityishenkilo(valittuAsiakasViewModel.getAsiakasId(), katuosoite, email, puhelin, postialue, etunimi, sukunimi);
        } else if (valittuAsiakasViewModel.getTyyppi().equals("Yritys")) {
            String yrityksenNimi = normalizeWhitespace(muokkaaAsiakasTextField01.getText());
            String ytunnus = normalizeWhitespace(muokkaaAsiakasTextField02.getText());
            if (yrityksenNimi.isBlank() || ytunnus.isBlank()) {
                showError("Virhe", "Yrityksen nimi ja Y-tunnus ovat pakollisia.");
                return;
            }
            if (!ytunnus.matches("\\d{7}-\\d")) {
                showError("Virhe", "Virheellinen Y-tunnus. Varmista, että se on muotoa XXXXXXXX-X.");
                return;
            }
            muokattuAsiakas = new Yritys(valittuAsiakasViewModel.getAsiakasId(), katuosoite, email, puhelin, postialue, yrityksenNimi, ytunnus);
        }

        if (muokattuAsiakas != null) {
            dao.paivitaAsiakasTiedot(muokattuAsiakas);
            showInfo("Onnistui", "Asiakkaan tiedot päivitettiin onnistuneesti.");
            paivitaAsiakastaulukko();
            tyhjennaMuokkaaLomake();
        }
    }
}