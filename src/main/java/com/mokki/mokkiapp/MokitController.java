package com.mokki.mokkiapp;

import com.mokki.mokkiapp.dao.MokkiDAO;
import com.mokki.mokkiapp.dao.MiscDAO;
import com.mokki.mokkiapp.model.Mokki;
import com.mokki.mokkiapp.model.Postialue;
import com.mokki.mokkiapp.viewmodel.MokkiViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.math.BigDecimal;

public class MokitController {

    private final MokkiDAO mokkiDAO = new MokkiDAO();
    private final MiscDAO miscDAO = new MiscDAO();
    private final ObservableList<MokkiViewModel> mokkiLista = FXCollections.observableArrayList();

    // Kentät
    @FXML private TextField mokinNimiKentta;
    @FXML private TextField osoiteKentta;
    @FXML private TextField postinumeroKentta;
    @FXML private TextField hintaKentta;
    @FXML private TextField kuvausKentta;

    // Taulukko ja sarakkeet
    @FXML private TableView<MokkiViewModel> mokitTaulukko;
    @FXML private TableColumn<MokkiViewModel, Integer> mokkiIdSarake;
    @FXML private TableColumn<MokkiViewModel, String> mokinNimiSarake;
    @FXML private TableColumn<MokkiViewModel, String> katuosoiteSarake;
    @FXML private TableColumn<MokkiViewModel, String> postinumeroSarake;
    @FXML private TableColumn<MokkiViewModel, BigDecimal> hintaSarake;
    @FXML private TableColumn<MokkiViewModel, String> kuvausSarake;

    @FXML
    public void initialize() {
        // Bind sarakkeet ViewModeliin
        mokkiIdSarake.setCellValueFactory(cellData -> cellData.getValue().mokkiIdProperty().asObject());
        mokinNimiSarake.setCellValueFactory(cellData -> cellData.getValue().nimiProperty());
        katuosoiteSarake.setCellValueFactory(cellData -> cellData.getValue().katuosoiteProperty());
        postinumeroSarake.setCellValueFactory(cellData -> cellData.getValue().postinumeroProperty());
        hintaSarake.setCellValueFactory(cellData -> cellData.getValue().hintaProperty());
        kuvausSarake.setCellValueFactory(cellData -> cellData.getValue().kuvausProperty());

        lataaMokit();
    }

    private void lataaMokit() {
        mokkiLista.clear();
        for (Mokki mokki : mokkiDAO.haeKaikkiMokitNimella()) {
            mokkiLista.add(new MokkiViewModel(mokki));
        }
        mokitTaulukko.setItems(mokkiLista);
    }

    @FXML
    private void handleLisaa(ActionEvent event) {
        try {
            String nimi = mokinNimiKentta.getText();
            String katuosoite = osoiteKentta.getText();
            String postinumero = postinumeroKentta.getText();
            BigDecimal hinta = new BigDecimal(hintaKentta.getText());
            String kuvaus = kuvausKentta.getText();

            Postialue postialue = new Postialue(postinumero, "", "Suomi");  // Kunta ja maa voi olla tyhjä tai haettu myöhemmin
            miscDAO.lisaaPostialue(postialue); // Varmista että postialue on tietokannassa

            Mokki mokki = new Mokki(0, nimi, katuosoite, hinta, kuvaus, postialue);
            mokkiDAO.lisaaMokki(mokki);
            lataaMokit();
            tyhjennaKentat();
        } catch (Exception e) {
            e.printStackTrace();
            naytaVirhe("Virhe lisättäessä mökkiä. Tarkista syötetiedot.");
        }
    }

    @FXML
    private void handleMuokkaa(ActionEvent event) {
        MokkiViewModel valittu = mokitTaulukko.getSelectionModel().getSelectedItem();
        if (valittu != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("muokkaa-mokki-view.fxml"));
                Parent root = loader.load();

                MuokkaaMokkiController controller = loader.getController();
                controller.setMokki(valittu);

                Stage stage = new Stage();
                stage.setTitle("Muokkaa mökkiä");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                lataaMokit(); // Päivitä lista ikkunan sulkeuduttua

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            naytaVirhe("Valitse mökki muokataksesi sitä.");
        }
    }


    @FXML
    private void handlePoista(ActionEvent event) {
        MokkiViewModel valittu = mokitTaulukko.getSelectionModel().getSelectedItem();
        if (valittu != null) {
            try {
                int id = valittu.mokkiIdProperty().get();
                String sql = "DELETE FROM Mökki WHERE mokki_id = ?";
                try (var conn = com.mokki.mokkiapp.database.Database.getConnection();
                     var ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                lataaMokit();
                tyhjennaKentat();
            } catch (Exception e) {
                e.printStackTrace();
                naytaVirhe("Virhe poistettaessa mökkiä.");
            }
        } else {
            naytaVirhe("Valitse mökki poistaaksesi sen.");
        }
    }

    private void tyhjennaKentat() {
        mokinNimiKentta.clear();
        osoiteKentta.clear();
        postinumeroKentta.clear();
        hintaKentta.clear();
        kuvausKentta.clear();
    }

    private void naytaVirhe(String viesti) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Virhe");
        alert.setHeaderText(null);
        alert.setContentText(viesti);
        alert.showAndWait();
    }

    @FXML
    public void onTakaisinButtonClick(ActionEvent event) throws IOException {
        Parent etusivuParent = FXMLLoader.load(getClass().getResource("etusivu-view.fxml"));
        Scene etusivuScene = new Scene(etusivuParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(etusivuScene);
        window.show();
    }
}
