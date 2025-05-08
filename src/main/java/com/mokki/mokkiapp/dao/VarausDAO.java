package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.database.Database;

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class VarausDAO {

    public List<Varaus> haeKaikkiVaraukset() {
        List<Varaus> varaukset = new ArrayList<>();
        String sql = "SELECT * FROM Varaus";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                varaukset.add(new Varaus(
                        rs.getInt("varaus_id"),
                        rs.getInt("asiakas_id"),
                        rs.getInt("mokki_id"),
                        rs.getDate("alkupvm"),
                        rs.getDate("loppupvm"),
                        rs.getDate("varaus_pvm")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return varaukset;
    }

    public void lisaaVaraus(Varaus varaus) {
        String sql = "INSERT INTO Varaus (varaus_id, asiakas_id, mokki_id, alkupvm, loppupvm, varaus_pvm) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, varaus.getVarausId());
            ps.setInt(2, varaus.getAsiakasId());
            ps.setInt(3, varaus.getMokkiId());
            /*
            ps.setDate(4, Date.valueOf(varaus.getAlku()));
            ps.setDate(5, Date.valueOf(varaus.getLoppu()));
            ps.setDate(6, Date.valueOf(varaus.getVarausPvm()));
            */
            ps.setDate(4, varaus.getAlku());
            ps.setDate(5, varaus.getLoppu());
            ps.setDate(6, varaus.getVarausPvm());


            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
