package com.example.demo2.Controllers.User;

import com.example.demo2.Stock;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import com.example.demo2.Stock;

import java.util.List;

public class MarketPerformanceController {

    @FXML
    private LineChart<Number, Number> stockChart;

    public void initialize(List<Stock> stockList) {
        // Clear existing data from the line chart
        stockChart.getData().clear();

        // Set up axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Price");

        // Add series for each stock
        for (Stock stock : stockList) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(stock.getActualLabel());

            // Retrieve price history for the stock
            List<Double> priceHistory = stock.getPriceHistory();

            // Add data points to the series
            for (int i = 0; i < priceHistory.size(); i++) {
                series.getData().add(new XYChart.Data<>(i, priceHistory.get(i)));
            }

            // Add the series to the line chart
            stockChart.getData().add(series);
        }

        // Set up the line chart
        stockChart.setTitle("Stock Performance");
        stockChart.setCreateSymbols(false);
        stockChart.setLegendVisible(true);
        stockChart.setAnimated(true);
    }
}
