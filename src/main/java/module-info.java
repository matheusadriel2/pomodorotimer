module com.matheusadriel.pomodorotimer {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires fr.brouillard.oss.cssfx;

    opens com.matheusadriel.pomodorotimer to javafx.fxml;
    opens com.matheusadriel.pomodorotimer.ui to javafx.fxml;
    exports com.matheusadriel.pomodorotimer;
    exports com.matheusadriel.pomodorotimer.model;

    exports com.matheusadriel.pomodorotimer.ui;
}
