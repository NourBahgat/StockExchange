package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class StockExchangeManager {
    private static List<User> users = new ArrayList<>();
    private Map<User, List<String>> userRequests;
//    private List<Stock.Transaction> transactionHistory;
    public static ArrayList<Stock> stockList = new ArrayList<>();
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

    public void addUser(User user) {
        this.users.add(user);
    }

    public static List<User> getUsers() {
        updateUsersFromCSV("users.csv");
        System.out.println(users);
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void listUsers() {
        System.out.println("List of Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }
    public static void updateUsersFromCSV(String filename) {
        users.clear(); // Clear the current list of users
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int idx = 0;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length > 0 && idx != 0) {
                    User loadedUser = new User(userData[0], userData[1], Double.parseDouble(userData[2]), Integer.parseInt(userData[3]),Boolean.getBoolean(userData[4]));
                    users.add(loadedUser); // Add the username to the users list
                }
                idx++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getUsernameList() {
        ArrayList<String> usernames = new ArrayList<>();

        for (User user : users) {
            usernames.add(user.getUsername());
        }

        return usernames;
    }

    public void initialize() {

        usernameListView.getItems().addAll(StockExchangeManager.getUsernameList());
    }

//    public static User getLoggedInUser(String username, String password) {
//        for (User user : users) {
//            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
//                return user;
//            }
//        }
//        return null;
//    }
public static double getLoggedInUser(String username, String password) {
    try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
        String line;
        int idx = 0;
        while ((line = reader.readLine()) != null) {
            String[] userData = line.split(",");
            if (userData.length > 0 && idx != 0) {
                String csvUsername = userData[0];
                String csvPassword = userData[1];
                double balance = Double.parseDouble(userData[2]);
                int numOfStocks=Integer.parseInt(userData[3]);
                boolean isPremium=Boolean.parseBoolean(userData[4]);

                if (csvUsername.equals(username) && csvPassword.equals(password)) {
                    // Found matching user, return User object
                    return balance;
                }
            }
            idx++;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return 0;
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
//        Stock.getStockList();
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("User,Request\n");
            for (Map.Entry<User, List<String>> entry : userRequests.entrySet()) {
                for (String request : entry.getValue()) {
                    writer.write(entry.getKey().getUsername() + "," + request + "\n");
                }
            }
        }
    }
    public static void saveFromCSVtoList(){

        String csvFile = "stocks.csv";
        String line;
        String cvsSplitBy = ",";
        stockList.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                String labelSlot=data[0];
                double initialPricesSlot=Double.parseDouble(data[1]);
                double currentPriceSlot=Double.parseDouble(data[2]);
                int availableStockSlot=Integer.parseInt(data[3]);
                double profitsSlot= Double.parseDouble(data[4]);
                Stock loadstock=new Stock(labelSlot, initialPricesSlot,currentPriceSlot,availableStockSlot, profitsSlot);
                stockList.add(loadstock);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static void updateCSV() {
        String csvFile = "stocks.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            for (Stock stock : stockList) {
                String line = stock.getActualLabel() + "," +
                        stock.getActualInitialPrice() + "," +
                        stock.getActualCurrentPrice() + "," +
                        stock.getActualAvailableStocks() + "," +
                        stock.getActualProfit();
                writer.write(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void buyStock(User user, Stock stock) {
        // Display confirmation dialog to admin
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Buy Stock Request");
        alert.setHeaderText("User " + user.getUsername() + " wants to buy stock " + stock.getLabel());
        alert.setContentText("Do you want to approve this request?");
        Optional<ButtonType> result = alert.showAndWait();

        // If admin approves the request
        if (result.isPresent() && result.get() == ButtonType.OK) {
            double price = stock.getActualCurrentPrice();
            double userCredit = user.getAccountBalance();

            // Check if user has enough credit
            if (userCredit >= price) {
                // Subtract price from user credit
                user.setAccountBalance(userCredit - price);

                // Decrease available stocks
                int availableStocks = stock.getActualAvailableStocks();
                stock.setAvailableStocks(availableStocks - 1);

                // Increase number of stocks bought by the user
                user.setNumOfStocks(user.getNumOfStocks() + 1);

                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Stock purchase approved and completed successfully!");
                successAlert.showAndWait();
            } else {
                // Show insufficient credit message
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Insufficient credit to buy the stock!");
                errorAlert.showAndWait();
            }
        } else {
            // Admin disapproved the request
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Stock purchase request disapproved by admin!");
            errorAlert.showAndWait();
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