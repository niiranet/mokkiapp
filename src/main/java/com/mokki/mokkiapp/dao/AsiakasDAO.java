package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.Varaustiedot;
import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.database.Database;
import com.mokki.mokkiapp.dao.MiscDAO;
import com.mokki.mokkiapp.testaus.Misc;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.*;


public class AsiakasDAO {

    /**
     * Testausta...
     * @param args
     */
    public static void main(String[] args) {
        AsiakasDAO dao = new AsiakasDAO();
        MiscDAO postialueDAO = new MiscDAO();
        String yTunnus = Misc.generoiYTunnus();


        // Päivittäiminen
        Postialue postialue1 = new Postialue("00100", "Helsinki", "Suomi");
        Yksityishenkilo paivitettyYksityinen = new Yksityishenkilo(
                1, "Päivitettykatu 10", "uusi@email.com", "0401234567", postialue1,
                "Maija", "Mallikas"
        );
        dao.paivitaAsiakasTiedot(paivitettyYksityinen);

        Postialue postialue2 = new Postialue("20100", "Turku", "Suomi");
        Yritys paivitettyYritys = new Yritys(
                2, "Yrityskatu 55", "firma@firma.fi", "020112233", postialue2,
                "Uusi Oy", "1234567-8"
        );
        dao.paivitaAsiakasTiedot(paivitettyYritys);


        // Luominen
        Postialue postialue3 = new Postialue("99999", "Testikaupunki", "Suomi");
        MiscDAO.lisaaPostialue(postialue3);

        Yksityishenkilo yksityinen = new Yksityishenkilo(
                0, // 0 = SERIAL kenttä määrittelee automaattisesti ID:n
                "TESTIKATU 123",
                "teppo@testi.fi",
                "0401234567",
                postialue3,
                "Teppo",
                "Testaaja"
        );
        //dao.lisaaAsiakas(yksityinen);  //viety try blockin sisälle ks. alta TN 13.5.2025

        Yritys yritys = new Yritys(
                0, // 0 = SERIAL kenttä määrittelee automaattisesti ID:n
                "TESTITIE 456",
                "info@firma.fi",
                "0207654321",
                postialue2,
                "Testifirma Oy",
                yTunnus
        );
        //dao.lisaaAsiakas(yritys); //viety try blockin sisälle ks. alta TN 13.5.2025

        // Lisätty virheen hallinta TN 13.5.2025
        try {
            dao.lisaaAsiakas(yksityinen);
            dao.lisaaAsiakas(yritys);
        } catch (SQLException e) {
            System.err.println("Virhe asiakasta lisätessä: " + e.getMessage());
            e.printStackTrace();
        }

        // pävittäminen: OK
        // luominen: asiakas_id skippaa yhdellä

    }


