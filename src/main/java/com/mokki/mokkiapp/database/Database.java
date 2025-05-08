package com.mokki.mokkiapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * tämä luokka hoitaa tietokantaan yhdistämisen ja siihen liittyvän virhekäsittelyn
 */
public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/lomakyla";
    private static final String USER = "root"; // Korvaa omalla käyttäjätunnuksellasi tai voi olla automaattisesti root
    private static final String PASSWORD = ""; // Korvaa omalla salasanallasi

    public static void main(String[] args) {
        System.out.println("Nykyinen työhakemisto: " + System.getProperty("user.dir"));
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Tietokantayhteys onnistui!");
            }
        } catch (SQLException e) {
            System.err.println("Tietokantayhteyden muodostaminen epäonnistui:");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}