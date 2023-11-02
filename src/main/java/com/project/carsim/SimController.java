package com.project.carsim;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;

import java.util.Set;

public class SimController {

    @FXML
    private Canvas mainCanvas;
    @FXML
    ComboBox<String> surfaceComboBox;
    @FXML
    Button resetButton;
    @FXML
    Button pauseButton;

    private GraphicsHandler graphicsHandler;
    private Surface surface;
    private Car car;

    @FXML
    public void initialize() {
        graphicsHandler = new GraphicsHandler(mainCanvas);
        surface = Surface.ASPHALT;

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