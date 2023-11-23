package com.project.carsim;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.Set;

public class SimController {

    @FXML
    public Label gearLabel;
    @FXML
    LineChart xAccelChart, yAccelChart, xVelocChart, yVelocChart, spdChart;
    @FXML
    private Pane mainCanvas;
    @FXML
    ComboBox<String> surfaceComboBox;
    @FXML
    Button resetButton;
    @FXML
    Slider scaleSlider;
    @FXML
    ProgressBar throttleBar;
    @FXML
    ProgressBar rpmBar;

    private GraphicsHandler graphicsHandler;
    private Surface surface;
    private Car car;
    // Temporary to test Graphs
    private Grapher xAccelGraph;

    @FXML
    public void initialize() {
        graphicsHandler = new GraphicsHandler(mainCanvas, 1200, 900);
        surface = Surface.ASPHALT;

        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> graphicsHandler.setScaleFactor(newValue.doubleValue()));

        mainCanvas.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.focusOwnerProperty().addListener((observable, oldFocusOwner, newFocusOwner) -> {
                    if (newFocusOwner != mainCanvas) {
                        mainCanvas.requestFocus();
                    }
                });
            }
        });

        xAccelGraph = new Grapher(xAccelChart, "blue", -0.25, 1.25);
    }

    public void update(double deltaTime, Set<KeyCode> activeKeys, Car car) {

        this.car = car;
        car.update(deltaTime, activeKeys, surface);
        graphicsHandler.update(car, surface);

        throttleBar.setProgress(car.inputs.throttle);
        rpmBar.setProgress(car.engine.rpm/6000);

        xAccelGraph.update(car.inputs.throttle, deltaTime);

        gearLabel.setText("Current Gear: " + car.inputs.currentGear);
    }

    public void SurfaceSelected(ActionEvent actionEvent) {
        switch (surfaceComboBox.getValue()) {
            case "Asphalt":
                surface = Surface.ASPHALT;
                break;
            case "Gravel":
                surface = Surface.GRAVEL;
                break;
            case "Ice":
                surface = Surface.ICE;
                break;
        }
    }

    public void resetPressed(ActionEvent actionEvent) {
        car.reset(graphicsHandler.WIDTH/2, graphicsHandler.HEIGHT/2);
    }

    public void exitPressed(ActionEvent actionEvent) {
        System.exit(0);
    }
}