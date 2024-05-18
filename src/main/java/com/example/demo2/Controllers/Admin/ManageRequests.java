package com.example.demo2.Controllers.Admin;

import com.example.demo2.StockExchangeManager;
import com.example.demo2.TransactionRequest;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ManageRequests {

    @FXML
    private TableView<TransactionRequest> requestsTableView;
    @FXML
    private TableColumn<TransactionRequest, String> usernameColumn;
    @FXML
    private TableColumn<TransactionRequest, String> operationColumn;
    @FXML
    private TableColumn<TransactionRequest, String> stockLabel;
    @FXML
    private TableColumn<TransactionRequest, Double> amountColumn;

    private Stage stage;
    private Scene scene;
    private Parent root;


        public void initialize() {
            usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
            operationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActionStr()));
            stockLabel.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLabel()));
            amountColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAmount()).asObject());
            loadRequests();
        }

        private void loadRequests() {
            ObservableList<TransactionRequest> requests = FXCollections.observableArrayList(StockExchangeManager.getTransactionRequests());
            requestsTableView.setItems(requests);
        }

    @FXML
    private void handleApprove() {
        TransactionRequest selectedRequest = requestsTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            StockExchangeManager.approveTransaction(selectedRequest);
            loadRequests();
            showAlert(AlertType.INFORMATION, "Transaction Approved", "The transaction has been approved successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No transaction request selected. Please select a request to approve.");
        }
    }

    @FXML
    private void handleDisapprove() {
        TransactionRequest selectedRequest = requestsTableView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            StockExchangeManager.disapproveTransaction(selectedRequest);
            loadRequests();
            showAlert(Alert.AlertType.INFORMATION, "Transaction Disapproved", "The transaction has been disapproved.");
        } else {
            showAlert(AlertType.WARNING, "No Selection", "No transaction request selected. Please select a request to disapprove.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void BackToAdminMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/AdminMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}