package com.project.carsim;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class GraphicsHandler {
    public final double WIDTH;
    public final double HEIGHT;
    private final Canvas backgroundCanvas;
    private final Canvas dynamicCanvas;
    private final GraphicsContext bgGc;
    private final GraphicsContext dynGc;
    private final double cameraLag = 0.02;
    private double scaleFactor = 10;
    private Surface previousSurface;
    private Vector cameraPosition;
    private List<BackgroundElement> backgroundElements = new ArrayList<>();
    private Color backgroundColor = Color.BLACK;

    public GraphicsHandler(Pane canvasContainer, double width, double height) {
        this.WIDTH = width;
        this.HEIGHT = height;

        this.cameraPosition = new Vector(WIDTH / 2, HEIGHT / 2);

        this.backgroundCanvas = new Canvas(width, height);
        this.dynamicCanvas = new Canvas(width, height);

        this.bgGc = backgroundCanvas.getGraphicsContext2D();
        this.dynGc = dynamicCanvas.getGraphicsContext2D();

        canvasContainer.getChildren().addAll(backgroundCanvas, dynamicCanvas);
    }

    public void update(Car car, Surface surface) {

        updateBackground(surface);

        dynGc.clearRect(0, 0, WIDTH, HEIGHT);
        dynGc.save();
        dynGc.scale(scaleFactor, scaleFactor);

        // Update camera position based on the car's position
        updateCameraPosition(car);

        // Translate dynamic canvas to follow the car
        dynGc.translate(-cameraPosition.x, -cameraPosition.y);
        bgGc.setTransform(1, 0, 0, 1, 0, 0);
        bgGc.translate(-cameraPosition.x * scaleFactor, -cameraPosition.y * scaleFactor);

        // Rotate the car to face the direction it is heading
        dynGc.transform(new Affine(new Rotate(Math.toDegrees(Math.atan2(car.directionFacing.y, car.directionFacing.x)), car.position.x, car.position.y)));

        // Draw wheels
        dynGc.setFill(Color.BLACK);
        drawWheel(car.position.x - 1.8, car.position.y - 1.2, false, car);
        drawWheel(car.position.x + 1.1, car.position.y - 1.2, true, car);
        drawWheel(car.position.x - 1.8, car.position.y + 0.7, false, car);
        drawWheel(car.position.x + 1.1, car.position.y + 0.7, true, car);

        // Draw car
        dynGc.setFill(Color.RED);
        dynGc.fillRect(car.position.x - (car.WHEELBASE / 2), car.position.y - (car.TRACK / 2), car.WHEELBASE, car.TRACK);

        // Windshield
        dynGc.setFill(Color.ALICEBLUE);
        dynGc.fillRect(car.position.x + 0.2, car.position.y - 0.8, 0.7, 1.6);

        // Spoiler
        dynGc.setFill(Color.DARKRED);
        dynGc.fillRect(car.position.x - 2.2, car.position.y - 1.1, 0.8, 2.2);

        dynGc.restore();
    }

    private void updateCameraPosition(Car car) {
        // Desired position based on car's position
        double targetX = car.position.x - (WIDTH / 2) / scaleFactor;
        double targetY = car.position.y - (HEIGHT / 2) / scaleFactor;

        // Interpolate between the current position and the target position
        cameraPosition.x += (targetX - cameraPosition.x) * cameraLag;
        cameraPosition.y += (targetY - cameraPosition.y) * cameraLag;
    }

    private void drawWheel(double x, double y, boolean isFrontWheel, Car car) {
        dynGc.save();
        if (isFrontWheel) {
            dynGc.transform(new Affine(new Rotate(Math.toDegrees(car.inputs.steeringAngle), x + car.WHEELDIAMETER / 2, y + car.WHEELWIDTH / 2)));
        }
        dynGc.fillRect(x, y, car.WHEELDIAMETER, car.WHEELWIDTH);
        dynGc.restore();
    }

    private void updateBackground(Surface surface) {
        bgGc.save();
        bgGc.scale(scaleFactor, scaleFactor);
        bgGc.clearRect(-500, -500, WIDTH + 1000, HEIGHT + 1000);

        if (surface != previousSurface) {
            backgroundElements.clear();
            generateBackgroundElements(surface);
            previousSurface = surface;
        }
        bgGc.setFill(backgroundColor);
        bgGc.fillRect(0, 0, WIDTH, HEIGHT);
        for (BackgroundElement element : backgroundElements) {
            bgGc.setFill(element.color);
            switch (element.shape) {
                case "circle":
                    bgGc.fillOval(element.x, element.y, element.size, element.size);
                    break;
                case "rectangle":
                    bgGc.fillRect(element.x, element.y, element.size, element.size);
                    break;
            }
        }

        bgGc.restore();
    }

    private void generateBackgroundElements(Surface surface) {
        switch (surface) {
            case ASPHALT:
                int numberOfSpeckles = 500;
                for (int i = 0; i < numberOfSpeckles; i++) {
                    double x = Math.random() * WIDTH;
                    double y = Math.random() * HEIGHT;
                    backgroundElements.add(new BackgroundElement(x, y, 2, "rectangle", Color.DARKGRAY));
                    backgroundColor = Color.GRAY;
                }
                break;
            case GRAVEL:
                int numberOfStones = 1000;
                for (int i = 0; i < numberOfStones; i++) {
                    double x = Math.random() * WIDTH;
                    double y = Math.random() * HEIGHT;
                    backgroundElements.add(new BackgroundElement(x, y, 5, "circle", Color.LIGHTGRAY));
                    backgroundColor = Color.DARKGRAY;

                }
                break;
            case ICE:
                int numberOfPatches = 100;
                for (int i = 0; i < numberOfPatches; i++) {
                    double x = Math.random() * WIDTH;
                    double y = Math.random() * HEIGHT;
                    backgroundElements.add(new BackgroundElement(x, y, 20, "circle", Color.WHITE));
                    backgroundColor = Color.LIGHTBLUE;
                }
                break;
        }
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    // Define a class for background elements
    private static class BackgroundElement {
        double x;
        double y;
        double size;
        Color color;
        String shape;

        public BackgroundElement(double x, double y, double size, String shape, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.shape = shape;
        }
    }
}
