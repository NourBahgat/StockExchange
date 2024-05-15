package com.example.demo2.Controllers.Admin;
import com.example.demo2.Admin;
import com.example.demo2.StockExchangeManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.List;

import static com.example.demo2.Admin.searchUserInCSV;


public class ManageUsers {
    @FXML
    private TableView<String> userTableView;
    @FXML
    private TableColumn<String, String> usernameColumn;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField creditTextField;
    private StockExchangeManager stockExchangeManager;

    @FXML
    public void initialize() {
        // Get the list of usernames from UserManager
        List<String> usernames = stockExchangeManager.getUsers();

        // Populate the TableView
        for (String username : usernames) {
            userTableView.getItems().add(username);
        }

        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String selectedUsername = newVal;
                String[] userData = searchUserInCSV(selectedUsername);
                if (userData != null && userData.length == 2) {
                    usernameTextField.setText(newVal);
                    passwordTextField.setText(userData[0]);
                    creditTextField.setText(userData[1]);
                }
            }
        });
    }
    public void handleDeleteButton(ActionEvent event) throws IOException {
        String selected = userTableView.getSelectionModel().selectedItemProperty().getValue();
        Admin delete = new Admin();
        delete.removeUser(selected);
        System.out.println(selected);
        StockExchangeManager.updateUsersFromCSV("users.csv");
        userTableView.getItems().clear();
        List<String> updatedUsernames = stockExchangeManager.getUsers();
        for (String username : updatedUsernames) {
            userTableView.getItems().add(username);
        }
    }
}

