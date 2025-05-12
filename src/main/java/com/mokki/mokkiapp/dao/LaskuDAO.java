package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

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

    public int lisaaLasku(Lasku lasku) throws SQLException {
        String sql = "INSERT INTO Lasku (varaus_id, summa, tilinro, erapaiva, maksupvm, maksettu) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, lasku.getVarausId());
            ps.setBigDecimal(2, lasku.getSumma());
            ps.setString(3, lasku.getTilinro());
            ps.setDate(4, Date.valueOf(lasku.getErapaiva().toLocalDate()));
            ps.setDate(5, lasku.getMaksupvm() != null ? Date.valueOf(lasku.getMaksupvm().toLocalDate()) : null);
            ps.setBoolean(6, lasku.isMaksettu());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Laskun luonti epäonnistui, yhtään riviä ei lisätty.");
            }

            // Hae viimeksi lisätyn rivin ID MySQL:ssä
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_id()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Laskun ID:n haku epäonnistui.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}