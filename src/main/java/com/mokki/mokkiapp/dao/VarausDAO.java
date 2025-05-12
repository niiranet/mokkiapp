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

    public int lisaaVaraus(Varaustiedot varaus) throws SQLException {
        String sql = "INSERT INTO Varaus (asiakas_id, mokki_id, alkupvm, loppupvm, varaus_pvm) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, varaus.getAsiakasId());
            ps.setInt(2, varaus.getMokkiId());
            ps.setDate(3, Date.valueOf(varaus.getAlkupvm()));
            ps.setDate(4, Date.valueOf(varaus.getLoppupvm()));
            ps.setDate(5, Date.valueOf(varaus.getVarausPvm()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Varauksen luonti epäonnistui, yhtään riviä ei lisätty.");
            }

            // Hae viimeksi lisätyn rivin ID MySQL:ssä
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_id()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Varauksen ID:n haku epäonnistui.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Varaustiedot haeVaraus(int varausId) throws SQLException {
        String sql = "SELECT * FROM Varaus WHERE varaus_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, varausId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Varaustiedot(
                        rs.getInt("varaus_id"),
                        rs.getInt("asiakas_id"),
                        rs.getInt("mokki_id"),
                        rs.getDate("alkupvm").toLocalDate(),
                        rs.getDate("loppupvm").toLocalDate(),
                        rs.getDate("varaus_pvm").toLocalDate()
                );
            }
        }
        return null;
    }

    public int haeAsiakasIdVaraukselta(int varausId) throws SQLException {
        String sql = "SELECT asiakas_id FROM Varaus WHERE varaus_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, varausId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("asiakas_id");
            }
        }
        return -1; // Palauttaa -1, jos varausta ei löydy
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

    public List<Varaustiedot> getReservationsByCottage(int mokkiId) {
        List<Varaustiedot> varaukset = new ArrayList<>();
        String sql = "SELECT * FROM Varaus WHERE mokki_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mokkiId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return varaukset;
    }

    public List<Varaustiedot> getReservationsByMonth(int month) {
        List<Varaustiedot> varaukset = new ArrayList<>();
        String sql = "SELECT * FROM Varaus WHERE MONTH(alkupvm) = ? OR MONTH(loppupvm) = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, month);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return varaukset;
    }

    public List<Varaustiedot> haeVarauksetAsiakkaalle(int asiakasId) {
        List<Varaustiedot> varaukset = new ArrayList<>();
        String sql = "SELECT * FROM Varaus WHERE asiakas_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asiakasId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return varaukset;
    }

    public List<Varaustiedot> haeVarauksetAsiakkaalle(int asiakasId, int kuukausi) {
        List<Varaustiedot> varaukset = new ArrayList<>();
        String sql = "SELECT * FROM Varaus WHERE asiakas_id = ? AND (MONTH(alkupvm) = ? OR MONTH(loppupvm) = ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asiakasId);
            ps.setInt(2, kuukausi);
            ps.setInt(3, kuukausi);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return varaukset;
    }

    public List<Varaustiedot> haeVarauksetMokilleAikavalilla(int mokkiId, LocalDate alku, LocalDate loppu) {
        List<Varaustiedot> varaukset = new ArrayList<>();
        String sql = "SELECT * FROM Varaus " +
                "WHERE mokki_id = ? AND " +
                "(alkupvm < ? AND loppupvm > ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mokkiId);
            ps.setDate(2, Date.valueOf(loppu)); // Tarkistetaan, että olemassa oleva varaus alkaa ennen uuden varauksen loppua
            ps.setDate(3, Date.valueOf(alku));  // Tarkistetaan, että olemassa oleva varaus loppuu uuden varauksen alkamisen jälkeen
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return varaukset;
    }
}