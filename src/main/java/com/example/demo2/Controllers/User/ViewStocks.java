package com.example.demo2.Controllers.User;

import com.example.demo2.*;
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

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.demo2.Admin.searchUserInCSV;
import static com.example.demo2.StockExchangeManager.*;

public class ViewStocks {

    @FXML
    private TableView<Stock> stockTableView;
    @FXML
    private TableColumn<Stock, String> labelColumn;
    @FXML
    private TextField labelTextField;
    @FXML
    private TextField initialpriceTextField;
    @FXML
    private TextField currentpriceTextField;
    private final StockExchangeManager stockExchangeManager = App.manager;
    private Stock selectedstock;
    private User loggedInUser;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<Stock> stocksData = FXCollections.observableArrayList();

    public void initialize() {
        labelColumn.setCellValueFactory(cellData -> cellData.getValue().getLabel());
        loadStocksData();
        setupTableViewListener();
    }
    private void setupTableViewListener() {
        stockTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectedstock = newValue);
    }

    @FXML
    private void handleViewPriceHistoryButtonClick(ActionEvent event) {
        if (selectedstock != null) {
            labelTextField.textProperty().bind(selectedstock.getLabel());
            initialpriceTextField.textProperty().bind(selectedstock.initialPriceProperty().asString());
           currentpriceTextField.textProperty().bind(selectedstock.currentPriceProperty().asString());
        }
    }

    public void initData(User user){
        loggedInUser=user;
    }

    private void loadStocksData() {
        stocksData.addAll(stockList);
        stockTableView.setItems(stocksData);
    }

    public void BackToUserMain (ActionEvent event) throws IOException {
        User user = loggedInUser;
        System.out.println(user.toString());
        //root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/UserMain.fxml")));
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/StandardUser/UserMain.fxml"));
        root=loader.load();
        UserMainController userMainController= loader.getController();
        userMainController.initData(user);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void BuyStock (ActionEvent event) throws IOException {
        User user = loggedInUser;
        if(selectedstock !=null){
            stockExchangeManager.createTransactionRequest(user, RequestType.BUY_STOCK, selectedstock, selectedstock.getActualCurrentPrice());
        }
    }
}

