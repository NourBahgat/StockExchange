package com.example.demo2.Controllers.User;

import com.example.demo2.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.io.IOException;

import static org.apache.commons.lang3.ObjectUtils.NULL;

public class DepositOrWithdawController {
    @FXML
    private TextField withdrawTextField;
    private final StockExchangeManager stockExchangeManager = App.manager;

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
}
