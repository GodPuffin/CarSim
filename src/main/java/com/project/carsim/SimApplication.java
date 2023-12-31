package com.project.carsim;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Main class for the simulation

 */
public class SimApplication extends Application {

    private long lastFrameTime;

    /**
     * Main method
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Starts the application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException if an error occurs during loading
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimApplication.class.getResource("sim-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);

        Set<KeyCode> activeKeys = new HashSet<>();

        Car car = new Car();

        SimController controller = fxmlLoader.getController();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            long now = System.nanoTime();
            double deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
            lastFrameTime = now;

            controller.update(deltaTime, activeKeys, car);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));

        timeline.play();

        stage.setTitle("CarSim");
        stage.setResizable(false);
        stage.setFullScreenExitHint("Arrow Keys to move the Car \n Esc to exit full screen");
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
}