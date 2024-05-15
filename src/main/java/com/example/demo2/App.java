
package com.example.demo2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/FXML/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Stock exchange");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message dialog)
        }

    }

    public static void main(String[] args) {
        launch();
    }
}
