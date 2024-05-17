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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.demo2.StockExchangeManager.*;


public class TrackStocksController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<Object> stockTableView;

    @FXML
    private TableColumn<StockData, String> label;

    @FXML
    private TableColumn<StockData, Double> purchasePrice;

    @FXML
    private TableColumn<StockData, Double> currentPrice;

    @FXML
    private Button sellStockButton;

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

    @FXML
    private void handleSellStock() {
        StockData selectedStockData = (StockData) stockTableView.getSelectionModel().getSelectedItem();
        if (selectedStockData != null) {
            sendSellRequest(selectedStockData);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a stock to sell.");
            alert.showAndWait();
        }
    }

    private void sendSellRequest(StockData stockData) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sell Stock Request");
        alert.setHeaderText("Sell Request for Stock: " + stockData.getLabel());
        alert.setContentText("Do you want to approve or disapprove this request?");

        ButtonType approveButton = new ButtonType("Approve");
        ButtonType disapproveButton = new ButtonType("Disapprove");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(approveButton, disapproveButton, cancelButton);
        alert.showAndWait().ifPresent(response -> {
            if (response == approveButton) {
                User currentUser = UserController.loggedInUser;
                StockExchangeManager.addUserRequest(currentUser, "Sell request for stock: " + stockData.getLabel());
//                addUserRequest(currentUser, "Sell request for stock: " + stockData.getLabel());

                // Remove stock from table
                stockTableView.getItems().remove(stockData);

                currentUser.setNumOfStocks(currentUser.getNumOfStocks() - 1);
                // Update boughtStocks.csv
                StockExchangeManager.removeStockFromUser(currentUser, stockData.getLabel(), stockData.getPurchasePrice());

                // Update boughtStocks.csv
                StockExchangeManager.updateUserCSV();
                // Update bought stocks CSV
                StockExchangeManager.updateBoughtStocksCSV();


                // Send a pop-up message to the admin here
                Alert adminAlert = new Alert(Alert.AlertType.INFORMATION);
                adminAlert.setTitle("Sell Request approved");
                adminAlert.setHeaderText(null);
                adminAlert.setContentText("Sell request for stock: " + stockData.getLabel() + " approved ");
                adminAlert.showAndWait();
            } else if (response == disapproveButton) {
                Alert disapproveAlert = new Alert(Alert.AlertType.INFORMATION);
                disapproveAlert.setTitle("Request Disapproved");
                disapproveAlert.setHeaderText(null);
                disapproveAlert.setContentText("Sell request for stock: " + stockData.getLabel() + " disapproved.");
                disapproveAlert.showAndWait();
            }
        });
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

      public void backtoUserMain (ActionEvent event) throws IOException {
        User user = UserController.loggedInUser;
        System.out.println(user.toString());
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/UserMain.fxml")));
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/StandardUser/UserMain.fxml"));
        root=loader.load();
        UserMainController userMainController= loader.getController();
        userMainController.initData(user);
          // Update boughtStocks.csv
          StockExchangeManager.updateUserCSV();
          // Update bought stocks CSV
          StockExchangeManager.updateBoughtStocksCSV();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}

