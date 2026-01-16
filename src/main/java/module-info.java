module com.mycompany.ecotrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    
    opens com.mycompany.ecotrack to javafx.fxml;
    opens modelos to javafx.base;
    exports com.mycompany.ecotrack;
}
