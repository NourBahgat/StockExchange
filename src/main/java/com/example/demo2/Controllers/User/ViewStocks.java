package com.example.demo2.Controllers.User;

import com.example.demo2.Admin;
import com.example.demo2.Stock;
import com.example.demo2.StockExchangeManager;
import javafx.beans.binding.Bindings;
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

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.example.demo2.StockExchangeManager.stockList;
public class ViewStocks {

    @FXML
    private TableView<Stock> stockTableView;
    @FXML
    private TableColumn<Stock, String> labelColumn;
    @FXML
    private TextField labelField;
    @FXML
    private TextField profitField;
    private boolean isPopulated= false;


    private ObservableList<Stock> stocksData = FXCollections.observableArrayList();

    public void initialize() {
        if (!isPopulated) {
            labelColumn.setCellValueFactory(cellData -> cellData.getValue().getLabel());
            loadStocksData();
            isPopulated=true;
        }
    }

    private void loadStocksData() {
        StockExchangeManager.saveFromCSVtoList();// Ensure stockList is populated
        stocksData.addAll(stockList);
        stockTableView.setItems(stocksData);
    }
}
