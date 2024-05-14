package com.example.demo2.Controllers.Admin;

import com.example.demo2.Admin;
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

import java.io.IOException;
import java.util.Objects;

public class AdminController {
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
    public void switchToManageUsers (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/ManageUsers.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

}
