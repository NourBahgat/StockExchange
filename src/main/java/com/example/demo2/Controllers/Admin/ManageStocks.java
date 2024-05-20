package com.example.demo2.Controllers.Admin;

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
import static com.example.demo2.StockExchangeManager.updateStockCSV;

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
    @FXML
    private TextField labelField;
    @FXML
    private TextField initialPriceField;
    @FXML
    private TextField availableStocksField;
    @FXML
    private TextField profitField;
    @FXML
    private TextField currentPriceField;
    private boolean isPopulated= false;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<Stock> stocksData = FXCollections.observableArrayList();

    public void initialize() {
        if (!isPopulated) {
            labelColumn.setCellValueFactory(cellData -> cellData.getValue().getLabel());
            initialPriceColumn.setCellValueFactory(cellData -> cellData.getValue().initialPriceProperty().asObject());
            currentPriceColumn.setCellValueFactory(cellData -> cellData.getValue().currentPriceProperty().asObject());
            availableStocksColumn.setCellValueFactory(cellData -> cellData.getValue().availableStocksProperty().asObject());
            profitsColumn.setCellValueFactory(cellData -> cellData.getValue().profitProperty().asObject());
            loadStocksData();
            isPopulated=true;
        }

    }

    private void loadStocksData() {
        stocksData.addAll(stockList);
        stockTableView.setItems(stocksData);
    }
    public void BackToAdminMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/AdminMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    @FXML
    public void handleDeleteButton(){
        Stock selectedStock = stockTableView.getSelectionModel().getSelectedItem();
        if (selectedStock == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Stock Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a stock to delete.");
            alert.showAndWait();
        } else {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete the selected stock?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Admin admin = new Admin();
                admin.removeStock(selectedStock);
                stocksData.remove(selectedStock);
                stockList.remove(selectedStock);
                updateStockCSV();
            }
        }
    }
    public void addStock(ActionEvent event) {
        String label = labelField.getText();
        double initialPrice = Double.parseDouble(initialPriceField.getText());
        int availableStocks = Integer.parseInt(availableStocksField.getText());
        double profit = Double.parseDouble(profitField.getText());

        Stock newStock = new Stock(label, initialPrice, initialPrice, availableStocks, profit);

        stockList.add(newStock);
        stocksData.add(newStock);
        updateStockCSV();

        labelField.clear();
        initialPriceField.clear();
        availableStocksField.clear();
        profitField.clear();
    }
    @FXML
    public void handleUpdatePriceButton() {
        Stock selectedStock = stockTableView.getSelectionModel().getSelectedItem();
        StockExchangeManager.saveCostTrackerList("costTracker.csv");
        if (selectedStock == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Stock Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a stock to update.");
            alert.showAndWait();
            return;
        }

        try {
            double newPrice = Double.parseDouble(currentPriceField.getText());
            selectedStock.updatePrice(newPrice);
            stockTableView.refresh();
            updateStockCSV();
            currentPriceField.clear();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid number for the price.");
            alert.showAndWait();
        }
    }
}
