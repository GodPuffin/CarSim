package com.project.carsim;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Grapher {
    private XYChart.Series<Number, Number> series;
    private NumberAxis xAxis;
    private double currentTime = 0.0;
    private final double updateInterval = 1.0 / 60.0;

    public Grapher(XYChart<Number, Number> chart, String color) {
        series = new XYChart.Series<>();
        chart.getData().add(series);
        xAxis = (NumberAxis) chart.getXAxis();
        series.getNode().setStyle("-fx-stroke: " + color + ";");
    }

    public void update(double value, double deltaTime) {
        series.getData().add(new XYChart.Data<>(currentTime, value > 0.01 ? value : 0));
        //TODO: Ternary can be removed once graphing bounds are decided
        series.getData().get(series.getData().size() - 1).getNode().setVisible(false);

        // Remove old data points if the series gets too large
        if (series.getData().size() > 100) {
            series.getData().remove(0);
            xAxis.setLowerBound(currentTime - 100 * updateInterval);
            xAxis.setUpperBound(currentTime);
        }

        currentTime += deltaTime;
    }
}