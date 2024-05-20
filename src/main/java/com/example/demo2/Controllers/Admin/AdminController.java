package com.example.demo2.Controllers.Admin;

import com.example.demo2.*;
import com.example.demo2.Controllers.User.UserController;
import com.example.demo2.Controllers.User.UserMainController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;


import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.demo2.StockExchangeManager.stockList;
import static com.example.demo2.StockExchangeManager.users;

public class AdminController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label label = new Label();
    @FXML
    private Label label1 = new Label();
    public static boolean StartSession = true;

    public static void checkSession(Stage currentStage) {
        if (!StartSession) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Session Expired");
                alert.setHeaderText(null);
                alert.setContentText("SESSION EXPIRED");
                alert.showAndWait();
                redirectToLoginPage(currentStage);
            });
        }
    }
    public static boolean isStartSession() {
        return StartSession;
    }

    private static void redirectToLoginPage(Stage currentStage) {
        try {
            Parent root = FXMLLoader.load(AdminController.class.getResource("/Fxml/Login.fxml"));
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLogIn(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Admin soso = new Admin();

        if (username.equals(soso.getAdminName()) && password.equals(soso.getAdminPassword())) {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/AdminMain.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

        } else {
            label.setVisible(true);
            label.setText("Invalid username or password");
        }
    }

    public void switchToManageUsers(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/ManageUsers.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void switchToManageStocks(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/ManageStocks.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void switchToManageSessions(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/trading session.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void StartSession(ActionEvent event) throws IOException {
        StartSession = true;
    }

    public void EndSession(ActionEvent event) throws IOException {
        StartSession = false;
    }

    public void BackToMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/AdminMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void AdminLogOut(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void switchToManageRequests(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/ManageRequests.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    private void updateUserAndStockCount() {
        int numUsers = users.size();
        int numStocks = stockList.size();
        System.out.println(numStocks);
        System.out.println(numUsers);
        label.setText(String.valueOf(numUsers));
        label1.setText(String.valueOf(numStocks));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateUserAndStockCount();
    }
}
