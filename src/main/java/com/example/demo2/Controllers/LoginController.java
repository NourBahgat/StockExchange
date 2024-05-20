package com.example.demo2.Controllers;


import java.io.IOException;
import java.util.Objects;

import com.example.demo2.StockExchangeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {

    private boolean adminPageOpened = false;
    private Stage adminStage;

    public void switchToAdmin(ActionEvent event) throws IOException {
        if (!adminPageOpened || adminStage == null) {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Admin/Admin.fxml"));
            adminStage = new Stage();
            adminStage.setScene(new Scene(root));
            adminStage.setTitle("admin");
            adminStage.setOnCloseRequest(event1 -> adminPageOpened = false);
            adminStage.show();
            adminPageOpened = true;
        } else {
            adminStage.toFront();
        }
    }

    public void switchToSignIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/StandardUser/SignIn.fxml"));
        Stage signInStage = new Stage();
        signInStage.setTitle("Sign In");
        signInStage.setScene(new Scene(root));
        signInStage.show();
    }
}
