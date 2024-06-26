package com.example.demo2.Controllers.User;

import com.example.demo2.TimeStamp;
import com.example.demo2.Stock;
import com.example.demo2.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import javax.swing.text.View;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MarketPerformanceController {
    private User loggedInUser;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private LineChart<Number, Number> stockChart;

    public void initialize(List<Stock> stockList) {
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

    public void initData(User user){
        loggedInUser = user;
    }

    public void setStock(Stock stock) {
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

    public void backtoStocks(ActionEvent event) throws IOException {
        User user = loggedInUser;
        System.out.println(user.toString());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/UserMain.fxml"));
        root = loader.load();
        UserMainController mainController = loader.getController();
        mainController.initData(user);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}
