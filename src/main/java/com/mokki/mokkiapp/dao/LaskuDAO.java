package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.database.Database;

import java.sql.*;
import java.util.*;


/**
 * Kaikki laskujen käsittelyihin liittyvät metodit
 */
public class LaskuDAO {

    public List<Lasku> haeMaksetutLaskut() {
        return haeLaskut(true);
    }

    public List<Lasku> haeMaksamattomatLaskut() {
        return haeLaskut(false);
    }

    /**
     * hae lista joko maksetusta tai maksamattomasta laskusta
     * @param maksettu
     * @return
     */
    private List<Lasku> haeLaskut(boolean maksettu) {
        List<Lasku> lista = new ArrayList<>();
        String sql = "SELECT * FROM Lasku WHERE maksettu = ?";

        try (Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, maksettu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Lasku(
                        rs.getInt("lasku_id"),
                        rs.getInt("varaus_id"),
                        rs.getBigDecimal("summa"),
                        rs.getString("tilinro"),
                        rs.getDate("erapaiva"),
                        rs.getDate("maksupvm"),
                        rs.getBoolean("maksettu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    /**
     * merkitse lasku maksetuksi
     * @param laskuId
     */
    public void merkitseMaksetuksi(int laskuId) {
        String sql = "UPDATE Lasku SET maksettu = TRUE, maksupvm = CURRENT_DATE WHERE lasku_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, laskuId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void merkitseMaksamattomaksi(int laskuId) {
        String sql = "UPDATE Lasku SET maksettu = FALSE, maksupvm = NULL WHERE lasku_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, laskuId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}