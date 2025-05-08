package com.mokki.mokkiapp;

import java.util.List;
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

import com.mokki.mokkiapp.dao.MokkiDAO;
import com.mokki.mokkiapp.dao.VarausDAO;

public class MajoitusvarauksetController {

    @FXML
    public void onTakaisinButtonClick(ActionEvent event) throws IOException {
        Parent etusivuParent = FXMLLoader.load(getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);

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
    private TableView<Varaustiedot> reservationsTableView; // Renamed TableView to reservationsTableView

    @FXML
    private TableColumn<Varaustiedot, LocalDate> alkuColumn; // Muutettu tyyppi Varaustiedot
    @FXML
    private TableColumn<Varaustiedot, LocalDate> loppuColumn; // Muutettu tyyppi Varaustiedot
    @FXML
    private TableColumn<Varaustiedot, Integer> varaajaColumn; // Muutettu tyyppi Varaustiedot

    @FXML
    private Button VaraaMokkiButton;

    private MokkiDAO mokkiDAO;
    private VarausDAO varausDAO;

    @FXML
    public void initialize() {
        System.out.println("MajoitusvarauksetController initialized.");
        mokkiDAO = new MokkiDAO();
        varausDAO = new VarausDAO();

        // Populate cottage ComboBox from the database
        try {
            System.out.println("Calling mokkiDAO.haeKaikkiMokkiIdt()");
            List<Integer> mokkiIds = mokkiDAO.haeKaikkiMokkiIdt();
            if (mokkiIds != null) {
                System.out.println("Fetched cottage IDs: " + mokkiIds);
            } else {
                System.out.println("Failed to fetch cottage IDs - mokkiIds is null.");
            }
            ObservableList<Integer> cottageIdsObservableList = FXCollections.observableArrayList(mokkiIds);
            System.out.println("Creating ObservableList with: " + mokkiIds);
            cottageComboBox.setItems(cottageIdsObservableList);
            System.out.println("Setting cottageComboBox items to: " + cottageIdsObservableList);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching cottage IDs: " + e.getMessage());
        }

        monthComboBox.getItems().addAll("Tammikuu", "Helmikuu", "Maaliskuu",
                "Huhtikuu", "Toukokuu", "Kesäkuu", "Heinäkuu",
                "Elokuu", "Syyskuu", "Lokakuu",
                "Marraskuu", "Joulukuu");

        monthComboBox.setValue("Tammikuu"); // Muutettu oletusarvo vastaamaan switch-lausetta

        alkuColumn.setCellValueFactory(new PropertyValueFactory<>("alkupvm"));
        loppuColumn.setCellValueFactory(new PropertyValueFactory<>("loppupvm"));
        varaajaColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));

        reservationsTableView.setItems(FXCollections.observableArrayList());

        cottageComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateReservationsTable();
        });

        monthComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateReservationsTable();
        });
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
    private void updateReservationsTable() {
        Integer selectedCottageId = cottageComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();

        if (selectedCottageId != null && selectedMonth != null) {
            int monthNumber = getMonthNumber(selectedMonth);
            try {
                ObservableList<Varaustiedot> reservations = FXCollections.observableArrayList(
                        varausDAO.getReservationsByCottageAndMonth(selectedCottageId, monthNumber)
                );
                reservationsTableView.setItems(reservations);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle potential database errors
            }
        } else {
            reservationsTableView.setItems(FXCollections.observableArrayList());
        }
    }

    private int getMonthNumber(String monthName) {
        return switch (monthName) {
            case "Tammikuu" -> 1;
            case "Helmikuu" -> 2;
            case "Maaliskuu" -> 3;
            case "Huhtikuu" -> 4;
            case "Toukokuu" -> 5;
            case "Kesäkuu" -> 6;
            case "Heinäkuu" -> 7;
            case "Elokuu" -> 8;
            case "Syyskuu" -> 9;
            case "Lokakuu" -> 10;
            case "Marraskuu" -> 11;
            case "Joulukuu" -> 12;
            default -> -1; // Handle invalid month name
        };
    }
}