package com.example.demo2.Controllers.User;

import com.example.demo2.Stock;
import com.example.demo2.User;
import com.example.demo2.StockExchangeManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class TrackStocksController {

    @FXML
    private TableView<Object> stockTableView;

    @FXML
    private TableColumn<StockData, String> label;

    @FXML
    private TableColumn<StockData, Double> purchasePrice;

    @FXML
    private TableColumn<StockData, Double> currentPrice;

    private User currentUser;

    public void initialize() {
        label.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLabel()));
        purchasePrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPurchasePrice()).asObject());
        currentPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentPrice()).asObject());
        loadUserBoughtStocks();
    }

    private void loadUserBoughtStocks() {
        currentUser = UserController.loggedInUser;
        if (currentUser != null) {
            List<Pair<Stock, List<Double>>> userBoughtStocks = StockExchangeManager.getUserBoughtStocks(currentUser);
            ObservableList<Object> stockDataList = FXCollections.observableArrayList();
            for (Pair<Stock, List<Double>> stockCosts : userBoughtStocks) {
                Stock stock = stockCosts.getKey();
                List<Double> costs = stockCosts.getValue();
                for (double cost : costs) {
                    stockDataList.add(new StockData(stock.getActualLabel(), cost, stock.getActualCurrentPrice()));
                }
            }
            stockTableView.setItems(stockDataList);
        }
    }
    public void sellStock (ActionEvent event) throws IOException {
        User user= UserController.loggedInUser;
        Object selectedstock=stockTableView.getSelectionModel().getSelectedItem();
        if(selectedstock !=null){
            //toht khalas
//            StockExchangeManager.SellStock(user, selectedstock);
        }
    }


    public static class StockData {
        private final String label;
        private final double purchasePrice;
        private final double currentPrice;

        public StockData(String label, double purchasePrice, double currentPrice) {
            this.label = label;
            this.purchasePrice = purchasePrice;
            this.currentPrice = currentPrice;
        }

        public String getLabel() {
            return label;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public double getCurrentPrice() {
            return currentPrice;
        }
    }
}

