package com.example.demo2.Controllers.User;

import com.example.demo2.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TrackStocksController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<StockData> stockTableView;  // Use StockData instead of Object

    @FXML
    private TableColumn<StockData, String> label;

    @FXML
    private TableColumn<StockData, Double> purchasePrice;

    @FXML
    private TableColumn<StockData, Double> currentPrice;

    @FXML
    private Button sellStockButton;

    private User loggedInUser;
    private final StockExchangeManager stockExchangeManager = App.manager;
    private StockData selectedstock;

    public void initialize() {
        label.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLabel()));
        purchasePrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPurchasePrice()).asObject());
        currentPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentPrice()).asObject());
        loadUserBoughtStocks();
    }
    private void setupTableViewListener() {
        stockTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectedstock = newValue);
    }
    private void loadUserBoughtStocks() {
        if (loggedInUser != null) {
            List<Pair<Stock, List<Double>>> userBoughtStocks = StockExchangeManager.getUserBoughtStocks(loggedInUser);
            ObservableList<StockData> stockDataList = FXCollections.observableArrayList();

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

    public void initData(User user){
        loggedInUser=user;
        loadUserBoughtStocks();
    }

    @FXML
    private void handleSellStock() {
        StockData selectedStock = stockTableView.getSelectionModel().getSelectedItem();
        User user = loggedInUser;
        if(selectedStock !=null){
            Stock stock = stockExchangeManager.getStockFromLabel(selectedStock.label);
            assert stock != null;
            stockExchangeManager.createTransactionRequest(user, RequestType.SELL_STOCK, stock, stock.getActualCurrentPrice());
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

    public void backtoUserMain(ActionEvent event) throws IOException {
        User user = loggedInUser;
        System.out.println(user.toString());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/UserMain.fxml"));
        root = loader.load();
        UserMainController userMainController = loader.getController();
        userMainController.initData(user);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void handleExportButton(){
        User user = loggedInUser;
        StockExchangeManager.exportStockTransactionHistoryToCSV(user);

    }
}
