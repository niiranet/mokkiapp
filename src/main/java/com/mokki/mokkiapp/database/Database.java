package com.mokki.mokkiapp.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


/**
 * Hoitaa tietokantaan yhdistämisen ja siihen liittyvän virhekäsittelyn
 */
public class Database {

    public static void main(String[] args) {
    }

    public static Connection getConnection() throws SQLException {
        // tämä on kaikilla sama, kun käytetään setup_test.sql
        String URL = "jdbc:mysql://localhost:3306/lomakyla";

        String USER = null;
        String PASSWORD = null;
        Properties properties = new Properties();
        //try (FileInputStream fis = new FileInputStream("src/db_credentials.properties")) {
        try (FileInputStream fis = new FileInputStream("src/main/java/com/mokki/mokkiapp/db_credentials.properties")) {
            properties.load(fis);
            USER = properties.getProperty("db.username");
            PASSWORD = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // yhteyden luominen mysql serveriin
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}