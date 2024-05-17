package com.example.demo2.Controllers.User;

import com.example.demo2.Controllers.Admin.AdminController;
import com.example.demo2.StockExchangeManager;
import com.example.demo2.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

import static com.example.demo2.Admin.searchUserInCSV;
//import static com.example.demo2.StockExchangeManager.users;


public class UserController {
//    private User loggedInUser;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField creditField;
    @FXML
    private Label label;
    private Button button;

    @FXML
    private Label ExpiredLabel;

    @FXML
    private TextField AccountBalance;

    private User user;
    @FXML
    private Label Balance;

//    public void initialize() {
//        // You can initialize AccountBalance here or in your FXML file
//        AccountBalance = new TextField();
//    }

    public void handleSignUp(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        double credit = Double.parseDouble(creditField.getText());
//        StockExchangeManapager userAdd= new StockExchangeManager();

        if (userExists(username)) {
            // Username already exists, display an error message or handle it as per your requirement
            System.out.println("Username already exists. Please choose a different username.");
            label.setVisible(true);
            label.setText("Username already exists. Please choose a different username.");

            return; // Exit the method without proceeding further
        }

//        userAdd.addUser(username);
//        userAdd.listUsers();
        // Write user information to CSV file
            try (PrintWriter writer = new PrintWriter(new FileWriter("users.csv", true))) {
                writer.println(username + "," + password + "," + credit);
                label.setVisible(true);
                label.setText("Signed Up successfully, Return to login page");

                StockExchangeManager userAdd = new StockExchangeManager();
                userAdd.updateUsersFromCSV("users.csv");
                User newUser = new User(username, password, credit);
                userAdd.addUser(newUser);
                userAdd.listUsers();

            } catch (IOException e) {
                e.printStackTrace();
                // Handle IO exception
            }
    }

    public void switchToSignUp (ActionEvent event) throws IOException {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/SignUp.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

    }

    private boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length > 0 && userData[0].equals(username)) {
                    return true; // Username found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exception
        }
        return false; // Username not found
    }
    public void switchToSignIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/StandardUser/SignIn.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

   // AdminController admin = new AdminController();
    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User loggedInUser = StockExchangeManager.getLoggedInUser(username, password);
        double accountBalance = loggedInUser.getAccountBalance();
        System.out.println(accountBalance);
//        UserMainController.setTitle(loggedInUser);
      if(AdminController.StartSession && validateLogin(username, password)) {
//          loggedInUser= new User(username, password, 0.0);
//          initData(loggedInUser);
          Parent root = FXMLLoader.load(getClass().getResource("/FXML/StandardUser/UserMain.fxml"));
          stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
          scene = new Scene(root);
          stage.setScene(scene);
          stage.show();
          // Redirect to the main application or user dashboard
        }
      if (!validateLogin(username, password)) {
            ExpiredLabel.setVisible(false);
            System.out.println("Invalid username or password");
          label.setVisible(true);
            label.setText("Invalid username or password");
        }
      if (!AdminController.StartSession&&validateLogin(username, password)){
          label.setVisible(false);
          ExpiredLabel.setVisible(true);
          ExpiredLabel.setText("Session Expired");
      }
    }

    private boolean validateLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 2 && userData[0].equals(username) && userData[1].equals(password)) {
                    return true; // Username and password match
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exception
        }
        return false; // Username and password do not match
    }
//    public void switchToViewStocks (ActionEvent event) throws IOException {
//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/ViewStocks.fxml")));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//
//    }
//    public void switchToRequestsPage (ActionEvent event) throws IOException {
//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/RequestsPage.fxml")));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//
//    }
    public void BackToUserMain (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/UserMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

    }
//    public void initData(User user) {
//        loggedInUser = user;
//        if (loggedInUser != null) {
//            // Retrieve account balance from StockExchangeManager's static list
//            User userbalance= new
//            AccountBalance.setText(String.valueOf(accountBalance));
//        }
//    }
//    public void displayAccountBalance(String username, String password) {
//        User user=getUserByUsernameAndPassword(username,password);
//        double balance= user.getAccountBalance();
//        Balance.setVisible(true);
//        System.out.println("i passed here");
//        }
    private User getUserByUsernameAndPassword(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 3 && userData[0].equals(username) && userData[1].equals(password)) {
                    // Username and password match, create and return a User object
                    double balance = Double.parseDouble(userData[2]);
                    return new User(username, password, balance);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exception
        }
        // Username and password not found, return null
        return null;
    }
//    private User getUserFromCSV(String username) {
//        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] userData = line.split(",");
//                if (userData.length >= 3 && userData[0].equals(username)) {
//                    // Parse user information from CSV and create a User object
//                    String password = userData[1];
//                    double accountBalance = Double.parseDouble(userData[2]);
//                    return new User(username, password, accountBalance);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    public void setUser(User user) {
//        this.user = user;
//        if (user != null) {
//            // Set the account balance in the text field
//            accountBalance.setText(String.valueOf(user.getAccountBalance()));
//        }
//    }
//public void displayAccountBalance(ActionEvent event){
//        User userObj = new User(usernameField.getText(),passwordField.getText(),)
//}

}
