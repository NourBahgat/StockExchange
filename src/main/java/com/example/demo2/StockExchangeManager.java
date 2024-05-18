package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class StockExchangeManager {
    private static List<User> users = new ArrayList<>();
    private static Map<User, List<String>> userRequests;
//    private List<Stock.Transaction> transactionHistory;
    public static ArrayList<Stock> stockList = new ArrayList<>();
    private static List<TransactionRequest> transactionRequests = new ArrayList<>();
    private List<Stock> availableStocks;
    private List<Order> orders;
    private List<Session> sessions;
    private static List<Pair<String, Double>> transactionHistory = new ArrayList<>();


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


    public static User getUserFromUsername(String username) {
        for (User user : users) if (user.getUsername().equals(username)) return user;
        return null;
    }

    public static Stock getStockFromLabel(String label) {
        for (Stock stock : stockList) if (stock.getActualLabel().equals(label)) return stock;
        return null;
    }

//    public static User getLoggedInUser(String username, String password) {
//        for (User user : users) {
//            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
//                return user;
//            }
//        }
//        return null;
//    }
public static User getLoggedInUser(String username, String password) {
    for (User user : users) {
        System.out.println(user.toString());
        if (user.getUsername().equals(username) && user.getPassword().equals(password))
            return user;
    }
    return null;
}


    public static void addUserRequest(User user, String request) {
        if (userRequests.containsKey(user)) {
            userRequests.get(user).add(request);
        } else {
            List<String> requests = new ArrayList<>();
            requests.add(request);
            userRequests.put(user, requests);
        }
    }

    public static void buyStock(User user, Stock stock) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Buy Stock Request");
        alert.setHeaderText("User " + user.getUsername() + " wants to buy stock " + stock.getLabel());
        alert.setContentText("Do you want to approve this request?");
        Optional<ButtonType> result = alert.showAndWait();
        String filePath = "boughtStocks.csv";

        if (result.isPresent() && result.get() == ButtonType.OK) {
            double price = stock.getActualCurrentPrice();
            double userCredit = user.getAccountBalance();

            if (userCredit >= price) {
                user.setAccountBalance(userCredit - price);
                int availableStocks = stock.getActualAvailableStocks();
                stock.setAvailableStocks(availableStocks - 1);
                stock.addBuyer(user, price);
                user.setNumOfStocks(user.getNumOfStocks() + 1);

                //saveUserBoughtStocksToCSV(user, filePath);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Stock purchase approved and completed successfully!");
                successAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Insufficient credit to buy the stock!");
                errorAlert.showAndWait();
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Stock purchase request disapproved by admin!");
            errorAlert.showAndWait();
        }
    }

    public static void sellStock(User user, Stock stock) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sell Stock Request");
        alert.setHeaderText("User " + user.getUsername() + " wants to buy stock " + stock.getLabel());
        alert.setContentText("Do you want to approve this request?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            double price = stock.getActualCurrentPrice();
            double userCredit = user.getAccountBalance();
            stock.removeBuyer(user, price);
            user.setAccountBalance(userCredit + price);
            int availableStocks = stock.getActualAvailableStocks();
            stock.setAvailableStocks(availableStocks + 1);
            user.setNumOfStocks(user.getNumOfStocks() - 1);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Stock sold successfully!");
            successAlert.showAndWait();

        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Stock sell request disapproved by admin!");
            errorAlert.showAndWait();
        }
    }



    public static List<Pair<Stock, List<Double>>> getUserBoughtStocks(User user) {
        List<Pair<Stock, List<Double>>> boughtStocksList = new ArrayList<>();
        for (Stock stock : stockList) {
            List<Double> orderCosts = stock.getBuyerOrderCosts(user);
            if (orderCosts != null) {
                Pair<Stock, List<Double>> stockCosts = new Pair<>(stock, orderCosts);
                boughtStocksList.add(stockCosts);
            }
        }
        return boughtStocksList;
    }

    public static void removeStockFromUser(User user, String stockLabel, double purchasePrice) {
        Stock stock = getStockFromLabel(stockLabel);
        if (stock != null) {
            stock.removeBuyer(user, purchasePrice);
//            updateBoughtStocksCSV();
//            updateStockCSV();
        }
    }

    public static void createTransactionRequest(User user, RequestType type, Stock stock, Double amount) {
        TransactionRequest newRequest = new TransactionRequest(user, type, stock, amount);
        addTransactionRequest(newRequest);
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Request Submitted");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText("Your request has been submitted for approval.");
        infoAlert.showAndWait();
    }
    public static void logTransaction(User user, String type, Double amount) {
        user.addTransaction(type, amount);
        transactionHistory.add(new Pair<>(type, amount));
    }

    public static void saveTransactionHistoryToCSV(User user) {
        String filePath = user.getUsername() + "_transaction_history.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Type,Amount\n");
            for (Pair<String, Double> transaction : user.getTransactionHistory()) {
                writer.write(transaction.getKey() + "," + transaction.getValue() + "\n");
            }
            writer.flush();
            System.out.println("Transaction history saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving transaction history: " + e.getMessage());
        }
    }

    public static void addTransactionRequest(TransactionRequest request) {
        transactionRequests.add(request);
    }

    public static List<TransactionRequest> getTransactionRequests() {
        return new ArrayList<>(transactionRequests);
    }

    public static void approveTransaction(TransactionRequest request) {
        User user = request.getUser();
        Stock stock = request.getStock();
        Double amount = request.getAmount();
        RequestType type = request.getRequestType();

        switch (type) {
            case DEPOSIT:
                user.addTransaction(type.name(), amount);
                user.deposit(amount);
                break;
            case WITHDRAWAL:
                user.addTransaction(type.name(), amount);
                user.withdraw(amount);
                break;
            case BUY_STOCK:
                buyStock(user, stock);
                break;
            case SELL_STOCK:
                sellStock(user, stock);
                break;
            default:
                break;
        }

        transactionRequests.remove(request);
    }
    public static void disapproveTransaction(TransactionRequest request) {
        //sheel el request bas??
        transactionRequests.remove(request);
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

    public static void saveSystem() {
        updateUserCSV();
        updateStockCSV();
        saveUserBoughtStocksToCSV("boughtStocks.csv");
        saveUserTransactionHistory("transactions.csv");
        updateRequestCSV();
//        StockExchangeManager.saveTransactionHistoryToCSV(loggedInUser, "transactionHistory.csv");
    }

    public static void updateStockCSV() {
        String csvFile = "stocks.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("label,initial price,current price,available stocks,profit"
                    + System.getProperty("line.separator"));
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

    public static void updateUserCSV() {
        String csvFile = "users.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("username,password,initial credit,numOfStocks,isPremium"
                    + System.getProperty("line.separator"));
            for (User user : users) {
                String line = user.getUsername() + "," +
                        user.getPassword() + "," +
                        user.getAccountBalance() + "," +
                        user.getNumOfStocks() + "," +
                        user.isPremium();
                writer.write(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveUserBoughtStocksToCSV(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.append("Username,Stock Name,Bought Prices\n");
            for (User user : users) {
                List<Pair<Stock, List<Double>>> boughtStocksList = getUserBoughtStocks(user);
                for (Pair<Stock, List<Double>> stockCosts : boughtStocksList) {
                    Stock stock = stockCosts.getKey();
                    List<Double> prices = stockCosts.getValue();
                    writer.append(user.getUsername()).append(",").append(stock.getActualLabel());
                    for (Double price : prices) {
                        writer.append(",").append(String.valueOf(price));
                    }
                    writer.append("\n");
                }
            }
            writer.flush();
            writer.close();
            System.out.println("CSV file saved successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred while saving CSV file: " + e.getMessage());
        }

    }

    public static void saveUserTransactionHistory(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.append("Username,Transaction Type,Amount\n");
            for (User user : users) {
                List<Pair<String, Double>> transactionHistory = user.getTransactionHistory();
                String username = user.getUsername();
                for (Pair<String, Double> transaction : transactionHistory) {
                    writer.append(username).append(",").append(transaction.getKey()).append(",")
                            .append(transaction.getValue().toString()).append("\n");
                }
            }
            writer.flush();
            writer.close();
            System.out.println("CSV file saved successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred while saving CSV file: " + e.getMessage());
        }

    }

    private static void updateRequestCSV() {
        String csvFile = "requests.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("username,action_type,label,amount"
                    + System.getProperty("line.separator"));
            for (TransactionRequest request : transactionRequests) {
                String line = request.getUsername() + ","
                        + request.getRequestType() + ","
                        + request.getLabel() + ","
                        + request.getAmount();
                writer.write(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSystem() {
        loadUserList();
        loadStockList();
        loadUserBoughtStocksList("boughtStocks.csv");
        loadUserTransactionHistory("transactions.csv");
        loadRequestList();
//        StockExchangeManager.loadTransactionHistoryFromCSV(, "transactionHistory.csv");
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

    public static void loadUserList() {
        String csvFile = "users.csv";
        String line;
        String cvsSplitBy = ",";
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            line = br.readLine();   // Skips header from file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                String username= data[0];
                String password = data[1];
                double credit = Double.parseDouble(data[2]);
                int numOfStocks = Integer.parseInt(data[3]);
                boolean isPremium = Boolean.parseBoolean(data[4]);
                User loadedUser = new User(username, password, credit, numOfStocks, isPremium);
                users.add(loadedUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadStockList(){

        String csvFile = "stocks.csv";
        String line;
        String cvsSplitBy = ",";
        stockList.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                String labelSlot=data[0];
                double initialPricesSlot=Double.parseDouble(data[1]);
                double currentPriceSlot=Double.parseDouble(data[2]);
                int availableStockSlot=Integer.parseInt(data[3]);
                double profitsSlot= Double.parseDouble(data[4]);
                Stock loadstock = new Stock(labelSlot, initialPricesSlot,currentPriceSlot,availableStockSlot, profitsSlot);
                stockList.add(loadstock);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadUserBoughtStocksList(String filePath) {
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            line = br.readLine();   // Skips header from file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                User user = getUserFromUsername(data[0]);
                Stock stock = getStockFromLabel(data[1]);
                if (user == null || stock == null) continue;
                for (int i = 2; i < data.length; i++) {
                    stock.addBuyer(user, Double.parseDouble(data[i]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error occurred while loading BoughtStocks CSV file: " + e.getMessage());
        }
    }

    public static void loadUserTransactionHistory(String filePath) {
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            line = br.readLine();   // Skips header from file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                User user = getUserFromUsername(data[0]);
                String type = data[1];
                double amount = Double.parseDouble(data[2]);
                assert user != null;
                user.addTransaction(type, amount);
            }
        } catch (IOException e) {
            System.err.println("Error occurred while loading BoughtStocks CSV file: " + e.getMessage());
        }
    }

    private static void loadRequestList() {
        String csvFile = "requests.csv";
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            line = br.readLine();   // Skips header from file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                User user = getUserFromUsername(data[0]);
                RequestType type = RequestType.valueOf(data[1]);
                Stock stock = getStockFromLabel(data[2]);
                Double amount = Double.parseDouble(data[3]);
                TransactionRequest loadedRequest = new TransactionRequest(user, type, stock, amount);
                transactionRequests.add(loadedRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUsers() {
        return users;
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
