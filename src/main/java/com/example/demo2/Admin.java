package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.*;

public class Admin {
    private String adminName;
    private String adminPassword;
    @FXML
    private ListView<String> usernameListView;

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

    public static String[] searchUserInCSV(String username) {
        String csvFile = "users.csv"; // Replace with the path to your CSV file
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                if (data.length >= 3 && data[0].equals(username)) {
                    return new String[]{data[1], data[2]};
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Method to add a new user
//  public void addUser(User user, StockExchangeManager stockExchangeManager) {
//       stockExchangeManager.addUser(user);
// }
public void removeUser(String username)
{
    String csvFile = "users.csv"; // Replace with the path to your CSV file
    String tempFile = "temp.csv"; // Replace with the path to a temporary file

    File inputFile = new File(csvFile);
    File tempOutputFile = new File(tempFile);

    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
         BufferedWriter writer = new BufferedWriter(new FileWriter(tempOutputFile))) {

        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 3 && data[0].equals(username)) {
                continue;
            }
            writer.write(line + System.getProperty("line.separator"));
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(tempOutputFile));
         BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            writer.write(line + System.getProperty("line.separator"));
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
//    public void addUser(String username, String password, double credit) {
//        String csvFile = "users.csv";
//
//        try (FileWriter writer = new FileWriter(csvFile, true)) {
//            writer.append(username).append(",").append(password).append(",").append(String.valueOf(credit)).append("\n");
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


        public void addUser(String username, String password, double credit) {
            try {
                FileWriter writer = new FileWriter("users.csv", true);
                writer.append(username).append(",").append(password).append(",").append(String.valueOf(credit)).append("\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    // Method to add a new stock
    public void addStock(Stock stock, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.addStock(stock);
    }

    // Method to remove a stock
    public void removeStock(Stock stock) {
        String csvFile = "stocks.csv"; // Replace with the path to your CSV file
        String tempFile = "temp2.csv"; // Replace with the path to a temporary file

        File inputFile = new File(csvFile);
        File tempOutputFile = new File(tempFile);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempOutputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(stock.getLabel())) {
                    continue;
                }
                writer.write(line + System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(tempOutputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                writer.write(line + System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to update stock information
//    public void updateStockInfo(Stock stock, String label, double initialPrice, double currentPrice, int availableStocks, double profit) {
//        stock.setLabel(label);
//        stock.setInitialPrice(initialPrice);
//        stock.setCurrentPrice(currentPrice);
//        stock.setAvailableStocks(availableStocks);
//        stock.setProfit(profit);
//    }

    // Method to list orders by label
    public void listOrdersByLabel(String label, StockExchangeManager stockExchangeManager) {
        stockExchangeManager.listOrdersByLabel(label);
    }

    // Method to get stock price history
//    public void getStockPriceHistory(Stock stock) {
//        System.out.println("Price history for " + stock.getLabel() + ": " + stock.getPriceHistory());
//    }

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