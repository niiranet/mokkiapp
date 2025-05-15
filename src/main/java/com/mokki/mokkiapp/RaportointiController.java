package com.mokki.mokkiapp;

import com.mokki.mokkiapp.Varaustiedot;
import com.mokki.mokkiapp.dao.RaporttiDAO;
import com.mokki.mokkiapp.dao.VarausDAO;
import com.mokki.mokkiapp.model.Mokki;
import com.mokki.mokkiapp.model.Raportti;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RaportointiController {

    @FXML private ComboBox<Mokki> mokkiComboBox;
    @FXML private DatePicker alkuPvmPicker;
    @FXML private DatePicker loppuPvmPicker;
    @FXML private Label raporttiOtsikkoLabel;
    @FXML private TableView<Raportti> varausTableView;
    @FXML private TableColumn<Raportti, Integer> mokkiIDColumn;
    @FXML private TableColumn<Raportti, Integer> asiakasIDColumn; 
    @FXML private TableColumn<Raportti, String> pvmColumn;
    @FXML private TableColumn<Raportti, String> hintaColumn;

    private VarausDAO varausDAO = new VarausDAO();

    @FXML
    public void initialize() {
        // Kerää kaikki varaukset ja muodosta Raportti-oliot
        List<Varaustiedot> kaikkiVaraukset = varausDAO.haeKaikkiVaraukset();
        Set<Mokki> mokkiSet = new HashSet<>();
        for (Varaustiedot vt : kaikkiVaraukset) {
            Raportti r = RaporttiDAO.muodostaRaportti(vt.getVarausId());
            if (r != null) mokkiSet.add(r.getMokki());
        }
        mokkiComboBox.setItems(FXCollections.observableArrayList(mokkiSet));

        // Aseta TableView-sarakkeet
        mokkiIDColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getMokki().getMokkiId()).asObject());
        asiakasIDColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getAsiakas().getAsiakasId()).asObject());  // Now shows ID
        pvmColumn.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getVaraus().getAlku().toString()));
        hintaColumn.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getLasku().getSumma().toString()));
    }

    @FXML
    public void handleNaytaRaportti(ActionEvent actionEvent) {
        Mokki valittu = mokkiComboBox.getValue();
        LocalDate alku = alkuPvmPicker.getValue();
        LocalDate loppu = loppuPvmPicker.getValue();
        if (valittu == null || alku == null || loppu == null) {
            raporttiOtsikkoLabel.setText("Valitse mökki ja aikaväli");
            return;
        }
        raporttiOtsikkoLabel.setText(
                String.format("Raportti: %s (%s - %s)", valittu.getNimi(), alku, loppu));

        // Suodata varaukset valitun mökin ja aikavälin mukaan
        List<Raportti> raportit = varausDAO.haeKaikkiVaraukset().stream()
                .map(vt -> RaporttiDAO.muodostaRaportti(vt.getVarausId()))
                .filter(r -> r != null && r.getMokki().getMokkiId() == valittu.getMokkiId())
                .filter(r -> {
                    LocalDate start = r.getVaraus().getAlku().toLocalDate();
                    return (!start.isBefore(alku) && !start.isAfter(loppu));
                })
                .collect(Collectors.toList());

        ObservableList<Raportti> data = FXCollections.observableArrayList(raportit);
        varausTableView.setItems(data);
    }

    @FXML
    public void onTakaisinButtonClick(ActionEvent event) throws IOException {
        Parent etusivuParent = FXMLLoader.load(
                getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(etusivuScene);
        window.show();
    }
}

