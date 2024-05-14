package com.example.demo2;

import java.io.IOException;

public class Admin {
    private String adminName;
    private String adminPassword;

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public Admin() {
        this.adminName = "soso";
        this.adminPassword = "1234";
    }
    // Method to add a new user
    public void addUser(User user, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.addUser(user);
    }

    // Method to remove a user
    public void removeUser(User user, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.removeUser(user);
    }

    // Method to add a new stock
    public void addStock(Stock stock, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.addStock(stock);
    }

    // Method to remove a stock
    public void removeStock(Stock stock, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.removeStock(stock);
    }

    // Method to update stock information
    public void updateStockInfo(Stock stock, String label, double initialPrice, double currentPrice, int availableStocks, double profit) {
        stock.setLabel(label);
        stock.setInitialPrice(initialPrice);
        stock.setCurrentPrice(currentPrice);
        stock.setAvailableStocks(availableStocks);
        stock.setProfit(profit);
    }

    // Method to list orders by label
    public void listOrdersByLabel(String label, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.listOrdersByLabel(label);
    }

    // Method to get stock price history
    public void getStockPriceHistory(Stock stock) {
        System.out.println("Price history for " + stock.getLabel() + ": " + stock.getPriceHistory());
    }

    // Method to list user requests
    public void listUserRequests(StockExchangeManager stockExchangeManager) {
        stockExchangeManager.listUserRequests();
    }

    // Method to approve user requests
    public void approveRequest(User user, String request, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.approveRequest(user, request);
    }

    // Method to disapprove user requests
    public void disapproveRequest(User user, String request, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.disapproveRequest(user, request);
    }

    // Method to add session
    public void addSession(StockExchangeManager.Session session, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.addSession(session);
    }

    // Method to remove session
    public void removeSession(StockExchangeManager.Session session, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.removeSession(session);
    }

    // Method to update session
    public void updateSession(StockExchangeManager.Session session, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.updateSession(session);
    }

    // Method to export user requests to CSV
    public void exportUserRequestsToCSV(String filename, StockExchangeManager stockExchangeManager) {
        try {
            stockExchangeManager.exportUserRequestsToCSV(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}