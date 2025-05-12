package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.database.Database;
import com.mokki.mokkiapp.model.Mokki;
import com.mokki.mokkiapp.model.Postialue;
import com.mokki.mokkiapp.dao.MiscDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;


public class MokkiDAO {

    /**
     * Testausta...
     * @param args
     */
    public static void main(String[] args) {
        MiscDAO postialueDAO = new MiscDAO();
        Postialue postialue = new Postialue("99999", "Testikylä", "Suomi");
        postialueDAO.lisaaPostialue(postialue);


        // Luominen
        Mokki uusiMokki = new Mokki(
                0, // mökki_id = 0, koska SERIAL
                "TESTIMÖKKI",
                "TESTIPOLKU 1",
                new BigDecimal("150.00"),
                "TESTI KUVAUS",
                postialue
                );
        MokkiDAO mokkiDAO = new MokkiDAO();
        mokkiDAO.lisaaMokki(uusiMokki);


        // Päivittäminen
        Mokki paivitetty = new Mokki(
                5, // 5 = Tunturimökki
                "TUNTURIMÖKKI",
                "TESTIKUJA 9",
                new BigDecimal("5000.00"),
                "PÄIVITETTY KUVAUS",
                new Postialue("00100", "Helsinki", "Suomi")
                );
        MokkiDAO.paivitaMokki(paivitetty);


        // luominen: OK
        // pävittäminen: OK
    }


    public List<Integer> haeKaikkiMokkiIdt() {
        List<Integer> mokkiIds = new ArrayList<>();
        String sql = "SELECT mokki_id FROM Mökki";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mokkiIds.add(rs.getInt("mokki_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mokkiIds;
    }


    public List<Mokki> haeKaikkiMokitNimella() {
        List<Mokki> mokit = new ArrayList<>();
        String sql = "SELECT m.mokki_id, m.nimi, m.katuosoite, m.hinta, m.kuvaus, m.postinumero, p.kunta, p.maa " +
                "FROM Mökki m JOIN Postialue p ON m.postinumero = p.postinumero"; // KORJATTU JOIN-LAUSE
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Postialue postialue = new Postialue(
                        rs.getString("postinumero"),
                        rs.getString("kunta"),
                        rs.getString("maa")
                );
                Mokki mokki = new Mokki(
                        rs.getInt("mokki_id"),
                        rs.getString("nimi"),
                        rs.getString("katuosoite"),
                        rs.getBigDecimal("hinta"),
                        rs.getString("kuvaus"),
                        postialue
                );
                mokit.add(mokki);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mokit;
    }


    public Mokki haeMokki(int mokkiId) {
        String sql = "SELECT m.mokki_id, m.nimi, m.katuosoite, m.hinta, m.kuvaus, m.postinumero, p.kunta, p.maa " +
                "FROM Mökki m JOIN Postialue p ON m.postinumero = p.postinumero " + // KORJATTU JOIN-LAUSE
                "WHERE m.mokki_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mokkiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Postialue postialue = new Postialue(
                        rs.getString("postinumero"),
                        rs.getString("kunta"),
                        rs.getString("maa")
                );
                return new Mokki(
                        rs.getInt("mokki_id"),
                        rs.getString("nimi"),
                        rs.getString("katuosoite"),
                        rs.getBigDecimal("hinta"),
                        rs.getString("kuvaus"),
                        postialue
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Lisää uusi mökki
     * @param mokki
     */
    public void lisaaMokki(Mokki mokki) {
        String sql = "INSERT INTO Mökki (nimi, katuosoite, postinumero, hinta, kuvaus) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mokki.getNimi());
            ps.setString(2, mokki.getKatuosoite());
            ps.setString(3, mokki.getPostialue().getPostinumero());
            ps.setBigDecimal(4, mokki.getHinta());
            ps.setString(5, mokki.getKuvaus());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Päivitä mökin tietoja
     * @param mokki
     */
    public static void paivitaMokki(Mokki mokki) {
        String sql = "UPDATE Mökki SET nimi = ?, katuosoite = ?, postinumero = ?, hinta = ?, kuvaus = ? WHERE mokki_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mokki.getNimi());
            ps.setString(2, mokki.getKatuosoite());
            ps.setString(3, mokki.getPostialue().getPostinumero());
            ps.setBigDecimal(4, mokki.getHinta());
            ps.setString(5, mokki.getKuvaus());
            ps.setInt(6, mokki.getMokkiId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Lisää tarvittaessa muita MokkiDAO:n metodeja


}