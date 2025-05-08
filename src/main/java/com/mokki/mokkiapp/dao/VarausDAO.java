package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.database.Database;
import com.mokki.mokkiapp.Varaustiedot;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.TemporalAdjusters;

public class VarausDAO {

    public List<Varaustiedot> haeKaikkiVaraukset() {
        List<Varaustiedot> varaukset = new ArrayList<>();
        String sql = "SELECT * FROM Varaus";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                varaukset.add(new Varaustiedot(
                        rs.getInt("varaus_id"),
                        rs.getInt("asiakas_id"),
                        rs.getInt("mokki_id"),
                        rs.getDate("alkupvm").toLocalDate(),
                        rs.getDate("loppupvm").toLocalDate(),
                        rs.getDate("varaus_pvm").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return varaukset;
    }

    public void lisaaVaraus(Varaustiedot varaus) {
        String sql = "INSERT INTO Varaus (varaus_id, asiakas_id, mokki_id, alkupvm, loppupvm, varaus_pvm) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, varaus.getVarausId());
            ps.setInt(2, varaus.getAsiakasId());
            ps.setInt(3, varaus.getMokkiId());
            ps.setDate(4, Date.valueOf(varaus.getAlkupvm()));
            ps.setDate(5, Date.valueOf(varaus.getLoppupvm()));
            ps.setDate(6, Date.valueOf(varaus.getVarausPvm()));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Varaustiedot> getReservationsByCottageAndMonth(int mokkiId, int month) throws SQLException {
        List<Varaustiedot> varaukset = new ArrayList<>();
        String sql = "SELECT * FROM Varaus WHERE mokki_id = ? AND (" +
                "MONTH(alkupvm) = ? OR MONTH(loppupvm) = ? OR (alkupvm <= ? AND loppupvm >= ?))";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, mokkiId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, month);

            // Luodaan LocalDate-objektit valitun kuukauden rajoille vuodella 2000 (ei merkitystä haulle kuukauden perusteella)
            LocalDate firstDayOfMonth = LocalDate.of(2000, month, 1);
            LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

            pstmt.setDate(4, Date.valueOf(firstDayOfMonth));
            pstmt.setDate(5, Date.valueOf(lastDayOfMonth));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Varaustiedot varaus = new Varaustiedot();
                    varaus.setVarausId(rs.getInt("varaus_id"));
                    varaus.setAsiakasId(rs.getInt("asiakas_id"));
                    varaus.setMokkiId(rs.getInt("mokki_id"));
                    varaus.setAlkupvm(rs.getDate("alkupvm").toLocalDate());
                    varaus.setLoppupvm(rs.getDate("loppupvm").toLocalDate());
                    varaus.setVarausPvm(rs.getDate("varaus_pvm").toLocalDate());
                    varaukset.add(varaus);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return varaukset;
    }
}