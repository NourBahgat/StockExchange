package com.example.demo2;

import javafx.beans.property.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock {
//    private String label;
//    private double initialPrice;
//    private double currentPrice;
//    private int availableStocks;
//    private double profit;
    private StringProperty label;
    private DoubleProperty initialPrice;
    private DoubleProperty currentPrice;
    private IntegerProperty availableStocks;
    private DoubleProperty profit;
    private HashMap<User, List<Double>> buyers;
    private List<Double> priceHistory;
    public Stock(String label, double initialPricess, double
                 currentPrice, int availableStocks, double profit) {
        this.label =new SimpleStringProperty(label);
        this.initialPrice= new SimpleDoubleProperty(initialPricess);
        this.currentPrice = new SimpleDoubleProperty(currentPrice);
        this.availableStocks = new SimpleIntegerProperty(availableStocks);
        this.profit = new SimpleDoubleProperty(profit);
        this.buyers = new HashMap<>();
    }
    // Getter methods for JavaFX properties
    public HashMap<User, List<Double>> getBuyersList() { return buyers; }

    public StringProperty getLabel() {
        return label;
    }

    public DoubleProperty initialPriceProperty() {
        return initialPrice;
    }

    public DoubleProperty currentPriceProperty() {
        return currentPrice;
    }

    public IntegerProperty availableStocksProperty() {
        return availableStocks;
    }

    public DoubleProperty profitProperty() {
        return profit;
    }
    public String getActualLabel() {
        return label.get();
    }
    public double getActualInitialPrice() {
        return initialPrice.get();
    }

    public double getActualCurrentPrice() {
        return currentPrice.get();
    }

    public int getActualAvailableStocks() {
        return availableStocks.get();
    }

    public double getActualProfit() {
        return profit.get();
    }

    public void setAvailableStocks(int availableStocks) {
        this.availableStocks.set(availableStocks);
    }
    public List<Double> getBuyerOrderCosts(User user) {
        if (buyers.containsKey(user))   return buyers.get(user);
        return null;
    }

    public void addBuyer(User user, Double currentPrice) {
        if (buyers.containsKey(user)) {
            List<Double> buyPrice = buyers.get(user);
            buyPrice.add(currentPrice);
            System.out.println(buyPrice);
        } else {
            List<Double> buyPrice = new ArrayList<Double>();
            buyPrice.add(currentPrice);
            buyers.put(user, buyPrice);
            System.out.println(buyPrice);
        }
    }
    public void removeBuyer(User user, Double purchasePrice) {
        if (buyers.containsKey(user)) {
            List<Double> purchasePrices = buyers.get(user);
            purchasePrices.remove(purchasePrice);
            if (purchasePrices.isEmpty()) {
                buyers.remove(user);
            }
        }
    }
    public List<Double> getPriceHistory() {
        return priceHistory;
    }
    public void updatePrice(double newPrice) {
        this.currentPrice.set(newPrice);
        this.priceHistory.add(newPrice);
    }

    public void setPriceHistory(List<Double> priceHistory) {
        this.priceHistory = priceHistory;
    }
    //    public void setActualAvailableStocks(){this.availableStocks.get()=availableStocks.get();}

//    public static ArrayList<Stock> getStockList() {
//        return stockList;
//    }

//    public Stock(String label, double initialPrice, int availableStocks) {
//        this.label = label;
//        this.initialPrice = initialPrice;
//        this.currentPrice = initialPrice;
//        this.availableStocks = availableStocks;
//        this.profit = 0.0;
//        this.priceHistory = new ArrayList<>();
//        this.priceHistory.add(initialPrice);
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public double getInitialPrice() {
//        return initialPrice;
//    }
//
//    public double getCurrentPrice() {
//        return currentPrice;
//    }
//
//    public int getAvailableStocks() {
//        return availableStocks;
//    }
//
//    public double getProfit() {
//        return profit;
//    }
//
//    public ArrayList<Double> getPriceHistory() {
//        return priceHistory;
//    }
//
//    public void updatePrice(double newPrice) {
//        priceHistory.add(newPrice);
//        this.currentPrice = newPrice;
//    }
//
//    public void exportStockDataToCSV(String filename) throws IOException {
//        try (FileWriter writer = new FileWriter(filename)) {
//            writer.write("Label,InitialPrice,CurrentPrice,AvailableStocks,Profit\n");
//            writer.write(label + "," + initialPrice + "," + currentPrice + "," + availableStocks + "," + profit + "\n");
//        }
//    }
//
//    public void exportPriceHistoryToCSV(String filename) throws IOException {
//        try (FileWriter writer = new FileWriter(filename)) {
//            writer.write("Price\n");
//            for (double price : priceHistory) {
//                writer.write(price + "\n");
//            }
//        }
//    }
//
//    public boolean isBought() {
//        return availableStocks < 0;
//    }
//
//    public void setLabel(String label) {
//    }
//
//    public void setInitialPrice(double initialPrice) {
//    }
//
//    public void setCurrentPrice(double currentPrice) {
//    }
//
//    public void setAvailableStocks(int availableStocks) {
//    }
//
//    public void setProfit(double profit) {
//    }
//
//    public class Transaction extends Stock{
//
//        public Transaction(String label, double initialPrice, int availableStocks) {
//            super(label, initialPrice, availableStocks);
//        }
//    }
    }