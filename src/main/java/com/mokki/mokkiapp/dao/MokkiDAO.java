package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.database.Database;
import com.mokki.mokkiapp.model.Mokki;
import com.mokki.mokkiapp.model.Postialue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class MokkiDAO {

    public List<Integer> haeKaikkiMokkiIdt() {
        List<Integer> mokkiIds = new ArrayList<>();
        String sql = "SELECT mokki_id FROM mökki";
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
                "FROM mökki m JOIN Postialue p ON m.postinumero = p.postinumero"; // KORJATTU JOIN-LAUSE
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
                "FROM mökki m JOIN Postialue p ON m.postinumero = p.postinumero " + // KORJATTU JOIN-LAUSE
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

    // Lisää tarvittaessa muita MokkiDAO:n metodeja
}

    // Lisää tarvittaessa muita MokkiDAO:n metodeja
