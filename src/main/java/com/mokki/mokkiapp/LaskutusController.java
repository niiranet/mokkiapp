package com.mokki.mokkiapp;
import com.mokki.mokkiapp.dao.LaskuDAO;
import com.mokki.mokkiapp.model.Lasku;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


import java.io.IOException;

public class LaskutusController implements Initializable {

    // Näytä laskut RadioButtonit
    @FXML
    private RadioButton rbKaikki;
    @FXML
    private RadioButton rbAvoimet;
    @FXML
    private RadioButton rbMaksetut;
    @FXML
    private RadioButton rbHaelaskut;
    @FXML
    private ToggleGroup tgNaytaLaskut;
    @FXML
    private Button paivitaSuodatinButton;
    // Hae lasku osion elementit
    @FXML
    private Label haeLaskuLabel;
    @FXML
    private TextField haeLaskuTextField;
    @FXML
    private Button tyhjennaValinnatButton;
    private FilteredList<Lasku> filteredData;

    // Merkitse lasku elementit
    @FXML
    private Label merkitseLaskuLabel;
    @FXML
    private Button maksettuButton;
    @FXML
    private Button maksamatonButton;

    //Text Areat
    @FXML
    private TextArea laskutusTextArea;
    @FXML
    private TextArea laskutusConsoleTextArea;

    // Näkymän TableView sarakkeet
    @FXML
    private TableView<Lasku> twLaskutus;
    @FXML
    private TableColumn<Lasku, Integer> lasku_idColumn;
    @FXML
    private TableColumn<Lasku, Integer> varaus_idColumn;
    @FXML
    private TableColumn<Lasku, BigDecimal> summaColumn;
    @FXML
    private TableColumn<Lasku, String> tilinroColumn;
    @FXML
    private TableColumn<Lasku, Date> erapaivaColumn;
    @FXML
    private TableColumn<Lasku, Date> maksupvmColumn;
    @FXML
    private TableColumn<Lasku, Boolean> maksettuColumn;

    // Luodaan LaskuDAO
    private final LaskuDAO laskuDAO = new LaskuDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // ALUSTUKSET //

