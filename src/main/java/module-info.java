module com.matheusadriel.pomodorotimer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.matheusadriel.pomodorotimer to javafx.fxml;
    exports com.matheusadriel.pomodorotimer;
}