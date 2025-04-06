package com.nft.marketplace.controller;

import com.nft.marketplace.model.user.UserInputStorage;
import com.nft.marketplace.view.ViewApp;
import com.nft.marketplace.view.Modal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.web3j.crypto.WalletUtils.isValidAddress;
import static org.web3j.crypto.WalletUtils.isValidPrivateKey;

public class LoginController implements Initializable {

    @FXML
    private Label lbl_err;

    @FXML
    private Button btn_login;

    @FXML
    private TextField address_input;

    @FXML
    private TextField key_input;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_login.setDisable(true);
    }
        @FXML
    private void login()
    {
        if(!checkValidAdress())
        {
            lbl_err.setText("Address not valid !");
        }

        if(!checkPrivateKey())
        {
            lbl_err.setText("Key not valid !");
        }

        if(checkPrivateKey() && checkValidAdress())
        {
            closeView();
            runMainView();
        }

    }

    private boolean checkValidAdress()
    {
        return isValidAddress(address_input.getText().trim());
    }

    private boolean checkPrivateKey()
    {
        return isValidPrivateKey(key_input.getText().trim());
    }

    @FXML
    private void inputKeyPressed()
    {
        if(getTextFromTextField(address_input).length() != 0 && getTextFromTextField(key_input).length() != 0)
        {
            btn_login.setDisable(false);
        }
        else btn_login.setDisable(true);
    }

    private String getTextFromTextField(TextField txtField)
    {
        return txtField.getText().trim();
    }

    private void closeView()
    {
        Stage stage = (Stage) btn_login.getScene().getWindow();
        stage.close();
    }

    private void runMainView()
    {
        try {
            new ViewApp(new UserInputStorage(getTextFromTextField(address_input),getTextFromTextField(key_input))).start(new Stage());
        } catch (IOException e) {
            new Modal().launch("Unexcepted error",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