        // Alustetaan näkymän taulukko
        lasku_idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getLaskuId()));
        varaus_idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getVarausId()));
        summaColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getSumma()));
        tilinroColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getTilinro()));
        erapaivaColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getErapaiva()));
        maksupvmColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getMaksupvm()));
        maksettuColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().isMaksettu()));

        List<Lasku> maksetutLaskut = laskuDAO.haeMaksetutLaskut();
        List<Lasku> maksamattomatLaskut = laskuDAO.haeMaksamattomatLaskut();
        List<Lasku> kaikkiLaskut = new ArrayList<>();
        kaikkiLaskut.addAll(maksetutLaskut);
        kaikkiLaskut.addAll(maksamattomatLaskut);

        ObservableList<Lasku> observableKaikkiLaskut = FXCollections.observableArrayList(kaikkiLaskut);
        twLaskutus.setItems(observableKaikkiLaskut);

        //Asetetaan table view editable, jotta tableview voidaan päivittää
        twLaskutus.setEditable(true);

        //Piilotetaan merkitse lasku, kunnes jotain valittuna tableview näkymässä
        maksettuButton.setVisible(false);
        maksamatonButton.setVisible(false);

        //Piilotetaan laskuhaku ja tuodaan se esiin kun se on valittuna radiobuttonista
        haeLaskuLabel.setVisible(false);
        haeLaskuTextField.setVisible(false);
        tyhjennaValinnatButton.setVisible(false);

        //Asetetaan TextAreat siten, että käyttäjä ei voi syöttää niihin tietoa

        laskutusTextArea.setEditable(false);
        laskutusConsoleTextArea.setEditable(false);

        // TABLE VIEW LISTENER //

        //Laaditaan listener tableviewille, joka seuraa onko näkymässä tableviewin rivi aktiivisena
        twLaskutus.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                boolean onMaksettu = newSel.isMaksettu();

                // Riippuen valitun rivin arvosta valitaan joko maksettu painike tai maksettu painike näkyviin
                maksettuButton.setVisible(!onMaksettu);
                maksamatonButton.setVisible(onMaksettu);
                // Piilotetaan ohjausteksti
                laskutusTextArea.setVisible(false);
            } else {
                // Mikäli taulukko ei ole aktiivisena, niin pistetään napit piiloon
                maksettuButton.setVisible(false);
                maksamatonButton.setVisible(false);
                // Ohjausviesti näkyviin, että käyttäjä älyää painaa taulukkoa
                laskutusTextArea.setVisible(true);
            }
        });

    }

    // NÄKYMÄN METODIT //

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
    private void onPaivitaSuodatinButtonClick(ActionEvent event) throws IOException {

        List<Lasku> maksetutLaskut = laskuDAO.haeMaksetutLaskut();
        List<Lasku> maksamattomatLaskut = laskuDAO.haeMaksamattomatLaskut();
        List<Lasku> kaikkiLaskut = new ArrayList<>();
        kaikkiLaskut.addAll(maksetutLaskut);
        kaikkiLaskut.addAll(maksamattomatLaskut);
        ObservableList<Lasku> observableKaikkiLaskut = FXCollections.observableArrayList(kaikkiLaskut);
        ObservableList<Lasku> observableMaksamattomatLaskut = FXCollections.observableArrayList(maksamattomatLaskut);
        ObservableList<Lasku> observableMaksetutLaskut = FXCollections.observableArrayList(maksetutLaskut);

        if(rbKaikki.isSelected()) {
            twLaskutus.setItems(observableKaikkiLaskut);
        }

        if(rbAvoimet.isSelected()) {
            twLaskutus.setItems(observableMaksamattomatLaskut);
        }

        if(rbMaksetut.isSelected()) {
            twLaskutus.setItems(observableMaksetutLaskut);
        }

    }

    @FXML
    private void onMaksettuButtonClick(ActionEvent event) throws IOException {
        Lasku valittuLasku = twLaskutus.getSelectionModel().getSelectedItem();
        if (valittuLasku != null) {
            laskuDAO.merkitseMaksetuksi(valittuLasku.getLaskuId());
            valittuLasku.setMaksettu(true); // Päivitetään taulukon arvo näkyviin manuaalisesti (muutos jo tietokannassa)
            valittuLasku.setMaksupvm(new java.sql.Date(System.currentTimeMillis())); // Päivitetään taulukon arvo näkyviin manuaalisesti (sama muutos jo tietokannassa)
            twLaskutus.refresh(); // Päivitetään TableView
            //poistetaan rivilltä aktiivinen ja valinta ja laitetaan se takaisin, jotta listener huomaa muutoksen
            twLaskutus.getSelectionModel().clearSelection(); // Poistetaan riviltä valinta
            twLaskutus.getSelectionModel().select(valittuLasku); // Valitaan rivi uudestaan
            laskutusConsoleTextArea.appendText("Lasku: "+valittuLasku.getLaskuId() + " kirjattu maksetuksi.\n");
        }
    }

    @FXML
    private void onMaksamatonButtonClick(ActionEvent event) throws IOException {
        Lasku valittuLasku = twLaskutus.getSelectionModel().getSelectedItem();
        if (valittuLasku != null) {
            laskuDAO.merkitseMaksamattomaksi(valittuLasku.getLaskuId());
            valittuLasku.setMaksettu(false); // Päivitetään taulukon arvo näkyviin manuaalisesti (muutos jo tietokannassa)
            valittuLasku.setMaksupvm(null); // Päivitetään taulukon arvo näkyviin manuaalisesti (muutos jo tietokannassa)
            twLaskutus.refresh(); // Päivitetään TableView
            //poistetaan rivilltä aktiivinen ja valinta ja laitetaan se takaisin, jotta listener huomaa muutoksen
            twLaskutus.getSelectionModel().clearSelection(); // // Poistetaan riviltä valinta
            twLaskutus.getSelectionModel().select(valittuLasku); // Valitaan rivi uudestaan
            laskutusConsoleTextArea.appendText("Lasku: "+valittuLasku.getLaskuId() + " kirjattu maksamattomaksi.\n");
        }
    }

    @FXML
    private void onRadioButtonClick(ActionEvent event) throws IOException {

        List<Lasku> maksetutLaskut = laskuDAO.haeMaksetutLaskut();
        List<Lasku> maksamattomatLaskut = laskuDAO.haeMaksamattomatLaskut();
        List<Lasku> kaikkiLaskut = new ArrayList<>();
        kaikkiLaskut.addAll(maksetutLaskut);
        kaikkiLaskut.addAll(maksamattomatLaskut);
        ObservableList<Lasku> observableKaikkiLaskut = FXCollections.observableArrayList(kaikkiLaskut);
        ObservableList<Lasku> observableMaksamattomatLaskut = FXCollections.observableArrayList(maksamattomatLaskut);
        ObservableList<Lasku> observableMaksetutLaskut = FXCollections.observableArrayList(maksetutLaskut);

        if(rbKaikki.isSelected()) {
            twLaskutus.setItems(observableKaikkiLaskut);
            haeLaskuLabel.setVisible(false);
            haeLaskuTextField.setVisible(false);
            tyhjennaValinnatButton.setVisible(false);
            paivitaSuodatinButton.setVisible(true);
        }

        if(rbAvoimet.isSelected()) {
            twLaskutus.setItems(observableMaksamattomatLaskut);
            haeLaskuLabel.setVisible(false);
            haeLaskuTextField.setVisible(false);
            tyhjennaValinnatButton.setVisible(false);
            paivitaSuodatinButton.setVisible(true);
        }

        if(rbMaksetut.isSelected()) {
            twLaskutus.setItems(observableMaksetutLaskut);
            haeLaskuLabel.setVisible(false);
            haeLaskuTextField.setVisible(false);
            tyhjennaValinnatButton.setVisible(false);
            paivitaSuodatinButton.setVisible(true);
        }

        if(rbHaelaskut.isSelected()) {
            twLaskutus.setItems(observableKaikkiLaskut);
            haeLaskuLabel.setVisible(true);
            haeLaskuTextField.setVisible(true);
            tyhjennaValinnatButton.setVisible(true);
            paivitaSuodatinButton.setVisible(false);
            hakuKentta();

        }
    }

    @FXML
    private void hakuKentta() {
        twLaskutus.refresh();

        // Kääritään ObservableList FilterListiin
        filteredData = new FilteredList<>(twLaskutus.getItems(), p -> true);

        // Yhdistetään haku kenttä filteroituun listaan
        haeLaskuTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lasku -> {
                // Jos filtteri on tyhjä, näytä kaikki rivit
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Verrataan lasku_id:tä filteröityyn tekstiin
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(lasku.getLaskuId()).toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Kääritään FilteredList SortedListiin
        SortedList<Lasku> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(twLaskutus.comparatorProperty());

        // Esitetään suodatetut tapaukset
        twLaskutus.setItems(sortedData);
    }

    @FXML
    private void onTyhjennaValinnatButton(ActionEvent event) throws IOException {
        List<Lasku> maksetutLaskut = laskuDAO.haeMaksetutLaskut();
        List<Lasku> maksamattomatLaskut = laskuDAO.haeMaksamattomatLaskut();
        List<Lasku> kaikkiLaskut = new ArrayList<>();
        kaikkiLaskut.addAll(maksetutLaskut);
        kaikkiLaskut.addAll(maksamattomatLaskut);
        ObservableList<Lasku> observableKaikkiLaskut = FXCollections.observableArrayList(kaikkiLaskut);
        twLaskutus.setItems(observableKaikkiLaskut);
        haeLaskuTextField.clear();
        hakuKentta();



    }



}
