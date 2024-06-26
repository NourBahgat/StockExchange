package com.example.demo2.Controllers.Admin;
import com.example.demo2.Admin;
import com.example.demo2.App;
import com.example.demo2.StockExchangeManager;
import com.example.demo2.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.example.demo2.Admin.searchUserInCSV;
import static com.example.demo2.StockExchangeManager.getUserFromUsername;
import static com.example.demo2.StockExchangeManager.users;


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
    @FXML
    private TextField type;
    private StockExchangeManager stockExchangeManager;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize() {
        userTableView.getItems().clear();
        stockExchangeManager = App.manager;
        // Get the list of usernames from UserManager
        List<User> users = stockExchangeManager.getUsers();
        // Populate the TableView
        for (User user : users) {
            userTableView.getItems().add(user.getUsername());
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
        stockExchangeManager.removeUser(getUserFromUsername(selected));
        userTableView.refresh();
        userTableView.getItems().clear();
        List<User> updatedUsernames = stockExchangeManager.getUsers();
        for (User user : updatedUsernames) {
            userTableView.getItems().add(user.getUsername());
        }
    }

    @FXML
    private void handleAddUserButton() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String creditText = creditTextField.getText();
        double credit = 0.0; // Initialize with a default value

        try {
            credit = Double.parseDouble(creditText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid credit value!", "Please enter a valid numerical value for credit.");
            return; // Exit the method early
        }
        String typeUser=type.getText();

        Admin admin = new Admin();
        StockExchangeManager userAdd = new StockExchangeManager();
        if (typeUser.equals("Standard")) {
            admin.addUser(username, password, credit,false );
            userTableView.getItems().add(username);
            User newUser = new User(username, password, credit, 0,false);
            userAdd.addUser(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!", null);
        } else if (typeUser.equals("Premium")) {
            admin.addUser(username, password, credit,true );
            userTableView.getItems().add(username);

            User newUser = new User(username, password, credit, 0,true);
            userAdd.addUser(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!", null);
        }
        else
        { showAlert(Alert.AlertType.INFORMATION, "Warning", "Please enter a valid type", null);}
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message, String headerText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void BackToMainAdmin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Admin/AdminMain.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);

    }

}

