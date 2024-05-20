package com.example.demo2;

import javafx.beans.property.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock {
    private StringProperty label;
    private DoubleProperty initialPrice;
    private DoubleProperty currentPrice;
    private IntegerProperty availableStocks;
    private DoubleProperty profit;
    private HashMap<User, List<Double>> buyers;
    private HashMap<User, List<Double>> sellers;
    private List<TimeStamp> priceHistory;
    public Stock(String label, double initialPricess, double
                 currentPrice, int availableStocks, double profit) {
        this.label =new SimpleStringProperty(label);
        this.initialPrice= new SimpleDoubleProperty(initialPricess);
        this.currentPrice = new SimpleDoubleProperty(currentPrice);
        this.availableStocks = new SimpleIntegerProperty(availableStocks);
        this.profit = new SimpleDoubleProperty(profit);
        this.buyers = new HashMap<>();
        this.sellers=new HashMap<>();
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(new TimeStamp(currentPrice, LocalDateTime.now()));
    }


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
    public void setInitialPrice(double initialPrice) {
        this.initialPrice.setValue(initialPrice);
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

    public void removeBuyer(User user) {
        if (buyers.containsKey(user)) {
            List<Double> purchasePrices = buyers.get(user);
            availableStocks.add(purchasePrices.size());
            buyers.remove(user);
        }
    }
    public void setPriceHistory(List<TimeStamp> priceHistory) {
        this.priceHistory = priceHistory;
    }
    public List<TimeStamp> getPriceHistory() {
        return priceHistory;
    }
    public void addPriceHistory(TimeStamp timeStamp) {
        priceHistory.add(timeStamp);
    }

    public void updatePrice(double newPrice) {
        this.currentPrice.set(newPrice);
        this.priceHistory.add(new TimeStamp(newPrice, LocalDateTime.now()));
    }

    }