package com.example.sorting_algorithm_visualiser;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;


import java.io.IOException;
import java.util.Random;

public class SortingApp extends Application {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    private static final int ARRAY_SIZE = 100;

    private int[] array;
    private Pane canvas;

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();

        // create the canvas where the bars will be drawn
        canvas = new Pane();
        canvas.setStyle("-fx-background-color: black;"); // A dark background for the canvas
        root.setCenter(canvas);

        // Perform the initial draw of the array by listing to what the slider says
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            drawArray();
        });
        // Generate the first random array
        this.array = generateRandomArray(ARRAY_SIZE);



        //creates a hbox for all the setting buttons and sliders
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(40);
        root.setTop(hBox);

        String[] sortingAlgorithms = {"Bubble Sort", "Merge Sort"};
        ChoiceBox<String> choiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(sortingAlgorithms));

        //creates a speed slider to adjust the speed at which the bars are sorted
        Slider speedSlider = new Slider();
        Label speedLabel = new Label("Speed");
        speedSlider.setMin(0);
        speedSlider.setMax(100);
        speedSlider.setValue(50);
        speedSlider.setPrefWidth(150);
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {});

        //creates an array size slider to adjust how many bars there are
        Slider arraySizeSlider = new Slider();
        Label arraySizeLabel = new Label("Array Size");
        arraySizeSlider.setMin(2);
        arraySizeSlider.setMax(ARRAY_SIZE);
        arraySizeSlider.setValue(10);
        arraySizeSlider.setPrefWidth(150);
        arraySizeSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging) { // If the user has released the mouse
                // Get the new size, cast to int, generate new array, and draw it
                int newSize = (int) arraySizeSlider.getValue();
                this.array = generateRandomArray(newSize);
                drawArray();
            }
        });

        Button startButton = new Button("Sort");
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            int newSize = (int) arraySizeSlider.getValue();
            this.array = generateRandomArray(newSize);
            drawArray();});

        //adds all the settings to the hbox
        hBox.getChildren().addAll(
                resetButton, startButton, choiceBox,
                arraySizeLabel, arraySizeSlider,
                speedLabel, speedSlider
        );



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
        double canvasHeight = canvas.getHeight();

        for (int i = 0; i < array.length; i++) {
            int value = array[i];
            Rectangle bar = new Rectangle();

            // Set the position and size of the bar
            // X position is based on the index and bar width
            bar.setX(i * barWidth);
            // Y position is calculated from the bottom of the canvas
            bar.setY(canvasHeight - value);
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
