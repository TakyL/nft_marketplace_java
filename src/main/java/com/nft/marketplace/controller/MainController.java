package com.nft.marketplace.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
public class MainController  implements Initializable {
    @FXML
    private Label welcomeText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            Web3j web3 = Web3j.build(new HttpService("https://polygon-amoy.drpc.org"));

            java.math.BigInteger blockNumber = web3.ethBlockNumber().send().getBlockNumber();

            System.out.println("Latest Ethereum block number: " + blockNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

    }



    /**
     * Loading a Smart Contract¶
     *
     * If you have already deployed a contract and would like to interact with it through web3j then the Java Wrappers or your smart contract have a load method.
     *
     *  Web3j web3j = Web3j.build(new HttpService("<your_node_url>"));
     *  String greeting;
     *  HelloWorld helloWorld = HelloWorld.load("your_contract_address", web3j, Credentials.create("your_private_key"), new DefaultGasProvider());
     *  if (helloWorld.isValid()) {
     *     greeting = helloWorld.greeting().send();
     *  }
     *  web3j.shutdown();
     *
     * It is important that the loaded contract is checked using the isValid() method. This method will return false if the contract's bytecode does not match with the deployed one.
     */
}