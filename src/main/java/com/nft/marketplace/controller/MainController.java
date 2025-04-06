package com.nft.marketplace.controller;

import com.nft.marketplace.model.user.MarketHandler;
import com.nft.marketplace.model.user.User;
import com.nft.marketplace.model.user.UserInputStorage;
import com.nft.marketplace.view.Modal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class MainController {
    @FXML
    private TextField wallet_value;

    @FXML
    private TextField nft_entry;

    @FXML
    private ImageView POL_logo;

    @FXML
    private TextField song_title;

    @FXML
    private Label lbl_pending;
    @FXML
    private GridPane nft_list;
    private Web3j web;

    private User currentUser;

    private String clickedValue;//Value of the title song to delete

    private MarketHandler marketHandler;

    @FXML
    private Button btn_ajout;

    @FXML
    private Button btn_delete;

    private UserInputStorage userInputStorage;

    public void fetchLoginData(UserInputStorage data)
    {
        this.userInputStorage = data;
    }



    public void init()
    {
        try {
            //this.web = Web3j.build(new HttpService("https://polygon-amoy.drpc.org"));
            this.web = Web3j.build(new HttpService("wss://polygon-amoy-bor-rpc.publicnode.com"));
            this.currentUser = new User(userInputStorage,web);
            LoadWallet();
            UpdateNFTNumbers();
            initUIProprities();
            fillGrid();

        } catch (Exception e) {
            new Modal().launch("Error", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }


    private void LoadWallet()
    {
        UpdateWalletValue();
        LoadContracts();
        UpdateWalletValue();
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
            marketHandler.addNFT(title,()->updateAppStatut());
            runPending();
        }
    }

    @FXML
    private void delete()
    {
        if(clickedValue.trim().length() != 0)
        {
            marketHandler.removeNFT(clickedValue,()->updateAppStatut());
            runPending();
        }
    }

    @FXML
    public void gridclick()
    {
        removeStyle(this.nft_list);
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

    private void updateAppStatut()
    {
        this.song_title.setText("");
        this.lbl_pending.setText("");
        updateBtnStatut(btn_ajout,true);
        UpdateWalletValue();
        fillGrid();
    }

    private void UpdateNFTNumbers()
    {
        List<Utf8String> addressNfts =marketHandler.getListOfNFT();
        nft_entry.setText(String.valueOf(addressNfts.size()));
        currentUser.setOwnedNFT(addressNfts);
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

    private void runPending()
    {
        updateBtnStatut(btn_ajout,true);
        updateBtnStatut(btn_delete,true);
        printPendingMsg();
        disabledAllBtn();
    }
    private void fillGrid() {
        UpdateNFTNumbers();
        clearBtnGrid(nft_list);
        createBtnGrid();
    }

    private void createBtnGrid () {
        List<Utf8String> items = currentUser.getOwnedNFT();
        for (int i = 0; i < items.size(); i++) {
            int row = i / 2;
            int col = i % 2;

            String value = String.valueOf(items.get(i));
            Button text = new Button(value);
            text.setOnMouseClicked(event -> {
                removeStyle(nft_list);
                text.setStyle("-fx-background-color: #B0B0B0;");  // Darker shade when pressed
                updateBtnStatut(btn_delete,false);
                this.clickedValue=value;
            });

            this.nft_list.add(text, col, row); // (child, columnIndex, rowIndex)
        }
    }

    private void removeStyle(Pane pane) {
        for (Node child : pane.getChildren()) {
            // Check if the child is a Button (or other node type that has style)
            if (child instanceof Button) {
                Button button = (Button) child;

                // Check if the button has the specific background color style
                if (button.getStyle().equals("-fx-background-color: #B0B0B0;") && (button.getStyle().contains("-fx-background-color: #B0B0B0;"))) {
                        // Remove the specific background color style by resetting it
                        button.setStyle(button.getStyle().replace("-fx-background-color: #B0B0B0;", ""));
                        updateBtnStatut(btn_delete,true);
                }
            }
        }
    }

    private void printPendingMsg()
    {
        this.lbl_pending.setText("Pending ...");
    }

    private void clearBtnGrid(Pane pane)
    {
        pane.getChildren().clear();
    }

    private void disabledAllBtn()
    {
        for (Node node : nft_list.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }    }
}