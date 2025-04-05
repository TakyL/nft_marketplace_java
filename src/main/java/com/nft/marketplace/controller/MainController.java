package com.nft.marketplace.controller;

import com.nft.marketplace.model.user.MarketHandler;
import com.nft.marketplace.model.user.User;
import com.nft.marketplace.view.Modal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class MainController  implements Initializable {
    @FXML
    private TextField wallet_value;

    @FXML
    private TextField nft_entry;

    @FXML
    private ImageView POL_logo;

    @FXML
    private TextField song_title;
    @FXML
    private GridPane nft_list;
    private Web3j web;

    private User currentUser;

    private MarketHandler marketHandler;

    @FXML
    private Button btn_ajout;

    @FXML
    private Button btn_delete;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //this.web = Web3j.build(new HttpService("https://polygon-amoy.drpc.org"));
            this.web = Web3j.build(new HttpService("wss://polygon-amoy-bor-rpc.publicnode.com"));
            String walletAddress ="0xEfc1Efb0F31426D88eaCF9D23Aa0233caebA6bCb";
            LoadWallet(walletAddress);
        } catch (Exception e) {
             new Modal().launch("Error", e.getMessage().trim());
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
        initUIProprities();
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

    @FXML
    private void delete()
    {

    }

    @FXML
    private void gridclick()
    {
        //Action du gridpane qui trigger delete()
    }

    @FXML
    private void input_song()
    {
        this.btn_ajout.setDisable(song_title.getText().trim().length() == 0);
    }
    private String fetch_title()
    {
        return song_title.getText().trim();
    }

    private void UpdateNFTNumbers()
    {
        nft_entry.setText(String.valueOf(marketHandler.getNumberOfNFT()));
    }

    private void initUIProprities()
    {
        this.nft_entry.setDisable(true);
        this.wallet_value.setDisable(true);
        updateBtnStatut(this.btn_ajout,true);
        updateBtnStatut(this.btn_delete,true);
    }

    private void updateBtnStatut(Button btn, boolean statut)
    {
        btn.setDisable(statut);
    }
}