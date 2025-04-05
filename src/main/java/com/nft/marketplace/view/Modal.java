package com.nft.marketplace.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Modal {

    private Stage stage;
    private Scene scene;
    private Label lbl_msg;
    public Modal() {
        this.stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
    }

    public void launch(String titlePopUp, String message)
    {
        Stage dialog = stage;
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(titlePopUp);

        this.lbl_msg = new Label(message);

        VBox layout = new VBox(10);
        layout.getChildren().add(lbl_msg);

        this.scene = new Scene(layout);

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public void updateModal(String modalTitle, String msg)
    {
        stage.setTitle(modalTitle);
        lbl_msg.setText(msg);
    }

    public void close()
    {
        stage.close();
    }
}
