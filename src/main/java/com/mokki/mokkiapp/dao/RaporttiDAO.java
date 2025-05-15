package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.Varaustiedot;
import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.database.Database;

import java.sql.*;
import java.util.*;


/**
 * Kaikki raportointiin liittyvät metodit
 */
public class RaporttiDAO {

    /**
     * Testausta...
     * @param args
     */
    public static void main(String[] args) {
        VarausDAO varausDAO = new VarausDAO();
        List<Varaustiedot> varaukset = varausDAO.haeKaikkiVaraukset();

        for (Varaustiedot varaus : varaukset) {
            Raportti raportti = muodostaRaportti(varaus.getVarausId());
            if (raportti != null) {
                System.out.println(raportti);
                System.out.println("===================================");
            } else {
                System.out.println("Raporttia ei löytynyt varaukselle ID: " + varaus.getVarausId());
            }
        }
    }

    /**
     * Kokoaa yhteen kaikki varaukseen liittyvät taulut,
     * joista voi muodostaa majoituksen raportin.
     * @param varausId
     * @return
     */
    public static Raportti muodostaRaportti(int varausId) {
        String sql = """
        SELECT v.varaus_id, v.alkupvm, v.loppupvm, v.varaus_pvm,
               a.asiakas_id, a.katuosoite, a.email, a.puhelin,
               p.postinumero, p.kunta, p.maa,
               y.etunimi, y.sukunimi, r.y_tunnus, r.yrityksen_nimi,
               m.mokki_id, m.nimi AS mokki_nimi, m.katuosoite AS mokki_osoite,
               m.hinta, m.kuvaus,
               l.lasku_id, l.summa, l.viitenro, l.erapaiva, l.maksupvm, l.maksettu
        FROM Varaus v
        JOIN Asiakas a ON v.asiakas_id = a.asiakas_id
        LEFT JOIN Yksityishenkilo y ON a.asiakas_id = y.asiakas_id
        LEFT JOIN Yritys r ON a.asiakas_id = r.asiakas_id
        JOIN Postialue p ON a.postinumero = p.postinumero
        JOIN Mökki m ON v.mokki_id = m.mokki_id
        JOIN Lasku l ON v.varaus_id = l.varaus_id
        WHERE v.varaus_id = ?
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, varausId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Postialue postialue = new Postialue(
                            rs.getString("postinumero"),
                            rs.getString("kunta"),
                            rs.getString("maa")
                    );
                    Asiakas asiakas;
                    // yksityishenkilöiden ja yritysten erottelu
                    if (rs.getString("etunimi") != null) {
                        asiakas = new Yksityishenkilo(
                                rs.getInt("asiakas_id"),
                                rs.getString("katuosoite"),
                                rs.getString("email"),
                                rs.getString("puhelin"),
                                postialue,
                                rs.getString("etunimi"),
                                rs.getString("sukunimi")
                        );
                    }
                    else {
                        asiakas = new Yritys(
                                rs.getInt("asiakas_id"),
                                rs.getString("katuosoite"),
                                rs.getString("email"),
                                rs.getString("puhelin"),
                                postialue,
                                rs.getString("yrityksen_nimi"),
                                rs.getString("y_tunnus")
                        );
                    }

                    Mokki mokki = new Mokki(
                            rs.getInt("mokki_id"),
                            rs.getString("mokki_nimi"),
                            rs.getString("mokki_osoite"),
                            rs.getBigDecimal("hinta"),
                            rs.getString("kuvaus"),
                            postialue
                    );

                    Lasku lasku = new Lasku(
                            rs.getInt("lasku_id"),
                            rs.getInt("varaus_id"),
                            rs.getBigDecimal("summa"),
                            rs.getString("viitenro"),
                            rs.getDate("erapaiva"),
                            rs.getDate("maksupvm"),
                            rs.getBoolean("maksettu")
                    );

                    Varaus varaus = new Varaus(
                            rs.getInt("varaus_id"),
                            rs.getInt("asiakas_id"),
                            rs.getInt("mokki_id"),
                            rs.getDate("alkupvm"),
                            rs.getDate("loppupvm"),
                            rs.getDate("varaus_pvm")
                    );

                    return new Raportti(varaus, asiakas, mokki, lasku);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // jos ei löytynyt
    }



}

