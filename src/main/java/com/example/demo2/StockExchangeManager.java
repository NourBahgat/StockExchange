package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockExchangeManager {
    private static List<String> users = new ArrayList<>();
    private Map<User, List<String>> userRequests;
//    private List<Stock.Transaction> transactionHistory;
    private List<Stock> availableStocks;
    private List<Order> orders;
    private List<Session> sessions;
    @FXML
    private ListView<String> usernameListView;




    public StockExchangeManager() {
        this.userRequests = new HashMap<>();
//        this.transactionHistory = new ArrayList<>();
        this.availableStocks = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.sessions = new ArrayList<>();
    }

//    public void addUser(User user) {
//        users.add(user);
//        userRequests.put(user, new ArrayList<>());
//    }

    public void addUser(String user) {
        this.users.add(user);
    }

    public static List<String> getUsers() {
        updateUsersFromCSV("users.csv");
        System.out.println(users);
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public void listUsers() {
        System.out.println("List of Users:");
        for (String user : users) {
            System.out.println(user);
        }
    }
    public static void updateUsersFromCSV(String filename) {
        users.clear(); // Clear the current list of users
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length > 0) {
                    users.add(userData[0]); // Add the username to the users list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
        // Assuming usernamesList is populated elsewhere in your code
        usernameListView.getItems().addAll(users);
    }


    public void addUserRequest(User user, String request) {
        if (userRequests.containsKey(user)) {
            userRequests.get(user).add(request);
        } else {
            List<String> requests = new ArrayList<>();
            requests.add(request);
            userRequests.put(user, requests);
        }
    }

    public void exportUserRequestsToCSV(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("User,Request\n");
            for (Map.Entry<User, List<String>> entry : userRequests.entrySet()) {
                for (String request : entry.getValue()) {
                    writer.write(entry.getKey().getUsername() + "," + request + "\n");
                }
            }
        }
    }

//    public List<Stock.Transaction> getUserTransactionHistory(User user) {
//        List<Stock.Transaction> userTransactions = new ArrayList<>();
//        for (Stock.Transaction transaction : transactionHistory) {
//            if (transaction.getLabel().equals(user)) {
//                userTransactions.add(transaction);
//            }
//        }
//        return userTransactions;
//    }

//    public void listStocksPriceHistory() {
//        for (Stock stock : availableStocks) {
//            System.out.println("Price history for " + stock.getLabel() + ": " + stock.getPriceHistory());
//        }
//    }
//
//    public void trackStocksPerformance() {
//        for (Stock stock : availableStocks) {
//            // Implement tracking stock performance based on requirements
//        }
//    }
//
//    public void addOrder(Order order) {
//        orders.add(order);
//    }

    public void deleteOrder(Order order) {
        orders.remove(order);
    }

    public void removeUser(User user) {
    }

    public void addStock(Stock stock) {
    }

    public void removeStock(Stock stock) {
    }

    public void listOrdersByLabel(String label) {
    }

    public void listUserRequests() {
    }

    public void approveRequest(User user, String request) {
    }

    public void disapproveRequest(User user, String request) {
    }

    public void addSession(Session session) {
    }

    public void removeSession(Session session) {
    }

    public void updateSession(Session session) {

    }


    public class Order extends StockExchangeManager{
        public Order() {

        }
    }
    public class Session extends StockExchangeManager{
        public Session() {

        }
    }
}