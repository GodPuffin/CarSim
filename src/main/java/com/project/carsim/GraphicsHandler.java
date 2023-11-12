package com.project.carsim;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class GraphicsHandler {
    private final Canvas backgroundCanvas;
    private final Canvas dynamicCanvas;
    private final GraphicsContext bgGc;
    private final GraphicsContext dynGc;
    public final double WIDTH;
    public final double HEIGHT;

    private double scaleFactor = 10;

    private WritableImage backgroundImage;
    private Surface previousSurface;


    public void update(Car car, Surface surface) {

        updateBackground(surface);

        dynGc.clearRect(0, 0, WIDTH, HEIGHT);
        dynGc.save();
        dynGc.scale(scaleFactor, scaleFactor);
//        bgGc.scale(scaleFactor, scaleFactor);

        // Rotate the car to face the direction it is heading
        dynGc.transform(new Affine(new Rotate(Math.toDegrees(Math.atan2(car.directionHeading.y, car.directionHeading.x)), car.position.x, car.position.y)));

        // Draw wheels
        dynGc.setFill(Color.BLACK);
        drawWheel(car.position.x - 1.8, car.position.y - 1.2, false, car);
        drawWheel(car.position.x + 1.1, car.position.y - 1.2, true, car);
        drawWheel(car.position.x - 1.8, car.position.y + 0.7, false, car);
        drawWheel(car.position.x + 1.1, car.position.y + 0.7, true, car);

        // Draw car
        dynGc.setFill(Color.RED);
        dynGc.fillRect(car.position.x - (car.WHEELBASE /2), car.position.y - (car.TRACK /2), car.WHEELBASE, car.TRACK);

        // Windshield
        dynGc.setFill(Color.ALICEBLUE);
        dynGc.fillRect(car.position.x + 0.2, car.position.y - 0.8, 0.7, 1.6);

        // Spoiler
        dynGc.setFill(Color.DARKRED);
        dynGc.fillRect(car.position.x - 2.2, car.position.y - 1.1, 0.8, 2.2);

        dynGc.restore();
    }

    private void drawWheel(double x, double y, boolean isFrontWheel, Car car) {
        dynGc.save();
        if (isFrontWheel) {
            dynGc.transform(new Affine(new Rotate(Math.toDegrees(car.steeringAngle), x + car.WHEELDIAMETER/2, y + car.WHEELWIDTH/2)));
        }
        dynGc.fillRect(x, y, car.WHEELDIAMETER, car.WHEELWIDTH);
        dynGc.restore();
    }

    private void updateBackground(Surface surface) {

        bgGc.save();

        if (surface != previousSurface) {
            bgGc.clearRect(0, 0, WIDTH, HEIGHT);

            switch (surface) {
                case ASPHALT:
                    bgGc.setFill(Color.GRAY);
                    bgGc.fillRect(0, 0, WIDTH, WIDTH);

                    int numberOfSpeckles = 500;
                    bgGc.setFill(Color.DARKGRAY);
                    for (int i = 0; i < numberOfSpeckles; i++) {
                        double x = Math.random() * WIDTH;
                        double y = Math.random() * HEIGHT;
                        bgGc.fillRect(x, y, 2, 2);
                    }
                    break;
                case GRAVEL:
                    bgGc.setFill(Color.DARKGRAY);
                    bgGc.fillRect(0, 0, WIDTH, HEIGHT);

                    int numberOfStones = 1000;
                    bgGc.setFill(Color.LIGHTGRAY);
                    for (int i = 0; i < numberOfStones; i++) {
                        double x = Math.random() * WIDTH;
                        double y = Math.random() * HEIGHT;
                        bgGc.fillOval(x, y, 5, 5);
                    }
                    break;
                case ICE:
                    bgGc.setFill(Color.LIGHTBLUE);
                    bgGc.fillRect(0, 0, WIDTH, HEIGHT);

                    int numberOfPatches = 100;
                    bgGc.setFill(Color.WHITE);
                    for (int i = 0; i < numberOfPatches; i++) {
                        double x = Math.random() * WIDTH;
                        double y = Math.random() * HEIGHT;
                        bgGc.fillOval(x, y, 20, 20);
                    }
                    break;
            }
            backgroundImage = backgroundCanvas.snapshot(null, null);
            previousSurface = surface;
        } else {
            bgGc.scale(scaleFactor, scaleFactor);
            bgGc.drawImage(backgroundImage, 0, 0);
        }
        bgGc.restore();
    }

    public GraphicsHandler(Pane canvasContainer, double width, double height) {
        this.WIDTH = width;
        this.HEIGHT = height;

        this.backgroundCanvas = new Canvas(width, height);
        this.dynamicCanvas = new Canvas(width, height);

        this.bgGc = backgroundCanvas.getGraphicsContext2D();
        this.dynGc = dynamicCanvas.getGraphicsContext2D();

        canvasContainer.getChildren().addAll(backgroundCanvas, dynamicCanvas);
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
