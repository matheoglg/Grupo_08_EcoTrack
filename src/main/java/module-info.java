module com.mycompany.ecotrack {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.ecotrack to javafx.fxml;
    exports com.mycompany.ecotrack;
}
