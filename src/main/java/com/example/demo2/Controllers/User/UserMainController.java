package com.example.demo2.Controllers.User;

import com.example.demo2.Controllers.Admin.AdminController;
import com.example.demo2.Stock;
import com.example.demo2.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.example.demo2.StockExchangeManager.updateUserCSV;

import static com.example.demo2.StockExchangeManager.stockList;


public class UserMainController {
    private static User loggedInUser;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField AccountBalance;
    @FXML
    private Label Balance;
    @FXML
    private Label numStocks;
    @FXML
    private Label userName;
    @FXML
    private Label smallamount;
    @FXML
    private Label Remaining;
    @FXML
    private TableView<Stock> stockTableView;

    public void switchToExploreStocks(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminController.checkSession(currentStage);
        if (AdminController.isStartSession()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/ViewStocks.fxml"));

            root = loader.load();
            ViewStocks viewStocks = loader.getController();
            viewStocks.initData(loggedInUser);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }
    }


    public void switchToRequestsPage(ActionEvent event) throws IOException {
    }

    public void switchToTrackStocks(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminController.checkSession(currentStage);
        if (AdminController.isStartSession()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/TrackStocks.fxml"));
            root = loader.load();
            TrackStocksController controller = loader.getController();
            controller.initData(loggedInUser);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }
    }

    public void initData(User user) {
        loggedInUser = user;
        userName.setText("Hello, " + loggedInUser.getUsername());
        numStocks.setText(String.valueOf(loggedInUser.getNumOfStocks()));
        Balance.setText(String.valueOf(loggedInUser.getAccountBalance()));
    }

    public void BackToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }


    @FXML
    private void handleSubscribeButton(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminController.checkSession(currentStage);
        if (AdminController.isStartSession()) {
            if (loggedInUser.isPremium()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("You are already subscribed to premium");
                alert.showAndWait();
            } else {
                // Create the confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Subscription Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Do you agree to pay a 100 EGP fee to subscribe?");

                // Show the dialog and wait for the user's response
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User accepted the fee
                    boolean success = subscribeUser();
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "You have successfully subscribed to premium and notifications on stock price changes !", null);

                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Insufficient balance.", null);
                    }
                } else {
                    // User declined the fee
                    showAlert(Alert.AlertType.INFORMATION, "Subscription Cancelled", "You have cancelled the subscription.", null);
                }
            }
        }
    }
    private static void showAlert(Alert.AlertType alertType, String title, String message, String headerText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean subscribeUser() {
        if (loggedInUser.getAccountBalance() >= 100) {
            loggedInUser.setAccountBalance(loggedInUser.getAccountBalance() - 100);
            loggedInUser.setPremium(true);
            updateUserCSV();
            return true;
        } else {
            return false;
        }
    }

    public void Notifications(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/Notifications.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

    }

    public void LineCharts(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/ViewCharts.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void switchToMarketPerformance(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminController.checkSession(currentStage);
        if (AdminController.isStartSession()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/MarketPerformanceCharts.fxml"));
            Parent root = loader.load();

            MarketPerformanceController controller = loader.getController();
//        controller.setStock(selectedStock);
            controller.initData(loggedInUser);
            controller.initialize(stockList);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void switchToDepositWithdraw(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminController.checkSession(currentStage);
        if (AdminController.isStartSession()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/StandardUser/DepositOrWithdraw.fxml"));
            root = loader.load();
            DepositOrWithdawController DepositOrWithdraw = loader.getController();
            DepositOrWithdraw.initData(loggedInUser);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }
    }
    public void switchToTransactionHistory(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AdminController.checkSession(currentStage);
        if (AdminController.isStartSession()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/StandardUser/TransactionHistory.fxml"));
            root = loader.load();
            TransactionHistoryController transactionHistoryController = loader.getController();
            transactionHistoryController.initData(loggedInUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void viewCharts(ActionEvent event) throws IOException {
        Stock selectedStock = stockTableView.getSelectionModel().getSelectedItem();
        if (selectedStock != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/MarketPerformance.fxml"));
            root = loader.load();
            MarketPerformanceController marketPerformanceController = loader.getController();
            marketPerformanceController.initData(loggedInUser);
            marketPerformanceController.setStock(selectedStock);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }
    }
}