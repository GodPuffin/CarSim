package com.project.carsim;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.Set;

public class SimController {

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

    private GraphicsHandler graphicsHandler;
    private Surface surface;
    private Car car;

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
    }

    public void update(double deltaTime, Set<KeyCode> activeKeys, Car car) {
        this.car = car;
        car.update(deltaTime, activeKeys, surface);
        graphicsHandler.update(car, surface);

        throttleBar.setProgress(car.inputs.throttle);

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