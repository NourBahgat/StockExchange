package com.example.demo2.Controllers;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private boolean adminPageOpened = false;
    private Stage adminStage; // Declare adminStage at class level

    public void switchToAdmin(ActionEvent event) throws IOException {
        // Check if the admin page is already opened
        if (!adminPageOpened || adminStage == null) {
            // Load the admin screen
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Admin/Admin.fxml"));

            // Create a new stage for the admin screen
            adminStage = new Stage();
            adminStage.setScene(new Scene(root));
            // Add a listener to handle the admin stage close event
            adminStage.setTitle("admin");
            adminStage.setOnCloseRequest(event1 -> adminPageOpened = false);
            // Show the admin stage
            adminStage.show();
            // Set the flag to true indicating that the admin page is opened
            adminPageOpened = true;
        } else {
            // If admin page is already opened, bring it to front
            adminStage.toFront();
        }
    }

    public void switchToSignIn(ActionEvent event) throws IOException {
        // Load the sign-in or sign-up screen based on your logic
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/StandardUser/SignIn.fxml"));

        // Create a new stage for the sign-in screen
        Stage signInStage = new Stage();
        signInStage.setTitle("Sign In");
        signInStage.setScene(new Scene(root));
        signInStage.show();
    }
}
