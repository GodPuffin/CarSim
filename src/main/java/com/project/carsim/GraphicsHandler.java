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
    private final double cameraLag = 0.05;
    private double scaleFactor = 10;
    private Surface previousSurface;
    private Vector cameraPosition;
    private List<BackgroundElement> backgroundElements = new ArrayList<>();
    private Color backgroundColor = Color.BLACK;

    // Constructor
    public GraphicsHandler(Pane canvasContainer, double width, double height) {
        this.WIDTH = width;
        this.HEIGHT = height;

        this.cameraPosition = new Vector(WIDTH / 2, HEIGHT / 2);

        // Create two canvases, one for the background and one for the dynamic elements
        this.backgroundCanvas = new Canvas(width, height);
        this.dynamicCanvas = new Canvas(width, height);

        this.bgGc = backgroundCanvas.getGraphicsContext2D();
        this.dynGc = dynamicCanvas.getGraphicsContext2D();

        canvasContainer.getChildren().addAll(backgroundCanvas, dynamicCanvas);
    }

    public void update(Car car, Surface surface) {

        // Update background
        updateBackground(surface);

        // clear dynamic canvas
        dynGc.clearRect(0, 0, WIDTH, HEIGHT);
        dynGc.save();

        // Scale dynamic canvas
        dynGc.scale(scaleFactor, scaleFactor);

        // Update camera position based on the car's position
        updateCameraPosition(car);

        // Translate both canvas to follow the car
        dynGc.translate(-cameraPosition.x, -cameraPosition.y);
        bgGc.setTransform(1, 0, 0, 1, 0, 0);
        bgGc.translate(-cameraPosition.x * scaleFactor, -cameraPosition.y * scaleFactor);

        // Rotate the car to face the direction it is heading
        dynGc.transform(new Affine(new Rotate(Math.toDegrees(car.angle), car.position.x, car.position.y)));

        // Draw wheels
        dynGc.setFill(Color.BLACK);
        drawWheel(car.position.x - car.length/2, car.position.y - car.width/2, false, car);
        drawWheel(car.position.x + car.length/2, car.position.y - car.width/2, true, car);
        drawWheel(car.position.x - car.length/2, car.position.y + car.width/2, false, car);
        drawWheel(car.position.x + car.length/2, car.position.y + car.width/2, true, car);

        // Draw car
        dynGc.setFill(Color.RED);
        dynGc.fillRect(car.position.x - (car.length / 2) - (car.wheellength/2) - 0.2, car.position.y - (car.width / 2), car.length + car.wheellength + 0.4, car.width);

        // Windshield
        dynGc.setFill(Color.ALICEBLUE);
        dynGc.fillRect(car.position.x + (car.length/2) - 0.7, car.position.y - (car.width/2) + 0.2, 0.7, car.width - 0.4);

        // Spoiler
        dynGc.setFill(Color.DARKRED);
        dynGc.fillRect(car.position.x - (car.length/2) - (car.wheellength/2) - 0.2, car.position.y - (car.width/2) - 0.2, 0.8, car.width + 0.4);

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
        // Rotate the wheel based on the steering angle
        if (isFrontWheel) {
            dynGc.transform(new Affine(new Rotate(Math.toDegrees(car.inputs.steeringAngle), x, y)));
        }
        // Draw the wheel
        dynGc.fillRect(x - car.wheellength/2, y - car.wheelwidth/2, car.wheellength, car.wheelwidth);
        dynGc.restore();
    }

    private void updateBackground(Surface surface) {

        bgGc.save();

        // Scale & clear background canvas
        bgGc.scale(scaleFactor, scaleFactor);
        bgGc.clearRect(-500, -500, WIDTH + 1000, HEIGHT + 1000);

        // Generate background elements if the surface has changed
        if (surface != previousSurface) {
            backgroundElements.clear();
            generateBackgroundElements(surface);
            previousSurface = surface;
        }

        // Draw background elements
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

    // Generate background elements based on the surface
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

    // Background elements class
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
