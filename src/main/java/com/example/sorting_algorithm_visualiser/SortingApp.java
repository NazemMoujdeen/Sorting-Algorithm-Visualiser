package com.example.sorting_algorithm_visualiser;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.io.IOException;
import java.util.Random;

public class SortingApp extends Application {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;
    private static final int ARRAY_SIZE = 100;

    private int[] array;
    private Pane canvas;

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();

        // Initialize the canvas where the bars will be drawn
        canvas = new Pane();
        canvas.setStyle("-fx-background-color: black;"); // A dark background for the canvas
        root.setCenter(canvas);

        // Generate the first random array
        this.array = generateRandomArray(ARRAY_SIZE);

        // Perform the initial draw of the array
        drawArray();

        // --- Standard JavaFX Scene and Stage setup ---
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Sorting Algorithm Visualizer");
        stage.setScene(scene);
        stage.show();
    }
    private int[] generateRandomArray(int size) {
        int [] generatedArray = new int[size];
        Random random = new Random();
        int maxHeight = WINDOW_HEIGHT - 50;
        for (int i = 0; i < size; i++) {
            generatedArray[i] = random.nextInt(maxHeight - 10) + 10;

        }
        return generatedArray;
    }
    private void drawArray() {
        canvas.getChildren().clear(); //removes previous bars
        double barWidth = (double) WINDOW_WIDTH / array.length;

        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            Rectangle bar = new Rectangle();

            // Set the position and size of the bar
            // X position is based on the index and bar width
            bar.setX(i * barWidth);
            // Y position is calculated from the bottom of the canvas
            bar.setY(WINDOW_HEIGHT - value);
            bar.setWidth(barWidth - 1); // Subtract 1 to create a small gap between bars
            bar.setHeight(value);

            // Style the bar
            bar.setFill(Color.AQUAMARINE);

            // Add the newly created bar to the canvas
            canvas.getChildren().add(bar);
        }
    }
    //not strictly needed for intellij as it is implied but needed if opened elsewhere
    public static void main(String[] args) {
        launch(args);
    }

}
