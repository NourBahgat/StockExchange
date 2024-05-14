package com.example.demo2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Stock {
    private String label;
    private double initialPrice;
    private double currentPrice;
    private int availableStocks;
    private double profit;
    private ArrayList<Double> priceHistory;

    public Stock(String label, double initialPrice, int availableStocks) {
        this.label = label;
        this.initialPrice = initialPrice;
        this.currentPrice = initialPrice;
        this.availableStocks = availableStocks;
        this.profit = 0.0;
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(initialPrice);
    }

    public String getLabel() {
        return label;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public int getAvailableStocks() {
        return availableStocks;
    }

    public double getProfit() {
        return profit;
    }

    public ArrayList<Double> getPriceHistory() {
        return priceHistory;
    }

    public void updatePrice(double newPrice) {
        priceHistory.add(newPrice);
        this.currentPrice = newPrice;
    }

    public void exportStockDataToCSV(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Label,InitialPrice,CurrentPrice,AvailableStocks,Profit\n");
            writer.write(label + "," + initialPrice + "," + currentPrice + "," + availableStocks + "," + profit + "\n");
        }
    }

    public void exportPriceHistoryToCSV(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Price\n");
            for (double price : priceHistory) {
                writer.write(price + "\n");
            }
        }
    }

    public boolean isBought() {
        return availableStocks < 0;
    }

    public void setLabel(String label) {
    }

    public void setInitialPrice(double initialPrice) {
    }

    public void setCurrentPrice(double currentPrice) {
    }

    public void setAvailableStocks(int availableStocks) {
    }

    public void setProfit(double profit) {
    }

    public class Transaction extends Stock{

        public Transaction(String label, double initialPrice, int availableStocks) {
            super(label, initialPrice, availableStocks);
        }
    }
}