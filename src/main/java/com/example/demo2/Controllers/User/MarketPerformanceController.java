package com.example.demo2.Controllers.User;


import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import com.example.demo2.Stock;

import java.util.ArrayList;

public class MarketPerformanceController {

    public static void showStockPriceChart(Stage stage, Stock stock) {
        stage.setTitle("Stock Price Chart");

        // Defining the x and y axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Price");

        // Creating the line chart
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Price Monitoring, " + stock.getActualLabel());

        // Defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Stock Price");

        // Populating the series with data
        ArrayList<Double> priceHistory = stock.getPriceHistory();
        for (int i = 0; i < priceHistory.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, priceHistory.get(i)));
        }

        // Adding the series to the chart
        lineChart.getData().add(series);

        // Creating the scene and showing the stage
        Scene scene = new Scene(lineChart, 300, 300);
        stage.setScene(scene);
        stage.show();
    }
}

