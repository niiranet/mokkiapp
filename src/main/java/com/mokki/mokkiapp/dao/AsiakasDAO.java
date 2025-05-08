package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.database.Database;

import java.sql.*;
import java.util.*;


public class AsiakasDAO {

    public Yksityishenkilo haeYksityishenkilo(int asiakasId) {
        String sql =
                "SELECT * FROM Asiakas " +
                "JOIN Yksityishenkilo USING(asiakas_id) " +
                "JOIN Postialue USING(postinumero) WHERE asiakas_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asiakasId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Postialue pa = new Postialue(
                        rs.getString("postinumero"),
                        rs.getString("kunta"),
                        rs.getString("maa"));
                return new Yksityishenkilo(
                        rs.getInt("asiakas_id"),
                        rs.getString("katuosoite"),
                        rs.getString("email"),
                        rs.getString("puhelin"),
                        pa,
                        rs.getString("etunimi"),
                        rs.getString("sukunimi")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Yritys haeYritys(int asiakasId) {
        String sql =
                "SELECT * FROM Asiakas " +
                "JOIN Yritys USING(asiakas_id) " +
                "JOIN Postialue USING(postinumero) WHERE asiakas_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asiakasId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Postialue pa = new Postialue(
                        rs.getString("postinumero"),
                        rs.getString("kunta"),
                        rs.getString("maa"));
                return new Yritys(
                        rs.getInt("asiakas_id"),
                        rs.getString("katuosoite"),
                        rs.getString("email"),
                        rs.getString("puhelin"),
                        pa,
                        rs.getString("yrityksen_nimi"),
                        rs.getString("y_tunnus")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public void paivitaAsiakasTiedot(Asiakas asiakas) {
        String sql = "UPDATE Asiakas SET katuosoite = ?, postinumero = ?, email = ?, puhelin = ? WHERE asiakas_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, asiakas.getKatuosoite());
            ps.setString(2, asiakas.getPostialue().getPostinumero());
            ps.setString(3, asiakas.getEmail());
            ps.setString(4, asiakas.getPuhelin());
            ps.setInt(5, asiakas.getAsiakasId());
            ps.executeUpdate();

            if (asiakas instanceof Yksityishenkilo yksityinen) {
                String ysql = "UPDATE Yksityishenkilo SET etunimi = ?, sukunimi = ? WHERE asiakas_id = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(ysql)) {
                    ps2.setString(1, yksityinen.getEtunimi());
                    ps2.setString(2, yksityinen.getSukunimi());
                    ps2.setInt(3, yksityinen.getAsiakasId());
                    ps2.executeUpdate();
                }
            } else if (asiakas instanceof Yritys yritys) {
                String ysql = "UPDATE Yritys SET nimi = ?, yhteyshenkilo = ? WHERE asiakas_id = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(ysql)) {
                    ps2.setString(1, yritys.getNimi());
                    ps2.setString(2, yritys.getYhteyshenkilo());
                    ps2.setInt(3, yritys.getAsiakasId());
                    ps2.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    */

}

