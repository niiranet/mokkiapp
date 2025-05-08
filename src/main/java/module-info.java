module com.mokki.mokkiapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.mokki.mokkiapp to javafx.fxml;
    exports com.mokki.mokkiapp;
}