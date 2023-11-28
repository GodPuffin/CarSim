package com.project.carsim;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.Set;

/**
 * Controller class for the simulation
 */
public class SimController {

    // FXML elements
    @FXML
    private LineChart xAccelChart, yAccelChart, xVelocChart, yVelocChart, spdChart, throttleChart;
    @FXML
    private Pane mainCanvas;
    @FXML
    private ComboBox<String> surfaceComboBox;
    @FXML
    private Slider scaleSlider, engineSlider, weightSlider;

    // Other variables
    private GraphicsHandler graphicsHandler;
    private Surface surface;
    private Car car;
    private Grapher xAccelGraph, yAccelGraph, xVelocGraph, yVelocGraph, spdGraph, throttleGraph;

    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        // Initialize graphics handler
        graphicsHandler = new GraphicsHandler(mainCanvas, 1200, 900);
        // Initialize surface
        surface = Surface.ASPHALT;

        // Set up scale slider
        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> graphicsHandler.setScaleFactor(newValue.doubleValue()));

        //Set up engine slider
        engineSlider.valueProperty().addListener((observable, oldValue, newValue) -> car.setEnginePower((double) newValue));

        //Set up weight slider
        weightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            car.setMass((double) newValue);
            car.setInertia((double) newValue);
            car.setWeight(0.5 * 9.8 * (double) newValue);

        });

        // Keep continual focus on canvas
        mainCanvas.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.focusOwnerProperty().addListener((observable, oldFocusOwner, newFocusOwner) -> {
                    if (newFocusOwner != mainCanvas) {
                        mainCanvas.requestFocus();
                    }
                });
            }
        });

        // Initialize graphers
        xAccelGraph = new Grapher(xAccelChart, "blue");
        yAccelGraph = new Grapher(yAccelChart, "red");
        xVelocGraph = new Grapher(xVelocChart, "blue");
        yVelocGraph = new Grapher(yVelocChart, "red");
        spdGraph = new Grapher(spdChart, "green");
        throttleGraph = new Grapher(throttleChart, "pink");
    }

    /**
     * Updates the simulation
     * @param deltaTime Time since last update
     * @param activeKeys Set of active keys
     * @param car Car object
     */
    public void update(double deltaTime, Set<KeyCode> activeKeys, Car car) {

        // Update car and graphics
        this.car = car;
        car.update(deltaTime, activeKeys, surface);
        graphicsHandler.update(car, surface);

        // Update graphs
        xAccelGraph.update(car.getAcceleration_wc().x, deltaTime);
        yAccelGraph.update(-car.getAcceleration_wc().y, deltaTime);
        xVelocGraph.update(car.getVelocity_wc().x, deltaTime);
        yVelocGraph.update(-car.getVelocity_wc().y, deltaTime);
        spdGraph.update(car.getVelocity_wc().magnitude(), deltaTime);
        throttleGraph.update(car.getThrottle(), deltaTime);
    }

    /**
     * Called when a new surface is selected
     */
    public void SurfaceSelected() {
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

    /**
     * Called when the reset button is pressed
     */
    public void resetPressed() {
        scaleSlider.setValue(5);
        weightSlider.setValue(1500);
        engineSlider.setValue(15000);
        graphicsHandler.clearSkidmarks();
        surfaceComboBox.setValue("Asphalt");
        car.reset();
    }

    /**
     * Called when the exit button is pressed
     */
    public void exitPressed() {
        System.exit(0);
    }
}