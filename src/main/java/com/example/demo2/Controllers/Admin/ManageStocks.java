package com.example.demo2.Controllers.Admin;

import com.example.demo2.Stock;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ManageStocks {

    @FXML
    private TableView<Stock> stockTableView;
    @FXML
    private TableColumn<Stock, String> labelColumn;
    @FXML
    private TableColumn<Stock, Double> initialPriceColumn;
    @FXML
    private TableColumn<Stock, Double> currentPriceColumn;
    @FXML
    private TableColumn<Stock, Integer> availableStocksColumn;
    @FXML
    private TableColumn<Stock, Double> profitsColumn;

    private ObservableList<Stock> stocksData = FXCollections.observableArrayList();

    public void initialize() {
        // Set cell value factories to retrieve data from Stock objects
//        labelColumn.setCellValueFactory(cellData -> cellData.getValue().getLabel());
        labelColumn.setCellValueFactory(cellData -> {
            String label = cellData.getValue().getLabel();
            return Bindings.createStringBinding(() -> label);
        });
        initialPriceColumn.setCellValueFactory(cellData -> cellData.getValue().initialPriceProperty().asObject());
        currentPriceColumn.setCellValueFactory(cellData -> cellData.getValue().currentPriceProperty().asObject());
        availableStocksColumn.setCellValueFactory(cellData -> cellData.getValue().availableStocksProperty().asObject());
        profitsColumn.setCellValueFactory(cellData -> cellData.getValue().profitProperty().asObject());


        stockTableView.setItems(stocksData);
        Stock.readCSV();

        stocksData.addAll(Stock.labels.stream().map(label -> new Stock(label, Stock.initialPrices.get(Stock.labels.indexOf(label)), Stock.currentPrices.get(Stock.labels.indexOf(label)), Stock.availableStock.get(Stock.labels.indexOf(label)), Stock.profits.get(Stock.labels.indexOf(label)))).toList());


    }

}
