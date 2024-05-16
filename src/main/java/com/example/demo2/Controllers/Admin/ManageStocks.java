package com.example.demo2.Controllers.Admin;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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
    private Stage stage;
    private Scene scene;
    private Parent root;

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
        loadStocksData();
    }
    private void loadStocksData() {
        StockExchangeManager.saveCSV(); // Ensure stockList is populated
        stocksData.addAll(StockExchangeManager.stockList);
        stockTableView.setItems(stocksData);
    }
    public void BackToAdminMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/AdminMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}
