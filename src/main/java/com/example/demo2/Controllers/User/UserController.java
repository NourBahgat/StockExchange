package com.example.demo2.Controllers.User;

import com.example.demo2.Controllers.Admin.AdminController;
import com.example.demo2.Stock;
import com.example.demo2.StockExchangeManager;
import com.example.demo2.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;
import java.util.Optional;

import static com.example.demo2.Admin.searchUserInCSV;



public class UserController {
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

    public static User loggedInUser;


    public void handleSignUp(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        int numOfStocks=0;
        boolean isPremium=false;
        double credit = Double.parseDouble(creditField.getText());

        if (userExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            label.setVisible(true);
            label.setText("Username already exists. Please choose a different username.");
            return;
        }


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


    public void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        loggedInUser = StockExchangeManager.getLoggedInUser(username,password);
        System.out.println(loggedInUser);
      if(AdminController.StartSession && validateLogin(username, password)) {

          FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/StandardUser/UserMain.fxml"));
          root=loader.load();
          UserMainController userMainController=loader.getController();
          userMainController.initData(loggedInUser);
          System.out.println(loggedInUser.getAccountBalance());

          stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
          scene = new Scene(root);
          stage.setScene(scene);
          stage.show();

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

        }
        return false; // Username and password do not match
    }
    public void BackToUserMain (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/UserMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}

