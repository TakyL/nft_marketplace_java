package com.nft.marketplace.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main that the user must run (login page)
 * Run the app
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewApp.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 310, 120);
        stage.setTitle("Certificator songs login!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
