package com.example.demo2.Controllers.User;

import com.example.demo2.StockExchangeManager;
import com.example.demo2.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;


public class TransactionHistoryController {
    @FXML
    private TableView<Transaction> transactionHistoryTable;
    @FXML
    private TableColumn<Transaction, String> OperationColumn;
    @FXML
    private TableColumn<Transaction, Double> AmountColumn;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private User loggedInUser;

    public void initData(User user) {
        loggedInUser = user;
        displayTransactionHistory();
    }

    private void displayTransactionHistory() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        for (Pair<String, Double> transaction : loggedInUser.getTransactionHistory()) {
            transactions.add(new Transaction(transaction.getKey(), transaction.getValue()));
        }
        transactionHistoryTable.setItems(transactions);
    }

    @FXML
    public void initialize() {
        OperationColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        AmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }
    public void handleBackFromTransaction(ActionEvent event) throws IOException {
        User user = loggedInUser;
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/StandardUser/UserMain.fxml"));
        root=loader.load();
        UserMainController userMainController= loader.getController();
        userMainController.initData(user);
        StockExchangeManager.saveTransactionHistoryToCSV(user);


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public static class Transaction {
        private final String type;
        private final Double amount;

        public Transaction(String type, Double amount) {
            this.type = type;
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public Double getAmount() {
            return amount;
        }
    }
}

