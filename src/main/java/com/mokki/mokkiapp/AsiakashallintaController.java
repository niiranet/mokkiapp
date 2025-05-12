package com.mokki.mokkiapp;

import com.mokki.mokkiapp.dao.AsiakasDAO;
import com.mokki.mokkiapp.viewmodel.AsiakasUnifiedViewModel;
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
import javafx.fxml.Initializable;

import java.io.IOException;

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

    //Lisää asiakas matskut
    @FXML
    private ComboBox<String> lisaaAsiakasComboBox;
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
        // Asetetaan TextField, joissa ohjeet käyttäjälle lomakkeen täyttämiseksi uneditable
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

}
