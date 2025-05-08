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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import javafx.util.StringConverter;
import com.mokki.mokkiapp.dao.AsiakasDAO;
import com.mokki.mokkiapp.dao.MokkiDAO;
import com.mokki.mokkiapp.dao.VarausDAO;
import com.mokki.mokkiapp.model.Yksityishenkilo;
import com.mokki.mokkiapp.model.Yritys;
import com.mokki.mokkiapp.model.Mokki;

public class MajoitusvarauksetController {

    @FXML public void onTakaisinButtonClick(ActionEvent event) throws IOException {
        Parent etusivuParent = FXMLLoader.load(getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(etusivuScene);
        window.show();
    }

    @FXML private ComboBox<Mokki> cottageComboBox;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private DatePicker alkupvmDatePicker;
    @FXML private DatePicker loppupvmDatePicker;
    @FXML private TextField asiakasIdTextField;
    @FXML private TableView<Object> asiakasTableView;
    @FXML private TableColumn<Object, Integer> asiakasIdColumn;
    @FXML private TableColumn<Object, String> katuosoiteColumn;
    @FXML private TableColumn<Object, String> postinumeroColumn;
    @FXML private TableColumn<Object, String> puhelinColumn;
    @FXML private TableColumn<Object, String> nimiColumn;
    @FXML private TableColumn<Object, String> ytunnusColumn;
    @FXML private TableView<Varaustiedot> reservationsTableView;
    @FXML private TableColumn<Varaustiedot, LocalDate> alkuColumn;
    @FXML private TableColumn<Varaustiedot, LocalDate> loppuColumn;
    @FXML private TableColumn<Varaustiedot, Integer> varaajaColumn;
    @FXML private Button VaraaMokkiButton;

    private MokkiDAO mokkiDAO;
    private VarausDAO varausDAO;
    private AsiakasDAO asiakasDAO;

    @FXML public void initialize() {
        System.out.println("MajoitusvarauksetController initialized.");
        mokkiDAO = new MokkiDAO();
        varausDAO = new VarausDAO();
        asiakasDAO = new AsiakasDAO();

        try {
            System.out.println("Calling mokkiDAO.haeKaikkiMokitNimella()");
            List<Mokki> mokit = mokkiDAO.haeKaikkiMokitNimella();
            if (mokit != null) {
                System.out.println("Fetched cottages: " + mokit);
                ObservableList<Mokki> mokitObservableList = FXCollections.observableArrayList(mokit);
                cottageComboBox.setItems(mokitObservableList);
                cottageComboBox.setConverter(new StringConverter<Mokki>() {
                    @Override public String toString(Mokki mokki) { return mokki == null ? null : mokki.getNimi(); }
                    @Override public Mokki fromString(String string) { return null; }
                });

                if (cottageComboBox.getItems() != null) {
                    System.out.println("Comboboxin itemit (heti asettamisen jälkeen): " + cottageComboBox.getItems().size());
                    for (Mokki mokki : cottageComboBox.getItems()) {
                        System.out.println("  - " + mokki.getNimi() + " (ID: " + mokki.getMokkiId() + ")");
                    }
                } else {
                    System.out.println("Comboboxin itemit ovat null.");
                }

            } else {
                System.out.println("Failed to fetch cottages - mokit is null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching cottages: " + e.getMessage());
        }

        monthComboBox.getItems().addAll("Tammikuu", "Helmikuu", "Maaliskuu", "Huhtikuu", "Toukokuu", "Kesäkuu", "Heinäkuu", "Elokuu", "Syyskuu", "Lokakuu", "Marraskuu", "Joulukuu");
        alkuColumn.setCellValueFactory(new PropertyValueFactory<>("alkupvm"));
        loppuColumn.setCellValueFactory(new PropertyValueFactory<>("loppupvm"));
        varaajaColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));
        reservationsTableView.setItems(FXCollections.observableArrayList());

        cottageComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateReservationsTable(newValue == null ? -1 : newValue.getMokkiId(), monthComboBox.getValue());
        });

        monthComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Mokki selectedCottage = cottageComboBox.getValue();
            updateReservationsTable(selectedCottage == null ? -1 : selectedCottage.getMokkiId(), newValue);
        });

        asiakasTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int asiakasId = -1;
                if (newValue instanceof Yksityishenkilo) {
                    asiakasId = ((Yksityishenkilo) newValue).getAsiakasId();
                } else if (newValue instanceof Yritys) {
                    asiakasId = ((Yritys) newValue).getAsiakasId();
                }
                if (asiakasId != -1) {
                    haeJaNaytaAsiakkaanVaraukset(asiakasId);
                }
            } else {
                reservationsTableView.setItems(FXCollections.observableArrayList());
            }
        });
        naytaAsiakkaat();
    }

    private void naytaAsiakkaat() {
        ObservableList<Object> asiakkaat = FXCollections.observableArrayList();
        List<Yksityishenkilo> yksityishenkilot = haeKaikkiYksityishenkilot();
        List<Yritys> yritykset = haeKaikkiYritykset();
        if (yksityishenkilot != null) asiakkaat.addAll(yksityishenkilot);
        if (yritykset != null) asiakkaat.addAll(yritykset);
        asiakasTableView.setItems(asiakkaat);
        asiakasIdColumn.setCellValueFactory(cellData -> {
            Object asiakas = cellData.getValue();
            if (asiakas instanceof Yksityishenkilo) return new SimpleIntegerProperty(((Yksityishenkilo) asiakas).getAsiakasId()).asObject();
            else if (asiakas instanceof Yritys) return new SimpleIntegerProperty(((Yritys) asiakas).getAsiakasId()).asObject();
            else return null;
        });
        katuosoiteColumn.setCellValueFactory(cellData -> {
            Object asiakas = cellData.getValue();
            if (asiakas instanceof Yksityishenkilo) return new SimpleStringProperty(((Yksityishenkilo) asiakas).getKatuosoite());
            else if (asiakas instanceof Yritys) return new SimpleStringProperty(((Yritys) asiakas).getKatuosoite());
            else return null;
        });
        postinumeroColumn.setCellValueFactory(cellData -> {
            Object asiakas = cellData.getValue();
            if (asiakas instanceof Yksityishenkilo) return new SimpleStringProperty(((Yksityishenkilo) asiakas).getPostialue().getPostinumero());
            else if (asiakas instanceof Yritys) return new SimpleStringProperty(((Yritys) asiakas).getPostialue().getPostinumero());
            else return null;
        });
        puhelinColumn.setCellValueFactory(cellData -> {
            Object asiakas = cellData.getValue();
            if (asiakas instanceof Yksityishenkilo) return new SimpleStringProperty(((Yksityishenkilo) asiakas).getPuhelin());
            else if (asiakas instanceof Yritys) return new SimpleStringProperty(((Yritys) asiakas).getPuhelin());
            else return null;
        });
        nimiColumn.setCellValueFactory(cellData -> {
            Object asiakas = cellData.getValue();
            if (asiakas instanceof Yksityishenkilo) return new SimpleStringProperty(((Yksityishenkilo) asiakas).getEtunimi() + " " + ((Yksityishenkilo) asiakas).getSukunimi());
            else if (asiakas instanceof Yritys) return new SimpleStringProperty(((Yritys) asiakas).getYrityksenNimi());
            else return null;
        });
        ytunnusColumn.setCellValueFactory(cellData -> {
            Object asiakas = cellData.getValue();
            if (asiakas instanceof Yritys) return new SimpleStringProperty(((Yritys) asiakas).getYtunnus());
            else return new SimpleStringProperty("");
        });
    }

    private void haeJaNaytaAsiakkaanVaraukset(int asiakasId) {
        try {
            List<Varaustiedot> asiakkaanVaraukset = varausDAO.haeVarauksetAsiakkaalle(asiakasId);
            reservationsTableView.setItems(FXCollections.observableArrayList(asiakkaanVaraukset));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void haeJaNaytaAsiakkaanVaraukset(int asiakasId, int kuukausi) {
        try {
            List<Varaustiedot> asiakkaanVaraukset = varausDAO.haeVarauksetAsiakkaalle(asiakasId, kuukausi);
            reservationsTableView.setItems(FXCollections.observableArrayList(asiakkaanVaraukset));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Yksityishenkilo> haeKaikkiYksityishenkilot() {
        return asiakasDAO.haeKaikkiYksityishenkilot();
    }

    private List<Yritys> haeKaikkiYritykset() {
        return asiakasDAO.haeKaikkiYritykset();
    }

    @FXML
    private void handleVaraaMokkiButtonClick(ActionEvent event) {
        if (cottageComboBox.getValue() != null) System.out.println("Valittu mökki: " + cottageComboBox.getValue());
        if (alkupvmDatePicker.getValue() != null) System.out.println("Alkupvm: " + alkupvmDatePicker.getValue());
        if (loppupvmDatePicker.getValue() != null) System.out.println("Loppupvm: " + loppupvmDatePicker.getValue());
        if (asiakasIdTextField.getText() != null && !asiakasIdTextField.getText().isEmpty()) System.out.println("Asiakas ID: " + asiakasIdTextField.getText());
        else System.out.println("Asiakas ID ei ole syötetty.");
    }

    @FXML
    private void updateReservationsTable(int mokkiId, String selectedMonth) {
        if (selectedMonth != null) {
            int monthNumber = getMonthNumber(selectedMonth);
            if (mokkiId != -1) {
                try {
                    reservationsTableView.setItems(FXCollections.observableArrayList(
                            varausDAO.getReservationsByCottageAndMonth(mokkiId, monthNumber)
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    reservationsTableView.setItems(FXCollections.observableArrayList(
                            varausDAO.getReservationsByMonth(monthNumber) // Oletetaan, että VarausDAO:ssa on tämä metodi
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (mokkiId != -1) {
            try {
                reservationsTableView.setItems(FXCollections.observableArrayList(
                        varausDAO.getReservationsByCottage(mokkiId)
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            reservationsTableView.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void updateReservationsTable(int mokkiId) {
        // Tätä metodia ei enää suoraan kutsuta, mutta pidetään se olemassa siltä varalta
        updateReservationsTable(mokkiId, monthComboBox.getValue());
    }

    @FXML
    private void updateReservationsTable() {
        System.out.println("Varoitus: updateReservationsTable() kutsuttiin ilman parametreja.");
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
            default -> -1;
        };
    }
}