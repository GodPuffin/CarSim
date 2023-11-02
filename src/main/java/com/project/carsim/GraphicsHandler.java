package com.project.carsim;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class GraphicsHandler {
    private final Canvas canvas;
    private final GraphicsContext gc;
    public final double WIDTH;
    public final double HEIGHT;

    private WritableImage backgroundImage;
    private Surface previousSurface;


    public void update(Car car, Surface surface) {
        // Draw the stored background image
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0);
        }

        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        gc.save();

        if (surface != previousSurface) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            switch (surface) {
                case ASPHALT:
                    gc.setFill(Color.GRAY);
                    gc.fillRect(0, 0, width, height);

                    int numberOfSpeckles = 500;
                    gc.setFill(Color.DARKGRAY);
                    for (int i = 0; i < numberOfSpeckles; i++) {
                        double x = Math.random() * width;
                        double y = Math.random() * height;
                        gc.fillRect(x, y, 2, 2);
                    }
                    break;
                case GRAVEL:
                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(0, 0, width, height);

                    int numberOfStones = 1000;
                    gc.setFill(Color.LIGHTGRAY);
                    for (int i = 0; i < numberOfStones; i++) {
                        double x = Math.random() * width;
                        double y = Math.random() * height;
                        gc.fillOval(x, y, 5, 5);
                    }
                    break;
                case ICE:
                    gc.setFill(Color.LIGHTBLUE);
                    gc.fillRect(0, 0, width, height);

                    int numberOfPatches = 100;
                    gc.setFill(Color.WHITE);
                    for (int i = 0; i < numberOfPatches; i++) {
                        double x = Math.random() * width;
                        double y = Math.random() * height;
                        gc.fillOval(x, y, 20, 20);
                    }
                    break;
            }
            backgroundImage = canvas.snapshot(null, null);
            previousSurface = surface;
        }

        // Rotate the car to face the direction it is heading
        gc.transform(new Affine(new Rotate(Math.toDegrees(Math.atan2(car.directionHeading.y, car.directionHeading.x)), car.position.x, car.position.y)));

        // Draw wheels
        gc.setFill(Color.BLACK);
        drawWheel(car.position.x - 18, car.position.y - 12, false, car); // Front left
        drawWheel(car.position.x + 11, car.position.y - 12, true, car); // Front right
        drawWheel(car.position.x - 18, car.position.y + 7, false, car); // Rear left
        drawWheel(car.position.x + 11, car.position.y + 7, true, car); // Rear right

        // Draw car
        gc.setFill(Color.RED);
        gc.fillRect(car.position.x - (car.WHEELBASE /2), car.position.y - (car.TRACK /2), car.WHEELBASE, car.TRACK);

        // Windshield
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(car.position.x + 2, car.position.y - 8, 7, 16);

        // Spoiler
        gc.setFill(Color.DARKRED);
        gc.fillRect(car.position.x - 22, car.position.y - 11, 8, 22);

        gc.restore();
    }

    private void drawWheel(double x, double y, boolean isFrontWheel, Car car) {
        gc.save();
        if (isFrontWheel) {
            gc.transform(new Affine(new Rotate(Math.toDegrees(car.steeringAngle), x + 3.5, y + 2.5)));
        }
        gc.fillRect(x, y, 7, 5);
        gc.restore();
    }

    public GraphicsHandler(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.WIDTH = canvas.getWidth();
        this.HEIGHT = canvas.getHeight();
    }
}
