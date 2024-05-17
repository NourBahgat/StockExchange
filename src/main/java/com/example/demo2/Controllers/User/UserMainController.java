package com.example.demo2.Controllers.User;

import com.example.demo2.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class UserMainController {
    private User loggedInUser;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField AccountBalance;
    @FXML
    private Label Balance;
    @FXML
    private Label numStocks;
    @FXML
    private Label userName;
    @FXML
    private Label smallamount;
    @FXML
    private TextField MoneyTextField;
    private double depositedMoney;
    private double Subscriptionprice =100;

//    public void initialize(){
//
//    }
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
    public void switchToTrackStocks (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/TrackStocks.fxml")));
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
    public void initData(User user){
        loggedInUser=user;
        userName.setText("Hello, "+ loggedInUser.getUsername());
        numStocks.setText(String.valueOf(loggedInUser.getNumOfStocks()));
        Balance.setText(String.valueOf(loggedInUser.getAccountBalance()));
    }
    public void BackToLogin (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Login.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);}
    public void GoToSubscribe (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/Subscription.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void deposit(ActionEvent event) throws IOException {
        depositedMoney =Double.parseDouble( MoneyTextField.getText());
    }
    public void Subscripe(ActionEvent event) throws IOException {
        if (depositedMoney < Subscriptionprice) {
            smallamount.setText("Insufficient Amount");
        } else {

        }
    }
}
