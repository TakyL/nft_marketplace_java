package com.nft.marketplace.controller;

import com.nft.marketplace.model.contract.Market;
import com.nft.marketplace.model.contract.Message;
import com.nft.marketplace.model.user.MarketHandler;
import com.nft.marketplace.model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.w3c.dom.Text;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.crypto.exception.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
public class MainController  implements Initializable {
    @FXML
    private TextField wallet_value;

    @FXML
    private TextField nft_entry;

    @FXML
    private ImageView POL_logo;

    @FXML
    private TextField song_title;
    private Web3j web;

    private User currentUser;

    private MarketHandler marketHandler;

    @FXML
    private Button btn_ajout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //this.web = Web3j.build(new HttpService("https://polygon-amoy.drpc.org"));
            this.web = Web3j.build(new HttpService("wss://polygon-amoy-bor-rpc.publicnode.com"));
            String walletAddress ="0xEfc1Efb0F31426D88eaCF9D23Aa0233caebA6bCb";
            LoadWallet(walletAddress);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void LoadWallet(String walletAddress)
    {
        this.currentUser = new User(walletAddress,web);
        UpdateWalletValue();
        LoadContracts();
        UpdateWalletValue();
        UpdateNFTNumbers();
    }

    private void UpdateWalletValue()
    {
        this.wallet_value.setText(currentUser.getPOLvalue().toString());
    }


    private void LoadContracts()
    {
        this.marketHandler=new MarketHandler(currentUser,web);
    }
    @FXML
    private void ajout()
    {
        String title = fetch_title();
        if(title.length()!=0)
        {
            marketHandler.addNFT(title);
        }
    }

    private String fetch_title()
    {
        return song_title.getText().trim();
    }

    private void UpdateNFTNumbers()
    {
        nft_entry.setText(String.valueOf(marketHandler.getNumberOfNFT()));
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