package com.example.demo2.Controllers.User;

import com.example.demo2.Stock;
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
import java.util.List;
import java.util.Objects;

import static com.example.demo2.StockExchangeManager.stockList;


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
    private Label Remaining;
    @FXML
    private TextField MoneyTextField;
    private double depositedMoney;
    private double Subscriptionprice =100;
    private double remainingBalance;
//    public void initialize(){
//
//    }
    public void switchToViewStocks (ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/StandardUser/ViewStocks.fxml"));
        root=loader.load();
        ViewStocks viewStocks = loader.getController();
        viewStocks.initData(loggedInUser);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToRequestsPage (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/RequestsPage.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

    }
    public void switchToTrackStocks (ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/StandardUser/TrackStocks.fxml"));
        root=loader.load();
        TrackStocksController trackStocksController = loader.getController();
        trackStocksController.initData(loggedInUser);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

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
        stage.setScene(scene);
    }
    public void GoToSubscribe (ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/Subscription.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void deposit(ActionEvent event) throws IOException {
        depositedMoney =Double.parseDouble( MoneyTextField.getText());
    }
//    public void Subscripe(ActionEvent event) throws IOException {
//        if (depositedMoney < Subscriptionprice) {
//            smallamount.setText("Insufficient Amount");
//        }
//        else{
//
//            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/SubscriptionMain.fxml")));
//            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            scene = new Scene(root);
//            stage.setScene(scene);
//            remainingBalance=depositedMoney-Subscriptionprice ;
//            Remaining.setText("Remaining Balance ="+remainingBalance+"EGP");
//        }
      public void Subscripe(ActionEvent event) throws IOException {
          if (depositedMoney == Subscriptionprice){
              root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/SubscriptionMain.fxml")));
              stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
              scene = new Scene(root);
              stage.setScene(scene);
          }
         else {
              smallamount.setText("Insufficient Amount");
          }

      }
    public void Notifications(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/Notifications.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void LineCharts(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/StandardUser/Charts.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void switchToMarketPerformance(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/StandardUser/MarketPerformanceCharts.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    public void switchToDepositWithdraw(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/FXML/StandardUser/DepositOrWithdraw.fxml"));
        root=loader.load();
        DepositOrWithdawController DepositOrWithdraw = loader.getController();
        DepositOrWithdraw.initData(loggedInUser);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}
