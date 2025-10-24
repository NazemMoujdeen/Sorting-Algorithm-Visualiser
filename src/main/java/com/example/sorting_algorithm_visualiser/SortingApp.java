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
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.example.sorting_algorithm_visualiser.SortAction;
import com.example.sorting_algorithm_visualiser.SortingStrategy;


import java.io.IOException;


public class SortingApp extends Application {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    private static final int ARRAY_SIZE = 100;

    private int[] array;
    private Pane canvas;

    //made all the ui controls into fields instead so the disable control method can access them
    private Button startButton;
    private Button resetButton;
    private Slider speedSlider;
    private Slider arraySizeSlider;
    private ChoiceBox<String> choiceBox;
    //map to store the sorting strategies
    private Map<String, SortingStrategy> sortingStrategies;

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
        choiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(sortingAlgorithms));
        choiceBox.setValue("Bubble Sort"); // default sort

        //creates a speed slider to adjust the speed at which the bars are sorted
        speedSlider = new Slider();
        Label speedLabel = new Label("Speed");
        //changed the range so there is better control in the speed of the slider
        speedSlider.setMin(1);
        speedSlider.setMax(501);
        speedSlider.setValue(250);
        speedSlider.setPrefWidth(150);
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {});

        //creates an array size slider to adjust how many bars there are
        arraySizeSlider = new Slider();
        Label arraySizeLabel = new Label("Array Size");
        arraySizeSlider.setMin(10);
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

        startButton = new Button("Sort");
        startButton.setOnAction(event -> {
            startSorting();
        });
        resetButton = new Button("Reset");
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
            bar.setWidth(barWidth - 1); // subtracts 1 so there is a small gap between bars
            bar.setHeight(value);

            // sets the colour of bar to blue
            bar.setFill(Color.AQUAMARINE);

            // add s the newly created bar to the canvas
            canvas.getChildren().add(bar);
        }
    }
    //not strictly needed for intellij as it is implied but needed if opened elsewhere
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * starts the sorting based on what sorting is chosen
     */
    private void startSorting() {
        setControlsDisabled(true); // disables buttons
        String selectedAlgorithmName = choiceBox.getValue();
        if (selectedAlgorithmName == null) {
            setControlsDisabled(false);
            return;
        }

        List<SortAction> actions;

        switch (selectedAlgorithmName) {
            case "Bubble Sort":
                actions = bubbleSort(this.array);
                break;
            case "Merge Sort":
                actions = mergeSort(this.array);
                break;
            default:
                actions = new ArrayList<>();
                break;
        }

        SorterTask sorterTask = new SorterTask(actions, speedSlider);

        // turns the buttons back on when its all finished
        sorterTask.setOnSucceeded(e -> {
            setControlsDisabled(false);
        });

        new Thread(sorterTask).start();
    }

    /**
     * turns off all buttons
     */
    private void setControlsDisabled(boolean disabled) {
        startButton.setDisable(disabled);
        resetButton.setDisable(disabled);
        arraySizeSlider.setDisable(disabled);
        choiceBox.setDisable(disabled);
    }

    /**
     * this runs on a background thread
     */
    public class SorterTask extends javafx.concurrent.Task<Void> {
        private final List<SortAction> actions;
        private final Slider speedSlider;

        public SorterTask(List<SortAction> actions, Slider speedSlider) {
            this.actions = actions;
            this.speedSlider = speedSlider;
        }

        @Override
        protected Void call() throws Exception {
            for (SortAction action : actions) {
                if (isCancelled()) break;

                // sees what action to perform
                if (action instanceof SwapAction sa) {
                    int temp = array[sa.index1];
                    array[sa.index1] = array[sa.index2];
                    array[sa.index2] = temp;
                } else if (action instanceof OverwriteAction oa) {
                    array[oa.index] = oa.value;
                }

                // redraws on the main ui thread
                Platform.runLater(this::drawArray);

                // pause the animation based on the slider value
                try {
                    Thread.sleep((long) (501 - speedSlider.getValue()));
                } catch (InterruptedException e) {
                    if (isCancelled()) {
                        break;
                    }
                }
            }
            return null;
        }

        private void drawArray() {
            SortingApp.this.drawArray();
        }
    }


    /**
     * Bubble Sort algorithm.
     * Returns a List of SwapActions
     */
    public List<SortAction> bubbleSort(int[] array) {
        List<SortAction> actions = new ArrayList<>();
        int[] tempArray = Arrays.copyOf(array, array.length);

        boolean notSorted = true;
        while (notSorted) {
            notSorted = false;
            for (int i = 0; i < tempArray.length - 1; i++) {
                if (tempArray[i] > tempArray[i + 1]) {
                    // perform the swap on the temporary array
                    int temp = tempArray[i];
                    tempArray[i] = tempArray[i + 1];
                    tempArray[i + 1] = temp;

                    // records the action
                    actions.add(new SwapAction(i, i + 1));
                    notSorted = true;
                }
            }
        }
        return actions;
    }

    /**
     * --- NEW ---
     * Merge Sort algorithm.
     * Returns a list of OverwriteActions
     */
    public List<SortAction> mergeSort(int[] array) {
        List<SortAction> actions = new ArrayList<>();
        int[] tempArray = Arrays.copyOf(array, array.length);
        int[] helper = new int[tempArray.length];

        mergeSortHelper(tempArray, helper, 0, tempArray.length - 1, actions);
        return actions;
    }

    private void mergeSortHelper(int[] arr, int[] helper, int low, int high, List<SortAction> actions) {
        if (low < high) {
            int middle = low + (high - low) / 2;
            // sort the left half
            mergeSortHelper(arr, helper, low, middle, actions);
            // sort the right half
            mergeSortHelper(arr, helper, middle + 1, high, actions);
            // merges then
            merge(arr, helper, low, middle, high, actions);
        }
    }

    private void merge(int[] arr, int[] helper, int low, int middle, int high, List<SortAction> actions) {
        for (int i = low; i <= high; i++) {
            helper[i] = arr[i];
        }

        int helperLeft = low;
        int helperRight = middle + 1;
        int current = low;

        while (helperLeft <= middle && helperRight <= high) {
            if (helper[helperLeft] <= helper[helperRight]) {
                arr[current] = helper[helperLeft];
                // record the overwrite action
                actions.add(new OverwriteAction(current, arr[current]));
                helperLeft++;
            } else {
                arr[current] = helper[helperRight];
                actions.add(new OverwriteAction(current, arr[current]));
                helperRight++;
            }
            current++;
        }

        // copies the rest of the left side of the array into the target array
        int remaining = middle - helperLeft;
        for (int i = 0; i <= remaining; i++) {
            arr[current + i] = helper[helperLeft + i];
            actions.add(new OverwriteAction(current + i, arr[current + i]));
        }
    }

}
