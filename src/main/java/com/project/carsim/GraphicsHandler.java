package com.project.carsim;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all graphics for the simulation
 * @author Marcus
 */
public class GraphicsHandler {

    /**
     * Width of the canvas
     */
    public final double WIDTH;

    /**
     * Height of the canvas
     */
    public final double HEIGHT;
    private final Canvas backgroundCanvas;
    private final Canvas dynamicCanvas;
    private final GraphicsContext bgGc;
    private final GraphicsContext dynGc;
    private final double cameraLag = 0.1;
    private double scaleFactor = 10;
    private Surface previousSurface;
    private Vector cameraPosition;
    private List<BackgroundElement> backgroundElements = new ArrayList<>();
    private List<BackgroundElement> skidMarks = new ArrayList<>();
    private Color backgroundColor = Color.BLACK;

    /**
     * Constructor
     * @param canvasContainer The pane to add the canvases to
     * @param width Width of the canvas
     * @param height Height of the canvas
     */
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

    /**
     * Update the graphics
     * @param car The car to draw
     * @param surface The surface to draw
     */
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
        dynGc.transform(new Affine(new Rotate(Math.toDegrees(car.getAngle()), car.getPosition().x, car.getPosition().y)));

        if (car.isSliding()) {
            // Calculate skid mark position for each wheel
            double distanceFromCenterX = car.getLength() / 2;
            double distanceFromCenterY = car.getWidth() / 2;

            double sn = Math.sin(car.getAngle());
            double cs = Math.cos(car.getAngle());


            double offsetX = (cs * distanceFromCenterX - sn * distanceFromCenterY);
            double offsetY = (sn * distanceFromCenterX + cs * distanceFromCenterY);
            double skidMarkX = car.getPosition().x + offsetX;
            double skidMarkY = car.getPosition().y + offsetY;
            skidMarks.add(new BackgroundElement(skidMarkX, skidMarkY, car.getWheelwidth(), "circle", Color.BLACK));

            offsetX = -(cs * distanceFromCenterX - sn * distanceFromCenterY);
            offsetY = -(sn * distanceFromCenterX + cs * distanceFromCenterY);
            skidMarkX = car.getPosition().x + offsetX;
            skidMarkY = car.getPosition().y + offsetY;
            skidMarks.add(new BackgroundElement(skidMarkX, skidMarkY, car.getWheelwidth(), "circle", Color.BLACK));

            offsetX = -(cs * distanceFromCenterX + sn * distanceFromCenterY);
            offsetY = -(sn * distanceFromCenterX - cs * distanceFromCenterY);
            skidMarkX = car.getPosition().x + offsetX;
            skidMarkY = car.getPosition().y + offsetY;
            skidMarks.add(new BackgroundElement(skidMarkX, skidMarkY, car.getWheelwidth(), "circle", Color.BLACK));

            offsetX = (cs * distanceFromCenterX + sn * distanceFromCenterY);
            offsetY = (sn * distanceFromCenterX - cs * distanceFromCenterY);
            skidMarkX = car.getPosition().x + offsetX;
            skidMarkY = car.getPosition().y + offsetY;
            skidMarks.add(new BackgroundElement(skidMarkX, skidMarkY, car.getWheelwidth(), "circle", Color.BLACK));

        }
        if (skidMarks.size() > 2000) {
            skidMarks.subList(0, 4).clear();
        }

        // Draw wheels
        dynGc.setFill(Color.BLACK);
        drawWheel(car.getPosition().x - car.getLength() / 2, car.getPosition().y - car.getWidth() / 2, false, car);
        drawWheel(car.getPosition().x + car.getLength() / 2, car.getPosition().y - car.getWidth() / 2, true, car);
        drawWheel(car.getPosition().x - car.getLength() / 2, car.getPosition().y + car.getWidth() / 2, false, car);
        drawWheel(car.getPosition().x + car.getLength() / 2, car.getPosition().y + car.getWidth() / 2, true, car);

        // Draw car
        dynGc.setFill(Color.RED);
        dynGc.fillRect(car.getPosition().x - (car.getLength() / 2) - (car.getWheellength() / 2) - 0.2, car.getPosition().y - (car.getWidth() / 2), car.getLength() + car.getWheellength() + 0.4, car.getWidth());

