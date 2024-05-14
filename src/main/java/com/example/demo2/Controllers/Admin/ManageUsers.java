package com.example.demo2.Controllers.Admin;

import com.example.demo2.Admin;
import com.example.demo2.StockExchangeManager;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.List;
public class ManageUsers {
    @FXML
    private TableView<String> userTableView;
    @FXML
    private TableColumn<String, String> usernameColumn;
    private StockExchangeManager stockExchangeManager;

    @FXML
    public void initialize() {
        // Get the list of usernames from UserManager
        List<String> usernames = stockExchangeManager.getUsers();

        // Populate the TableView
        for (String username : usernames) {
            userTableView.getItems().add(username);
        }

        // Set up column to display usernames
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    }
}
