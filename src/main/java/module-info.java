module com.example.lab {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires sqlite.jdbc;

    opens com.example.lab4 to javafx.fxml;
    exports com.example.lab4;
}