package com.example.demo2;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Thread.sleep;

public class StockExchangeManager {
    public static List<User> users = new ArrayList<>();
    private static Map<User, List<String>> userRequests;
    public static ArrayList<Stock> stockList = new ArrayList<>();
    private static List<TransactionRequest> transactionRequests = new ArrayList<>();
    private List<Stock> availableStocks;
    private List<Order> orders;
    private List<Session> sessions;
    private static List<Pair<String, Double>> transactionHistory = new ArrayList<>();


    public StockExchangeManager() {
        this.userRequests = new HashMap<>();
        this.availableStocks = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.sessions = new ArrayList<>();
        new Thread(this::checkUpdates).start();
    }

    private void checkUpdates() {
        while(true) {
            System.out.println("Checking for updates");
            for (User user : users) {
                    user.checkAutoBuy();
                    user.checkCostChange();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addUser(User user) {
        this.users.add(user);
    }


    public void listUsers() {
        System.out.println("List of Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }
    public static void updateUsersFromCSV(String filename) {
        users.clear();
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

    public static User getUserFromUsername(String username) {
        for (User user : users) if (user.getUsername().equals(username)) return user;
        return null;
    }

    public static Stock getStockFromLabel(String label) {
        for (Stock stock : stockList) if (stock.getActualLabel().equals(label)) return stock;
        return null;
    }

public static User getLoggedInUser(String username, String password) {
    for (User user : users) {
        System.out.println(user.toString());
        if (user.getUsername().equals(username) && user.getPassword().equals(password))
            return user;
    }
    return null;
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

                logStockTransaction(user, "BUY", stock, price);

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
            logStockTransaction(user, "SELL", stock, price);

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

    public static void createTransactionRequest(User user, RequestType type, Stock stock, Double amount) {
        TransactionRequest newRequest = new TransactionRequest(user, type, stock, amount);
        addTransactionRequest(newRequest);
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Request Submitted");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText("Your request has been submitted for approval.");
        infoAlert.showAndWait();
    }

    public static void logStockTransaction(User user, String type, Stock stock, Double price) {
        String transaction = String.format("%s,%s,%s,%.2f", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), type, stock.getActualLabel(), price);
        user.addStockTransaction(transaction);
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
    public static void exportStockTransactionHistoryToCSV(User user) {
        String filePath = user.getUsername() + "_stock_transaction_history.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Timestamp,Type,Stock,Price\n");
            for (String transaction : user.getStockTransactions()) {
                writer.write(transaction + "\n");
            }
            writer.flush();
            System.out.println("Stock transaction history saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving stock transaction history: " + e.getMessage());
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
    public static void exportUserTransactionsToCSV(User user, String filename) {

    }
    public void deleteOrder(Order order) {
        orders.remove(order);
    }

    public void removeUser(User user) {
        users.remove(user);
        for (Stock stock : stockList) {
            stock.removeBuyer(user);
        }
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
        saveStockPriceHistoryToCSV("PriceHistory.csv");
        saveAutoBuyList("autoBuy.csv");
        saveCostTrackerList("costTracker.csv");
        saveAllUsersTransactionHistory("transactionsLogs.csv");
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

    private static void saveAutoBuyList(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.append("Username,Stock Label,Desired Price\n");
            for (User user : users) {
                HashMap<Stock, Double> autoBuyList = user.getAutoBuyList();
                String username = user.getUsername();
                for (Map.Entry<Stock, Double> autoBuy : autoBuyList.entrySet()) {
                    Stock stock = autoBuy.getKey();
                    writer.append(username).append(",").append(stock.getActualLabel()).append(",")
                            .append(autoBuy.getValue().toString()).append("\n");
                }
            }
            writer.flush();
            writer.close();
            System.out.println("CSV file saved successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred while saving CSV file: " + e.getMessage());
        }

    }

    public static void saveCostTrackerList(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.append("Username,Stock Label,Tracked Price\n");
            for (User user : users) {
                HashMap<Stock, Double> costTrackList = user.getCostTrackList();
                String username = user.getUsername();
                for (Map.Entry<Stock, Double> costTracker : costTrackList.entrySet()) {
                    System.out.println(costTracker);
                    Stock stock = costTracker.getKey();
                    writer.append(username).append(",").append(stock.getActualLabel()).append(",")
                            .append(costTracker.getValue().toString()).append("\n");
                }
            }
            writer.flush();
            writer.close();
            System.out.println("CSV file saved successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred while saving CSV file: " + e.getMessage());
        }

    }

    public static void saveAllUsersTransactionHistory(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Timestamp,Type,Stock,Price\n");
            for (User user : users) {
                writer.write(user.getUsername() + "\n");
                for (String transaction : user.getStockTransactions()) {
                    writer.write(transaction + "\n");
                }
            }
            writer.flush();
            writer.close();
            System.out.println("Stock transaction history saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving stock transaction history: " + e.getMessage());
        }
    }

    public static void loadSystem() {
        loadUserList();
        loadStockList();
        loadUserBoughtStocksList("boughtStocks.csv");
        loadUserTransactionHistory("transactions.csv");
        loadRequestList();
        loadStockPriceHistoryFromCSV("PriceHistory.csv");
        loadAutoBuyList("autoBuy.csv");
        loadCostTrackerList("costTracker.csv");
        loadAllUsersTransactionHistory("transactionsLogs.csv");
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

    public static void saveStockPriceHistoryToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("StockLabel,Timestamp,Price\n");
            for (Stock stock : stockList) {
                writer.append(stock.getActualLabel()).append(",")
                        .append(LocalDateTime.MIN.toString()).append(",")
                        .append(String.valueOf(stock.getActualInitialPrice())).append("\n");

                List<TimeStamp> priceHistory = stock.getPriceHistory();
                for (TimeStamp timeStamp : priceHistory) {
                    writer.append(stock.getActualLabel()).append(",")
                            .append(timeStamp.getTimestamp().toString()).append(",")
                            .append(String.valueOf(timeStamp.getPrice())).append("\n");
                }
            }
            writer.flush();
            System.out.println("Stock price history saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving stock price history: " + e.getMessage());
        }
    }
    public static void loadStockPriceHistoryFromCSV(String filePath) {
        String line;
        String cvsSplitBy = ",";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                if (data.length < 3) {
                    continue;
                }

                String stockLabel = data[0];
                LocalDateTime timestamp = LocalDateTime.parse(data[1], formatter);
                double price = Double.parseDouble(data[2]);

                Stock stock = getStockFromLabel(stockLabel);
                if (stock != null) {
                    if (timestamp.equals(LocalDateTime.MIN)) {
                        stock.setInitialPrice(price);
                    } else {
                        stock.addPriceHistory(new TimeStamp(price, timestamp));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading stock price history: " + e.getMessage());
        }
    }

    public static void loadAllUsersTransactionHistory(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            reader.readLine();
            User currUser = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 1) {
                    currUser = getUserFromUsername(parts[0]);
                }
                else {
                    String timestamp = parts[0];
                    String type = parts[1];
                    String stock = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    currUser.addStockTransaction(timestamp + "," + type + "," + stock + "," + price);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading stock transaction history for all users: " + e.getMessage());
        }
    }

    public static void loadUserList() {
        String csvFile = "users.csv";
        String line;
        String cvsSplitBy = ",";
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

    public static void loadAutoBuyList(String filePath) {
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            line = br.readLine();   // Skips header from file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                User user = getUserFromUsername(data[0]);
                Stock stock = getStockFromLabel(data[1]);
                Double amount = Double.parseDouble(data[2]);
                assert user != null;
                assert stock != null;
                user.setAutoBuy(stock, amount);
            }
        } catch (IOException e) {
            System.err.println("Error occurred while loading BoughtStocks CSV file: " + e.getMessage());
        }
    }

    public static void loadCostTrackerList(String filePath) {
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            line = br.readLine();   // Skips header from file
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                User user = getUserFromUsername(data[0]);
                Stock stock = getStockFromLabel(data[1]);
                Double amount = Double.parseDouble(data[2]);
                assert user != null;
                assert stock != null;
                user.setCostTracked(stock, amount);
            }
        } catch (IOException e) {
            System.err.println("Error occurred while loading BoughtStocks CSV file: " + e.getMessage());
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
