package com.example.demo2;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stock {
//    private String label;
//    private double initialPrice;
//    private double currentPrice;
//    private int availableStocks;
//    private double profit;
    private ArrayList<Double> priceHistory;
    public static List<String> labels = new ArrayList<>();
    public static List<Double> initialPrices = new ArrayList<>();
    static List<Double> currentPrices = new ArrayList<>();
    public static List<Integer> availableStock = new ArrayList<>();
    public static List<Double> profits =new ArrayList<>();
    private String labelss;
    private DoubleProperty initialPricess;
    private DoubleProperty currentPrice;
    private IntegerProperty availableStocks;
    private DoubleProperty profit;

    public Stock(String labelss, double initialPricess, int availableStocks, double profit) {
        this.labelss = labelss;
        this.initialPricess = new SimpleDoubleProperty(initialPricess);
        this.currentPrice = new SimpleDoubleProperty(initialPricess); // Assuming initialPrice is also the currentPrice initially
        this.availableStocks = new SimpleIntegerProperty(availableStocks);
        this.profit = new SimpleDoubleProperty(profit); // Assuming profit starts at 0 initially
    }
    // Getter methods for JavaFX properties
    public String getLabel() {
        return labelss;
    }

    public DoubleProperty initialPriceProperty() {
        return initialPricess;
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

        public static void readCSV(){

            String csvFile = "stocks.csv";
            String line;
            String cvsSplitBy = ",";

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy);
                    labels.add(data[0]);
                    initialPrices.add(Double.parseDouble(data[1]));
                    currentPrices.add(Double.parseDouble(data[2]));
                    availableStock.add(Integer.parseInt(data[3]));
                    profits.add(Double.parseDouble(data[4]));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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