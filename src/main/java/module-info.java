module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
    exports com.example.demo2.Controllers;
    exports com.example.demo2.Controllers.Admin;
    exports com.example.demo2.Controllers.User;
    exports com.example.demo2.Controllers.PremiumUserController;
    exports com.example.demo2.Models;
    exports com.example.demo2.Views;
}