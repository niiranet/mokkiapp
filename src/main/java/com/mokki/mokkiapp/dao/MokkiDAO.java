package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.database.Database;

import java.sql.*;
import java.util.*;

public class MokkiDAO {

    public Mokki haeMokki(int mokkiId) {
        String sql = "SELECT * FROM Mökki JOIN Postialue USING(postinumero) WHERE mokki_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mokkiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Postialue pa = new Postialue(
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
                        pa
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Integer> haeKaikkiMokkiIdt() {
        List<Integer> mokkiIdt = new ArrayList<>();
        String sql = "SELECT mokki_id FROM Mökki";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mokkiIdt.add(rs.getInt("mokki_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mokkiIdt;
    }

    // lisää mökki


    // muokkaa mökin tietoja


}