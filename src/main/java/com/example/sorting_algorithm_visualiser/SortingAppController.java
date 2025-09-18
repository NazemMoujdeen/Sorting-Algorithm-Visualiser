package com.example.sorting_algorithm_visualiser;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SortingAppController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
