package com.nft.marketplace.view;

import com.nft.marketplace.controller.MainController;
import com.nft.marketplace.model.user.UserInputStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private UserInputStorage LoginResult;

    public MainApplication(UserInputStorage loginResult) {
        LoginResult = loginResult;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        LoadController(fxmlLoader);
        stage.setTitle("Certificator songs DEMO!");
        stage.setScene(scene);
        stage.show();
    }

    private void LoadController(FXMLLoader fxmlLoader) {
        MainController mainController = fxmlLoader.getController();
        mainController.fetchLoginData(LoginResult);
        mainController.init();
    }

    public static void main(String[] args) {
        launch();
    }
}