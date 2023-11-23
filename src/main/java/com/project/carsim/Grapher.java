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
        series = new XYChart.Series<>();
        chart.getData().add(series);
        xAxis = (NumberAxis) chart.getXAxis();
        yAxis = (NumberAxis) chart.getYAxis();
        yAxis.setLowerBound(min);
        yAxis.setUpperBound(max);
        series.getNode().setStyle("-fx-stroke: " + color + ";");
    }

    public void update(double value, double deltaTime) {
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