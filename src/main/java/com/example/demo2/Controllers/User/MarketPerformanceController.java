package com.example.demo2.Controllers.User;

import com.example.demo2.TimeStamp;
import com.example.demo2.Stock;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MarketPerformanceController {

    @FXML
    private LineChart<Number, Number> stockChart;

    public void initialize(List<Stock> stockList) {
//        stockChart.getData().clear();

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        yAxis.setLabel("Price");

        for (Stock stock : stockList) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(stock.getActualLabel());

            List<TimeStamp> priceHistory = stock.getPriceHistory();

            if (priceHistory.isEmpty()) continue;

            LocalDateTime startTime = priceHistory.get(0).getTimestamp();
            for (TimeStamp update : priceHistory) {
                long timeDifference = ChronoUnit.HOURS.between(startTime, update.getTimestamp());
                series.getData().add(new XYChart.Data<>(timeDifference, update.getPrice()));
            }
            stockChart.getData().add(series);
        }


        stockChart.setTitle("Stock Performance");
        stockChart.setCreateSymbols(false);
        stockChart.setLegendVisible(true);
        stockChart.setAnimated(true);
    }

    public void setStock(Stock stock) {
//        stockChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(stock.getActualLabel());

        List<TimeStamp> priceHistory = stock.getPriceHistory();

        if (!priceHistory.isEmpty()) {
            LocalDateTime startTime = priceHistory.get(0).getTimestamp();

            for (TimeStamp update : priceHistory) {
                long timeDifference = ChronoUnit.HOURS.between(startTime, update.getTimestamp());
                series.getData().add(new XYChart.Data<>(timeDifference, update.getPrice()));
            }
        }

        stockChart.getData().add(series);
    }
}
