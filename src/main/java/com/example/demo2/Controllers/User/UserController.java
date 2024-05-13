package com.example.demo2.Controllers.User;

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



    public void handleSignUp(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String credit = creditField.getText();

        if (userExists(username)) {
            // Username already exists, display an error message or handle it as per your requirement
            System.out.println("Username already exists. Please choose a different username.");
            label.setVisible(true);
            label.setText("Username already exists. Please choose a different username.");

            return; // Exit the method without proceeding further
        }
        // Write user information to CSV file
            try (PrintWriter writer = new PrintWriter(new FileWriter("users.csv", true))) {
                writer.println(username + "," + password + "," + credit);
                label.setVisible(true);
                label.setText("Signed Up successfully, Return to login page");
                // Optionally, you can add a message to indicate successful sign-up
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
    }