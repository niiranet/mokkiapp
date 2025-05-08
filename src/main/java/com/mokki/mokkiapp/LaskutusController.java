package com.mokki.mokkiapp;
import com.mokki.mokkiapp.dao.LaskuDAO;
import com.mokki.mokkiapp.model.Lasku;
import com.mysql.cj.conf.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.time.LocalDate;

import java.io.IOException;

public class LaskutusController implements Initializable {

    @FXML
    private RadioButton rbKaikki;
    @FXML
    private RadioButton rbAvoimet;
    @FXML
    private RadioButton rbMaksetut;
    @FXML
    private ToggleGroup tgNaytaLaskut;
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

    private final LaskuDAO laskuDAO = new LaskuDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
    }


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
        }

        if(rbAvoimet.isSelected()) {
            twLaskutus.setItems(observableMaksamattomatLaskut);
        }

        if(rbMaksetut.isSelected()) {
            twLaskutus.setItems(observableMaksetutLaskut);
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
