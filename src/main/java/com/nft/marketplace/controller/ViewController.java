package com.nft.marketplace.controller;

import com.nft.marketplace.model.user.MarketHandler;
import com.nft.marketplace.model.user.User;
import com.nft.marketplace.model.user.UserInputStorage;
import com.nft.marketplace.view.Modal;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;


public class ViewController {
    @FXML
    private TextField wallet_value;//Display the value of wallet
    @FXML
    private TextField nft_entry; //Display the number of the nft

    @FXML
    private ImageView POL_logo;

    @FXML
    private TextField song_title;//Input area used for add a new song

    @FXML
    private Label lbl_pending;//Pending label
    @FXML
    private GridPane nft_list;//Grid that contains all song owned
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


    /**
     * Load user's data and web3j connection service
     */
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

    /**
     * Load the user wallet and handler's contrat
     */
    private void LoadWallet()
    {
        UpdateWalletValue();
        LoadContracts();
    }

    /**
     * Update UI user wallet in POL
     */
    private void UpdateWalletValue()
    {
        currentUser.updateUserWallet(web);
        this.wallet_value.setText(currentUser.getPOLvalue().toString());
    }

    /**
     * Load the Handler for the transaction for the blockchain
     */
    private void LoadContracts()
    {
        this.marketHandler=new MarketHandler(currentUser,web);
    }

    /**
     * Click of the btn Add
     */
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

    /**
     * Click of the btn delete
     */
    @FXML
    private void delete()
    {
        if(clickedValue.trim().length() != 0)
        {
            marketHandler.removeNFT(clickedValue,()->updateAppStatut());
            runPending();
        }
    }

    /**
     * Click for the grid that display nft's title
     */
    @FXML
    public void gridclick()
    {
        removeStyle(this.nft_list);
    }

    /**
     * Action fired onKeyPressed for song input textfield
     * Make the button clicable when the textfield is not empty
     */
    @FXML
    private void input_song()
    {
        this.btn_ajout.setDisable(song_title.getText().trim().length() == 0);
    }

    /**
     * Return the title of the song that we want to add
     *
     * @return TextField value trimmed : i.e bonjour, numb
     */
    private String fetch_title()
    {
        return song_title.getText().trim();
    }

    /**
     * Update UI when adding or removing a NFT
     * Empty the textfield and refresh the grid that display the list of nft owned
     */
    private void updateAppStatut()
    {
        this.song_title.setText("");
        this.lbl_pending.setText("");
        updateBtnStatut(btn_ajout,true);//Guarantee that btn add not clickable
        song_title.setDisable(false);//Garantee that textfield is editable
        UpdateWalletValue();
        fillGrid();
    }

    /**
     *  Update NFT owned by the user and the UI "BBL's owns"
     */
    private void UpdateNFTNumbers()
    {
        List<Utf8String> addressNfts =marketHandler.getListOfNFT();
        nft_entry.setText(String.valueOf(addressNfts.size())); //Update UI TextField
        currentUser.setOwnedNFT(addressNfts);
    }

    /**
     * Setup default interaction when view is launched
     */
    private void initUIProprities()
    {
        this.nft_entry.setDisable(true);
        this.wallet_value.setDisable(true);
        updateBtnStatut(this.btn_ajout,true);
        updateBtnStatut(this.btn_delete,true);
    }

    /**
     * Update the Disabled of a Button
     * @param btn The updated button
     * @param statut The updated statut : true for not clickable by user, false for clickable by user
     */
    private void updateBtnStatut(Button btn, boolean statut)
    {
        btn.setDisable(statut);
    }

    /**
     * Update the UI for the pending is fired
     * Triggers when a transaction is pending
     * Make all buttons not clicable and notify the user the transaction is pending
     */
    private void runPending()
    {
        updateBtnStatut(btn_ajout,true);
        updateBtnStatut(btn_delete,true);
        song_title.setDisable(true);//TextField unmodifiable by user
        printPendingMsg();
        disabledAllBtn();//User can't click on others nft's because a transaction is pending
    }

    /**
     * Refresh and print the nft of the user
     */
    private void fillGrid() {
        UpdateNFTNumbers();
        clearBtnGrid(nft_list);
        createBtnGrid();
    }

    /**
     * Create all the button for the grid
     * Each button represent a title and is clickable
     */
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

    /**
     * Remove the clicked button style assuring that only button is the last button clicked by the user
     * @param pane
     */
    private void removeStyle(Pane pane) {
        for (Node child : pane.getChildren()) {
            if (child instanceof Button) {
                Button button = (Button) child;

                // Remove other btn that have the clicked btn style
                if (button.getStyle().equals("-fx-background-color: #B0B0B0;") && (button.getStyle().contains("-fx-background-color: #B0B0B0;"))) {
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

    /**
     * Remove all buttons from the grid
     * @param pane : Grid
     */
    private void clearBtnGrid(Pane pane)
    {
        pane.getChildren().clear();
    }

    /**
     * Disabled all buttons of the title's song grid
     */
    private void disabledAllBtn()
    {
        for (Node node : nft_list.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
    }
}