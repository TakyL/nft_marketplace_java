package com.nft.marketplace.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Modal {

    public void launch(String titlePopUp,String message)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(titlePopUp);

        dialog.setResizable(false);

        // Create a label to display the message
        Label label = new Label(message);

        // Create a layout and add the label to it
        VBox layout = new VBox(10);
        layout.getChildren().add(label);

        // Create a scene and add the layout to it
        Scene scene = new Scene(layout);

        // Set the scene and show the dialog
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
