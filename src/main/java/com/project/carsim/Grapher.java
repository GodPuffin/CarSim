package com.project.carsim;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Grapher {
    private final XYChart.Series<Number, Number> series;
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private double currentTime = 0.0;
    private final double updateInterval = 1.0 / 60.0;

    public Grapher(XYChart<Number, Number> chart, String color, double min, double max) {
        // Create a new series for the chart
        series = new XYChart.Series<>();
        chart.getData().add(series);

        // Get Axis objects from the chart
        xAxis = (NumberAxis) chart.getXAxis();
        yAxis = (NumberAxis) chart.getYAxis();

        // Set the bounds
        yAxis.setLowerBound(min);
        yAxis.setUpperBound(max);

        // Set the color
        series.getNode().setStyle("-fx-stroke: " + color + ";");
    }

    public void update(double value, double deltaTime) {

        // Add a new data point to the series
        series.getData().add(new XYChart.Data<>(currentTime, value));

        // Remove old data points if the series gets too large
        if (series.getData().size() > 100) {
            series.getData().remove(0);
            xAxis.setLowerBound(currentTime - 100 * updateInterval);
            xAxis.setUpperBound(currentTime);
        }

        currentTime += deltaTime;
    }
}