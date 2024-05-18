
package com.example.demo2;

import com.example.demo2.Controllers.User.MarketPerformanceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static StockExchangeManager manager = null;
    @Override
    public void start(Stage stage)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/FXML/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            manager = new StockExchangeManager();
            StockExchangeManager.loadSystem();
            stage.setTitle("Stock exchange");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message dialog)
        }

    }
    public void start2(Stage primaryStage) {
        Stock stock = new Stock("AAPL", 150.0, 155.0, 100, 0.0);

        stock.updatePrice(160.0);
        stock.updatePrice(162.0);
        stock.updatePrice(158.0);

        MarketPerformanceController.showStockPriceChart(primaryStage, stock);
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized(manager) {
                try {
                    if (manager != null) {
                        StockExchangeManager.saveSystem();
                    }
                } catch (Exception e) {

                }
            }
        }));

        launch();
    }
}
