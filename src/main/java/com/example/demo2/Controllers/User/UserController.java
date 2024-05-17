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
    private Label test;
    private User loggedInUser;

    @FXML
    private TextField AccountBalance;

    private User user;
    @FXML
    private Label Balance;
    private User obj;
    private User obj2;

//    public void initialize() {
//        // You can initialize AccountBalance here or in your FXML file
//        AccountBalance = new TextField();
//    }

    public void handleSignUp(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        int numOfStocks=0;
        boolean isPremium=false;
        double credit = Double.parseDouble(creditField.getText());
//        StockExchangeManapager userAdd= new StockExchangeManager();

        if (userExists(username)) {
            // Username already exists, display an error message or handle it as per your requirement
            System.out.println("Username already exists. Please choose a different username.");
            label.setVisible(true);
            label.setText("Username already exists. Please choose a different username.");

            return; // Exit the method without proceeding further
        }

        // Write user information to CSV file
            try (PrintWriter writer = new PrintWriter(new FileWriter("users.csv", true))) {
                writer.println(username + "," + password + "," + credit + "," + numOfStocks + "," + isPremium);
                label.setVisible(true);
                label.setText("Signed Up successfully, Return to login page");

                StockExchangeManager userAdd = new StockExchangeManager();
                userAdd.updateUsersFromCSV("users.csv");
                User newUser = new User(username, password, credit, numOfStocks,isPremium);
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
        obj=new User();
//        StockExchangeManager objBalance=new StockExchangeManager();
        String username = usernameField.getText();
        String password = passwordField.getText();
        obj.setUsername(username);
        obj.setPassword(password);
        double balance=StockExchangeManager.getLoggedInUser(username,password);
        obj.setAccountBalance(balance);
        int numberOfStocks=obj.getNumOfStocks();
        System.out.println(obj);
      if(AdminController.StartSession && validateLogin(username, password)) {

          //Parent root = FXMLLoader.load(getClass().getResource("/FXML/StandardUser/UserMain.fxml"));
          FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/StandardUser/UserMain.fxml"));
          root=loader.load();
          UserMainController userMainController=loader.getController();
          userMainController.displayBalance(obj.getAccountBalance());
          userMainController.displayNumberOfStocks(numberOfStocks);
          userMainController.helloUser(username);
          userMainController.buyStock(obj);
          System.out.println(obj.getAccountBalance());

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
    public void switchToViewStocks (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/ViewStocks.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

    }
    public void switchToRequestsPage (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/RequestsPage.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

    }
    public void BackToUserMain (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/UserMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

}
