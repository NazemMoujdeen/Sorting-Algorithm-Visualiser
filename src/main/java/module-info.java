module com.example.sorting_algorithm_visualiser {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.sorting_algorithm_visualiser to javafx.fxml;
    exports com.example.sorting_algorithm_visualiser;
}