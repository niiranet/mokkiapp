package com.mokki.mokkiapp.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


/**
 * tämä luokka hoitaa tietokantaan yhdistämisen ja siihen liittyvän virhekäsittelyn
 */
public class Database {

    public static void main(String[] args) {
    }

    public static Connection getConnection() throws SQLException {
        // tämä on kaikilla sama, kun käytetään setup_test.sql
        String URL = "jdbc:mysql://localhost:3306/lomakyla";
        /*
        // TODO: saada tämä toimimaan mielummin
        // nämä haetaan ympäristömuuttujina
        String user = System.getenv("OTI_USER");
        String password = System.getenv("OTI_PASS");

        if (user != null && password != null) {
            System.out.println("Environment variables: OK");
        }
        else {
            System.out.println("Environment variables: null");
            System.exit(1);
        }
        */

        // vaihtoehtoinen ratkaisu
        String USER = null;
        String PASSWORD = null;
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/db_credentials.properties")) {
            properties.load(fis);
            USER = properties.getProperty("db.username");
            PASSWORD = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // yhteyden luominen mysql serveriin
        return DriverManager.getConnection(URL, USER, PASSWORD);


        /*
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Connected to the src.database.database!" + "\n");
                Statement statement = connection.createStatement();

                //suoritaTestikyselyt(statement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        */

    }

}