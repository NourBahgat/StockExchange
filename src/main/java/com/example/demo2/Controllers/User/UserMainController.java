package com.example.demo2.Controllers.User;

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

import static com.example.demo2.Controllers.Admin.ManageUsers.showAlert;
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

    public void switchToViewStocks(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/ViewStocks.fxml"));
        root = loader.load();
        ViewStocks viewStocks = loader.getController();
        viewStocks.initData(loggedInUser);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void switchToRequestsPage(ActionEvent event) throws IOException {
    }

    public void switchToTrackStocks(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/TrackStocks.fxml"));
        root = loader.load();
        TrackStocksController controller = loader.getController();
        controller.initData(loggedInUser);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
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

    public void GoToSubscribe(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/Subscription.fxml")));
        stage.setScene(scene);
    }

    public void switchToPremiumPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/PremiumUser/PremiumPage.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void handleSubscribeButton(ActionEvent event) {
        if (loggedInUser.isPremium()) {
            // Directly redirect to the Premium page
            try {
                redirectToAnotherPage(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
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
                showAlert(Alert.AlertType.INFORMATION, "Success", "You have successfully subscribed!", null);

                // Redirect to another FXML page
                try {
                    redirectToAnotherPage(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Insufficient balance.", null);
            }
        } else {
            // User declined the fee
            showAlert(Alert.AlertType.INFORMATION, "Subscription Cancelled", "You have cancelled the subscription.", null);
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
        // Deduct 100 EGP from the user's account balance
        if (loggedInUser.getAccountBalance() >= 100) {
            loggedInUser.setAccountBalance(loggedInUser.getAccountBalance() - 100);
            loggedInUser.setPremium(true); // Set the user as premium
            updateUserCSV(); // Update the user's balance and premium status in the CSV file
            return true;
        } else {
            return false;
        }
    }

    private void redirectToAnotherPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PremiumUser/PremiumPage.fxml"));
        root = loader.load();
        // Get the current stage
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        PremiumController premiumController = loader.getController();
        premiumController.initData(loggedInUser);
        // Set the scene for the stage
        stage.setScene(scene);
        stage.show();
    }

    //    public void Subscripe(ActionEvent event) throws IOException {
//        if (depositedMoney < Subscriptionprice) {
//            smallamount.setText("Insufficient Amount");
//        }
//        else{
//
//            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/SubscriptionMain.fxml")));
//            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            scene = new Scene(root);
//            stage.setScene(scene);
//            remainingBalance=depositedMoney-Subscriptionprice ;
//            Remaining.setText("Remaining Balance ="+remainingBalance+"EGP");
//        }

    public void Notifications(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/Notifications.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

    }

    //    public void deposit(ActionEvent event) throws IOException {
//        depositedMoney =Double.parseDouble( MoneyTextField.getText());
//    }
//    public void Subscripe(ActionEvent event) throws IOException {
//        if (depositedMoney < Subscriptionprice) {
//            smallamount.setText("Insufficient Amount");
//        } else {
//
//        }
    public void LineCharts(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/Charts.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void switchToMarketPerformance(ActionEvent event) throws IOException {
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

    public void switchToDepositWithdraw(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/StandardUser/DepositOrWithdraw.fxml"));
        root = loader.load();
        DepositOrWithdawController DepositOrWithdraw = loader.getController();
        DepositOrWithdraw.initData(loggedInUser);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void switchToTransactionHistory(ActionEvent event) throws IOException {
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

