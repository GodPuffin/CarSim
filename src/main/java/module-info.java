module com.project.carsim {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.project.carsim to javafx.fxml;
    exports com.project.carsim;
}