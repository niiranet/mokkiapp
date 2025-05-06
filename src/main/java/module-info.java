module com.mokki.mokkiapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mokki.mokkiapp to javafx.fxml;
    exports com.mokki.mokkiapp;
}