    /**
     * Yhteinen hakumetodi molemmille asiakastyypeille
     * @param asiakasId
     * @return asiakasolio
     */
    public Asiakas haeAsiakas(int asiakasId) {
        String sql = """
        SELECT * FROM Asiakas
        LEFT JOIN Yksityishenkilo USING(asiakas_id)
        LEFT JOIN Yritys USING(asiakas_id)
        JOIN Postialue USING(postinumero)
        WHERE asiakas_id = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asiakasId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Postialue pa = new Postialue(
                        rs.getString("postinumero"),
                        rs.getString("kunta"),
                        rs.getString("maa"));

                if (rs.getString("etunimi") != null) {
                    // Yksityishenkilö
                    return new Yksityishenkilo(
                            rs.getInt("asiakas_id"),
                            rs.getString("katuosoite"),
                            rs.getString("email"),
                            rs.getString("puhelin"),
                            pa,
                            rs.getString("etunimi"),
                            rs.getString("sukunimi")
                    );
                } else if (rs.getString("yrityksen_nimi") != null) {
                    // Yritys
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


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


    public List<Yksityishenkilo> haeKaikkiYksityishenkilot() {
        List<Yksityishenkilo> yksityishenkilot = new ArrayList<>();
        String sql = "SELECT * FROM Asiakas JOIN Yksityishenkilo USING(asiakas_id) JOIN Postialue USING(postinumero)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Postialue pa = new Postialue(
                        rs.getString("postinumero"),
                        rs.getString("kunta"),
                        rs.getString("maa"));
                yksityishenkilot.add(new Yksityishenkilo(
                        rs.getInt("asiakas_id"),
                        rs.getString("katuosoite"),
                        rs.getString("email"),
                        rs.getString("puhelin"),
                        pa,
                        rs.getString("etunimi"),
                        rs.getString("sukunimi")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yksityishenkilot;
    }


    public List<Yritys> haeKaikkiYritykset() {
        List<Yritys> yritykset = new ArrayList<>();
        String sql = "SELECT * FROM Asiakas JOIN Yritys USING(asiakas_id) JOIN Postialue USING(postinumero)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Postialue pa = new Postialue(
                        rs.getString("postinumero"),
                        rs.getString("kunta"),
                        rs.getString("maa"));
                yritykset.add(new Yritys(
                        rs.getInt("asiakas_id"),
                        rs.getString("katuosoite"),
                        rs.getString("email"),
                        rs.getString("puhelin"),
                        pa,
                        rs.getString("yrityksen_nimi"),
                        rs.getString("y_tunnus")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return yritykset;
    }


    /**
     * Päivittää asiakkaden tiedot muutettujen kenttien perusteella
     * @param asiakas Asiakasolio
     */
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

            if (asiakas instanceof Yksityishenkilo paivitettyYksityinen) { // Korjattu muuttujan nimi
                String ysql = "UPDATE Yksityishenkilo SET etunimi = ?, sukunimi = ? WHERE asiakas_id = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(ysql)) {
                    ps2.setString(1, paivitettyYksityinen.getEtunimi());
                    ps2.setString(2, paivitettyYksityinen.getSukunimi());
                    ps2.setInt(3, paivitettyYksityinen.getAsiakasId());
                    ps2.executeUpdate();
                }
            }
            else if (asiakas instanceof Yritys paivitettyYritys) { // Korjattu muuttujan nimi
                String ysql = "UPDATE Yritys SET yrityksen_nimi = ?, y_tunnus = ? WHERE asiakas_id = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(ysql)) {
                    ps2.setString(1, paivitettyYritys.getYrityksenNimi());
                    ps2.setString(2, paivitettyYritys.getYtunnus());
                    ps2.setInt(3, paivitettyYritys.getAsiakasId());
                    ps2.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Lisää uusi asiakas tietokantaan
     * @param asiakas
     */
    public void lisaaAsiakas(Asiakas asiakas) throws SQLException {

        MiscDAO miscDAO = new MiscDAO();
        Postialue pa = asiakas.getPostialue();
        if (!miscDAO.onkoPostialueOlemassa(pa.getPostinumero())) {
            miscDAO.lisaaPostialue(pa);  // Lisää uusi postialue, jos sitä ei ole
        }

        String sqlAsiakas = "INSERT INTO Asiakas (katuosoite, postinumero, email, puhelin) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement psAsiakas = conn.prepareStatement(sqlAsiakas, Statement.RETURN_GENERATED_KEYS)) {

            psAsiakas.setString(1, asiakas.getKatuosoite());
            psAsiakas.setString(2, asiakas.getPostialue().getPostinumero());
            psAsiakas.setString(3, asiakas.getEmail());
            psAsiakas.setString(4, asiakas.getPuhelin());
            psAsiakas.executeUpdate();

            try (ResultSet generatedKeys = psAsiakas.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int asiakasId = generatedKeys.getInt(1);

                    if (asiakas instanceof Yksityishenkilo yksityinen) {
                        String sqlYksityinen = "INSERT INTO Yksityishenkilo (asiakas_id, etunimi, sukunimi) VALUES (?, ?, ?)";
                        try (PreparedStatement psYksityinen = conn.prepareStatement(sqlYksityinen)) {
                            psYksityinen.setInt(1, asiakasId);
                            psYksityinen.setString(2, yksityinen.getEtunimi());
                            psYksityinen.setString(3, yksityinen.getSukunimi());
                            psYksityinen.executeUpdate();
                        }
                    } else if (asiakas instanceof Yritys yritys) {
                        String sqlYritys = "INSERT INTO Yritys (asiakas_id, yrityksen_nimi, y_tunnus) VALUES (?, ?, ?)";
                        try (PreparedStatement psYritys = conn.prepareStatement(sqlYritys)) {
                            psYritys.setInt(1, asiakasId);
                            psYritys.setString(2, yritys.getYrityksenNimi());
                            psYritys.setString(3, yritys.getYtunnus());
                            psYritys.executeUpdate();
                        }
                    }
                } else {
                    throw new SQLException("Asiakas ID:n luonti epäonnistui.");
                }
            }
        }
    }

    public Yritys etsiYritysYtunnuksella(String ytunnus) {
        String sql = "SELECT y.*, p.postinumero, p.kunta, p.maa FROM yritys y " +
                "JOIN postialue p ON y.postialue_id = p.id WHERE y.y_tunnus = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ytunnus.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Create Postialue object from the fetched values
                Postialue postialue = new Postialue(
                        rs.getString("postinumero"),
                        rs.getString("kunta"),
                        rs.getString("maa")
                );

                // Return the Yritys object with the Postialue included
                return new Yritys(
                        rs.getInt("id"),
                        rs.getString("katuosoite"),
                        rs.getString("email"),
                        rs.getString("puhelin"),
                        postialue,
                        rs.getString("nimi"),
                        rs.getString("y_tunnus")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Proper logging or exception handling
        }

        return null;
    }


    //Vanhan metodin koodit jätetty varuilta talteen.
    //MySQL supports retrieval of auto-generated keys (like AUTO_INCREMENT primary keys) via JDBC, but not directly in SQL syntax like PostgreSQL does.
    ///**
    // * Lisää uusi asiakas tietokantaan
    // * @param asiakas
    // */
    /* Korvattu vanha metodi
    public void lisaaAsiakas(Asiakas asiakas) {
        String sqlAsiakas = "INSERT INTO Asiakas (katuosoite, postinumero, email, puhelin) VALUES (?, ?, ?, ?) RETURNING asiakas_id";

        try (Connection conn = Database.getConnection();
             PreparedStatement psAsiakas = conn.prepareStatement(sqlAsiakas, Statement.RETURN_GENERATED_KEYS)) {

            psAsiakas.setString(1, asiakas.getKatuosoite());
            psAsiakas.setString(2, asiakas.getPostialue().getPostinumero());
            psAsiakas.setString(3, asiakas.getEmail());
            psAsiakas.setString(4, asiakas.getPuhelin());
            psAsiakas.executeUpdate();

            ResultSet generatedKeys = psAsiakas.getGeneratedKeys();
            if (generatedKeys.next()) {
                int asiakasId = generatedKeys.getInt(1);

                if (asiakas instanceof Yksityishenkilo yksityinen) {
                    String sqlYksityinen = "INSERT INTO Yksityishenkilo (asiakas_id, etunimi, sukunimi) VALUES (?, ?, ?)";
                    try (PreparedStatement psYksityinen = conn.prepareStatement(sqlYksityinen)) {
                        psYksityinen.setInt(1, asiakasId);
                        psYksityinen.setString(2, yksityinen.getEtunimi());
                        psYksityinen.setString(3, yksityinen.getSukunimi());
                        psYksityinen.executeUpdate();
                    }
                } else if (asiakas instanceof Yritys yritys) {
                    String sqlYritys = "INSERT INTO Yritys (asiakas_id, yrityksen_nimi, y_tunnus) VALUES (?, ?, ?)";
                    try (PreparedStatement psYritys = conn.prepareStatement(sqlYritys)) {
                        psYritys.setInt(1, asiakasId);
                        psYritys.setString(2, yritys.getYrityksenNimi());
                        psYritys.setString(3, yritys.getYtunnus());
                        psYritys.executeUpdate();
                    }
                }

            } else {
                throw new SQLException("Asiakas ID:n luonti epäonnistui.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */
}






