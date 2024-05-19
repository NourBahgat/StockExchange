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

public class PremiumController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private User loggedInUser;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label label;
    private Button button;

//    public void switchToSignInPremium(ActionEvent event) throws IOException {
//        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/PremiumUser/Premium.fxml")));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//    }

    public void initData(User user){
        loggedInUser=user;
    }

    public void BackToUserMain(ActionEvent event) throws IOException {
        User user = loggedInUser;
        System.out.println(user.toString());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/UserMain.fxml"));
        root = loader.load();
        UserMainController userMainController = loader.getController();
        userMainController.initData(user);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void handleShowNotifications(ActionEvent event) throws IOException {


    }
    public void handleShowLineCharts (ActionEvent event) throws IOException {


    }







}
