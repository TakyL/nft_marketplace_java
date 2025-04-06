package com.nft.marketplace.model.user;

import com.nft.marketplace.model.contract.Market;
import com.nft.marketplace.view.Modal;
import javafx.application.Platform;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * Class that runs the market command to blockchain
 */
public class MarketHandler {

    private Credentials userCrendentials;

    private Web3j web3jCon;

    private final String MARKET_ADR = "0xBC3911AbCe626aBF7389c8781aC2b3f1D71DD257";

    private Market market;

    ///!\ Problem when trying to call FeeInWei function from Contract's class
    private final BigInteger FEE;

    private Modal pendingModal;

    public MarketHandler(User user, Web3j web3j) {
        if (user == null && web3j == null) {
            throw new IllegalArgumentException("User or connection null");
        }
        this.userCrendentials = Credentials.create(user.getKey());
        this.web3jCon = web3j;
        this.ContractMarketLoad();
        this.FEE = Convert.toWei(new BigDecimal("0.05"), Convert.Unit.GWEI).toBigInteger();
    }

    /**
     *  Load the market class for the transactions
     */
    private void ContractMarketLoad() {
        RawTransactionManager txManager = new RawTransactionManager(web3jCon, userCrendentials, 80002);
        BigInteger gasPrice = BigInteger.valueOf(100000000000L); // Value : 100gwei
        BigInteger gasLimit = BigInteger.valueOf(500000);

        this.market = Market.load(MARKET_ADR, web3jCon, txManager, new StaticGasProvider(gasPrice, gasLimit));
    }

    /**
     * Run the market addNFT function
     * @param songtitle : Title of the song
     * @param runnable : The callback used for updating UI components
     */
    public void addNFT(String songtitle, Runnable runnable) {
        try {
            RemoteFunctionCall<TransactionReceipt> remoteFunctionCall = market.addNFT(songtitle, FEE);
            handleTransaction(remoteFunctionCall, runnable);
        } catch (Exception e) {
            new Modal().launch("Error during the adding action ", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }
    /**
     * Run the market removeNFT function
     * @param songtitle : Title of the song
     * @param runnable : The callback used for updating UI components
     */
    public void removeNFT(String songtitle, Runnable runnable) {
        try {
            RemoteFunctionCall<TransactionReceipt> remoteFunctionCall = market.removeNFT(songtitle);
            handleTransaction(remoteFunctionCall, runnable);
        } catch (Exception e) {
            new Modal().launch("Error during remove's action", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetch the list of the nft that the user has
     * @return List of string of song, may be empty if user has no nft
     */
    public List getListOfNFT() {
        try {
            return fetchNFTandDecode();
        } catch (Exception e) {
            new Modal().launch("Error when fetching the list", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }

    /**
     * Output to the console
     * @param receipt : Transaction
     */
    private void outputTransactionHash(TransactionReceipt receipt) {
        System.out.println("Action effectue : " + receipt.getTransactionHash());
    }

    /**
     * Open a pending modal
     */
    private void openPendingModal() {
        this.pendingModal = new Modal();
        this.pendingModal.launch("Pending", "Transaction Pending, please wait !");
    }

    /**
     * Run the transaction async
     * If not async, high chance of craches due to the fact that javafx run on one thread, will satured the thread and so freeze the app
     * @param rFC : The Transaction that we are calling
     * @param callback : Run callback operations when the transaction is finished
     */
    private void handleTransaction(RemoteFunctionCall<TransactionReceipt> rFC, Runnable callback) {
        rFC.sendAsync().thenAccept(receipt -> {
            Platform.runLater(() -> {
                //openPendingModal();
                //pendingModal.updateModal("Transaction done ! ","Transaction completed with hash: " + receipt.getTransactionHash());
                //pendingModal.close();
                outputTransactionHash(receipt);

                if (callback != null) callback.run();
            });
        });
    }

    /**
     * Fetch the NFT of the user
     * Run the contract transaction getMyNFTs without using the contract's class
     * This is due the getMyNFTs's return is encoded in bytes, so this function handles the decode part
     * @return List of string in utf8
     */
    private List<Utf8String> fetchNFTandDecode() {
        Function function = new Function(
                "getMyNFTs",
                Collections.emptyList(), // No parameters
                Collections.singletonList(new TypeReference<DynamicArray<Utf8String>>() {
                })
        );
        String encodedFunction = FunctionEncoder.encode(function);

        try {
            EthCall response = web3jCon.ethCall(
                    Transaction.createEthCallTransaction(userCrendentials.getAddress(), market.getContractAddress(), encodedFunction),
                    DefaultBlockParameterName.LATEST
            ).send();
            List<Type> decoded = FunctionReturnDecoder.decode(response.getResult(), function.getOutputParameters());
            List<Utf8String> nftList = (List<Utf8String>) decoded.get(0).getValue();

            return nftList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
