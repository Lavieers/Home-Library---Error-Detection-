module com.example.projekt1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.example.projekt1 to javafx.fxml;
    exports com.example.projekt1;
}