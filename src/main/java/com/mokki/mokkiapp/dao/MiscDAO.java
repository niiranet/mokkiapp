package com.mokki.mokkiapp.dao;

import com.mokki.mokkiapp.model.*;
import com.mokki.mokkiapp.database.Database;

import java.sql.*;


/**
 * Luokka sekalaisille DAO-metodeille
 */
public class MiscDAO {

    /**
     * Tarkista onko postialue olemassa,
     * jos ei ole niin lisää uusi postialue.
     * @param postialue
     */
    public static void lisaaPostialue(Postialue postialue) {
        String tarkistusSql = "SELECT 1 FROM Postialue WHERE postinumero = ?";
        String lisaysSql = "INSERT INTO Postialue (postinumero, kunta, maa) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection()) {
            try (PreparedStatement tarkistus = conn.prepareStatement(tarkistusSql)) {
                tarkistus.setString(1, postialue.getPostinumero());
                ResultSet rs = tarkistus.executeQuery();
                if (rs.next()) {
                    // Postialue on jo olemassa, ei lisätä uudelleen
                    return;
                }
            }

            // Lisää uusi postialue
            try (PreparedStatement lisays = conn.prepareStatement(lisaysSql)) {
                lisays.setString(1, postialue.getPostinumero());
                lisays.setString(2, postialue.getKunta());
                lisays.setString(3, postialue.getMaa());
                lisays.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