        // Windshield
        dynGc.setFill(Color.ALICEBLUE);
        dynGc.fillRect(car.getPosition().x + (car.getLength() / 2) - 0.7, car.getPosition().y - (car.getWidth() / 2) + 0.2, 0.7, car.getWidth() - 0.4);

        // Spoiler
        dynGc.setFill(Color.DARKRED);
        dynGc.fillRect(car.getPosition().x - (car.getLength() / 2) - (car.getWheellength() / 2) - 0.2, car.getPosition().y - (car.getWidth() / 2) - 0.2, 0.8, car.getWidth() + 0.4);

        dynGc.restore();
    }

    /**
     * Update the camera position
     * @param car The car to follow
     */
    private void updateCameraPosition(Car car) {
        // Desired position based on car's position
        double targetX = car.getPosition().x - (WIDTH / 2) / scaleFactor;
        double targetY = car.getPosition().y - (HEIGHT / 2) / scaleFactor;

        // Interpolate between the current position and the target position
        cameraPosition.x += (targetX - cameraPosition.x) * cameraLag;
        cameraPosition.y += (targetY - cameraPosition.y) * cameraLag;
    }

    /**
     * Draw a wheel
     * @param x position x
     * @param y position y
     * @param isFrontWheel true if the wheel is a front wheel
     * @param car The car to draw the wheels on
     */
    private void drawWheel(double x, double y, boolean isFrontWheel, Car car) {
        dynGc.save();
        // Rotate the wheel based on the steering angle
        if (isFrontWheel) {
            dynGc.transform(new Affine(new Rotate(Math.toDegrees(car.getSteeringAngle()), x, y)));
        }
        // Draw the wheel
        dynGc.fillRect(x - car.getWheellength() / 2, y - car.getWheelwidth() / 2, car.getWheellength(), car.getWheelwidth());
        dynGc.restore();
    }

    /**
     * Update the background
     * @param surface The surface to draw
     */
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
        for (BackgroundElement element : skidMarks) {
            bgGc.setFill(element.color);
            bgGc.fillOval(element.x, element.y, element.size, element.size);
        }

        bgGc.restore();
    }

    /**
     * Generate background elements based on the surface
     * @param surface The surface to generate elements for
     */
    private void generateBackgroundElements(Surface surface) {
        switch (surface) {
            case ASPHALT:
                int numberOfSpeckles = 5000;
                for (int i = 0; i < numberOfSpeckles; i++) {
                    double x = Math.random() * WIDTH;
                    double y = Math.random() * HEIGHT;
                    backgroundElements.add(new BackgroundElement(x, y, 0.4, "rectangle", Color.DARKGRAY));
                    backgroundColor = Color.GRAY;
                }
                break;
            case GRAVEL:
                int numberOfStones = 10000;
                for (int i = 0; i < numberOfStones; i++) {
                    double x = Math.random() * WIDTH;
                    double y = Math.random() * HEIGHT;
                    backgroundElements.add(new BackgroundElement(x, y, 0.7, "circle", Color.LIGHTGRAY));
                    backgroundColor = Color.DARKGRAY;

                }
                break;
            case ICE:
                int numberOfPatches = 1000;
                for (int i = 0; i < numberOfPatches; i++) {
                    double x = Math.random() * WIDTH;
                    double y = Math.random() * HEIGHT;
                    backgroundElements.add(new BackgroundElement(x, y, 2, "circle", Color.WHITE));
                    backgroundColor = Color.LIGHTBLUE;
                }
                break;
        }
    }

    /**
     * Set the scale factor
     * @param scaleFactor The scale factor
     */
    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    /**
     * A background element
     */
    private static class BackgroundElement {
        double x;
        double y;
        double size;
        Color color;
        String shape;

        /**
         * Constructor
         * @param x position x
         * @param y position y
         * @param size size
         * @param shape shape
         * @param color color
         */
        public BackgroundElement(double x, double y, double size, String shape, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.shape = shape;
        }
    }
}
