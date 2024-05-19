package com.example.demo2.Controllers.User;

import com.example.demo2.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class DepositOrWithdawController {
    @FXML
    private TextField withdrawTextField;
    @FXML
    private TextField depositTextField;
    private final StockExchangeManager stockExchangeManager = App.manager;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private User loggedInUser;

    @FXML
    public void handleWithdrawButton() {
        User user = loggedInUser;
        String withdrawAmountText = withdrawTextField.getText();
        try {
            double withdrawAmount = Double.parseDouble(withdrawAmountText);
            if (withdrawAmount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Withdraw amount must be greater than zero.");
                return;
            }
            if (withdrawAmount > user.getAccountBalance()) {
                showAlert(Alert.AlertType.ERROR, "Insufficient Balance", "You don't have enough balance for this withdrawal.");
                return;
            }
            stockExchangeManager.createTransactionRequest(user, RequestType.WITHDRAWAL, null, withdrawAmount);
                withdrawTextField.clear();
            } catch(NumberFormatException e){
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric amount.");
            }
        }
    @FXML
    public void handleDepositButton() {
        User user = loggedInUser;
        String depositAmountText = depositTextField.getText();
        try {
            double depositAmount = Double.parseDouble(depositAmountText);
            if (depositAmount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Withdraw amount must be greater than zero.");
                return;
            }
            stockExchangeManager.createTransactionRequest(user, RequestType.DEPOSIT, null, depositAmount);
            depositTextField.clear();
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric amount.");
        }
    }

    public void initData(User user){
        loggedInUser=user;
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
}
