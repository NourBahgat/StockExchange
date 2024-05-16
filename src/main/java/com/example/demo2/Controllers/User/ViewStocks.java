package com.example.demo2.Controllers.User;

import com.example.demo2.Stock;
import com.example.demo2.StockExchangeManager;
import com.example.demo2.User;
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
    private TextField labelField;
    @FXML
    private TextField profitField;
    @FXML
    private TextField labelTextField;
    @FXML
    private TextField initialpriceTextField;
    @FXML
    private TextField currentpriceTextField;
    private StockExchangeManager stockExchangeManager;
    private Stock selectedstock;

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

    private void loadStocksData() {
        String csvFile = "stocks.csv"; // Adjust file path as necessary
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                Stock stock = new Stock(data[0], Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]), Integer.parseInt(data[3]),
                        Double.parseDouble(data[4]));
                stocksData.add(stock);
            }
            stockTableView.setItems(stocksData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BackToUserMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/UserMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

    }
}

