package com.nft.marketplace.view;

import com.nft.marketplace.controller.ViewController;
import com.nft.marketplace.model.user.UserInputStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * View that focus on the NFT's view (add,remove,see)
 */
public class ViewApp extends Application {

    private UserInputStorage LoginResult;

    public ViewApp(UserInputStorage loginResult) {
        LoginResult = loginResult;
    }

    /**
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ViewApp.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        LoadController(fxmlLoader);
        stage.setTitle("Certificator songs DEMO!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load the user data into the controller
     * @param fxmlLoader
     */
    private void LoadController(FXMLLoader fxmlLoader) {
        ViewController mainController = fxmlLoader.getController();
        mainController.fetchLoginData(LoginResult);
        mainController.init();
    }

}