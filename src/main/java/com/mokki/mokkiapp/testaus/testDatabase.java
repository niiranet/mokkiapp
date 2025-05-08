package com.mokki.mokkiapp.testaus;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class testDatabase {

    /**
     * Debuggaus tarkoituksiin.
     * @param statement
     * @throws SQLException
     */
    private static void suoritaTestikyselyt(Statement statement) throws SQLException {
        List<String> queries = List.of(
                """
                SELECT * FROM Mökki;
                """,

                """
                UPDATE Mökki SET nimi = 'Moderni Rantamökki', hinta = 130.00, kuvaus = 'Mökki järven rannalla saunalla ja terassilla' WHERE mokki_id = 101;
                """,

                """
                INSERT INTO Mökki (mokki_id, nimi, katuosoite, postinumero, hinta, kuvaus) VALUES (109, 'TEST', 'TEST', '00100', 150.00, 'TEST');
                """,

                """
                SELECT * FROM Mökki;
                """,

                """
                SELECT * FROM Lasku;
                """,

                """
                INSERT INTO Lasku (lasku_id, varaus_id, summa, tilinro, erapaiva, maksupvm, maksettu) VALUES (5011, 1003, 600.00, 'FI2112345600000785', '2024-07-15', NULL, FALSE);
                """,

                """
                SELECT * FROM Lasku;
                """
        );

        for (String q : queries) {
            testaaKyselyt(statement, q, getSqlMethod(q) );
        }
    }


    private static void testaaKyselyt(Statement statement, String query, String method) throws SQLException {
        switch (method) {
            case "SELECT" -> {
                ResultSet rs = statement.executeQuery(query);
                tablePrint(rs);
                rs.close();
            }
            case "UPDATE", "INSERT", "DELETE" -> {
                int affectedRows = statement.executeUpdate(query);
                System.out.println("Rows affected: " + affectedRows + "\n");
            }
            default -> System.out.println("Unsupported SQL method: " + method);
        }

        // TODO: virhetilanteiden käsittely
    }



    // apumetodit

    public static String getSqlMethod(String query) {
        if (query == null || query.isBlank()) return null;
        return query.trim().split("\\s+")[0].toUpperCase();
    }

    private static void tablePrint(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        // Store all rows temporarily and track max width per column
        List<String[]> rows = new ArrayList<>();
        int[] columnWidths = new int[columnCount];

        // Initialize widths with column names
        for (int i = 0; i < columnCount; i++) {
            columnWidths[i] = meta.getColumnName(i + 1).length();
        }

        // Read and store rows
        while (rs.next()) {
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                String value = rs.getString(i + 1);
                if (value == null) value = "NULL";
                row[i] = value;
                columnWidths[i] = Math.max(columnWidths[i], value.length());
            }
            rows.add(row);
        }

        // Print column headers
        for (int i = 0; i < columnCount; i++) {
            System.out.printf("%-" + (columnWidths[i] + 2) + "s", meta.getColumnName(i + 1));
        }
        System.out.println();

        // Print separator
        for (int i = 0; i < columnCount; i++) {
            System.out.print("-".repeat(columnWidths[i] + 2));
        }
        System.out.println();

        // Print rows
        for (String[] row : rows) {
            for (int i = 0; i < columnCount; i++) {
                System.out.printf("%-" + (columnWidths[i] + 2) + "s", row[i]);
            }
            System.out.println();
        }

        System.out.println();
    }
}
