package com.example.demo2;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.util.Pair;

import java.util.*;

import static com.example.demo2.Controllers.User.UserController.loggedInUser;

public class User {
    private String username;
    private String password;
    private double accountBalance;
    private boolean isPremium;
    private int numOfStocks;
    private List<Pair<String, Double>> transactionHistory;

    private HashMap<Stock, Double> autoBuyList;
    private HashMap<Stock, Double> costTrackList;
    private List<Pair<Stock, List<Double>>> soldStocks;
    private List<String> stockTransactions;

    public User(String username, String password, double accountBalance, int numOfStocks, boolean isPremium) {
        this.username = username;
        this.password = password;
        this.accountBalance = accountBalance;
        this.numOfStocks = numOfStocks;
        this.isPremium = isPremium;
        this.transactionHistory = new ArrayList<>();
        this.autoBuyList = new HashMap<>();
        this.costTrackList = new HashMap<>();
        this.stockTransactions = new ArrayList<>();
    }

    public User() {
        this.username = username;
        this.password = password;
        this.accountBalance = accountBalance;
        this.numOfStocks = numOfStocks;
        this.isPremium = isPremium;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNumOfStocks(int numOfStocks) {
        this.numOfStocks = numOfStocks;
    }

    public int getNumOfStocks() {
        return numOfStocks;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public boolean isCostTracked(Stock stock) {
        return costTrackList.containsKey(stock);
    }

    public void setCostTracked(Stock stock, Double currentCost) {
        costTrackList.put(stock, currentCost);
    }

    public void checkCostChange() {

        for (Map.Entry<Stock, Double> costTracker : costTrackList.entrySet()) {
            Stock stock = costTracker.getKey();
            double prevPrice = costTracker.getValue();
            double currPrice = stock.getActualCurrentPrice();
            if (prevPrice != currPrice) {
                if (loggedInUser.isPremium()) {
                    costTracker.setValue(currPrice);
                    Platform.runLater(() -> {
                        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                        errorAlert.setTitle("Stock " + stock.getActualLabel() + " price change!");
                        errorAlert.setHeaderText("Hello, " + username);
                        errorAlert.setContentText("Price changed from " + prevPrice + " to " + currPrice + ".");
                        errorAlert.showAndWait();
                    });
                    StockExchangeManager.saveCostTrackerList("costTracker.csv");
                }
            }
        }
    }
    public void addStockTransaction(String transaction) {
        stockTransactions.add(transaction);
    }

    public List<String> getStockTransactions() {
        return stockTransactions;
    }


    public void removeCostTrack(Stock stock) {
        costTrackList.remove(stock);
    }

    public HashMap<Stock, Double> getCostTrackList() {
        return costTrackList;
    }

    public boolean isAutoBuy(Stock stock) {
        return autoBuyList.containsKey(stock);
    }

    public void setAutoBuy(Stock stock, Double cost) {
        autoBuyList.put(stock, cost);
    }

    public void checkAutoBuy() {
        Iterator<Map.Entry<Stock, Double>> iterator = autoBuyList.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Stock, Double> autoBuy = iterator.next();
            Stock stock = autoBuy.getKey();
            double wantedPrice = autoBuy.getValue();
            double currPrice = stock.getActualCurrentPrice();
            if (currPrice <= wantedPrice) {
                System.out.println(currPrice + "  " + wantedPrice);
                iterator.remove(); // Remove safely using iterator
                Platform.runLater(() -> {
                    StockExchangeManager.createTransactionRequest(this, RequestType.BUY_STOCK, stock, currPrice);
                });
            }
        }
    }

    public void removeAutoBuy(Stock stock) {
        autoBuyList.remove(stock);
    }

    public HashMap<Stock, Double> getAutoBuyList() {
        return autoBuyList;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }

    public void deposit(double amount) {
        if (amount > 0) {
            accountBalance += amount;
            System.out.println("Deposit successful. New balance: " + accountBalance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0) {
            if (accountBalance >= amount) {
                accountBalance -= amount;
                System.out.println("Withdrawal successful. New balance: " + accountBalance);
            } else {
                System.out.println("Insufficient funds.");
            }
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
    }

    public void addTransaction(String type, double amount) {
        transactionHistory.add(new Pair<>(type, amount));
    }

    public List<Pair<String, Double>> getTransactionHistory() {
        return transactionHistory;
    }

}
