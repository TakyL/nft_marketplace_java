package com.nft.marketplace.model.user;

import com.nft.marketplace.model.contract.Market;
import com.nft.marketplace.model.contract.NFT;
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
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class MarketHandler {

    private Credentials userCrendentials;

    private Web3j web3jCon;

    private final String MARKET_ADR = "0xBC3911AbCe626aBF7389c8781aC2b3f1D71DD257";

    private final String NFT_ADR = "0xA7F2Be4e39Cb23F8a60a4E0C5408CA2570dC405d";
    private Market market;

    private NFT nft;
    ///!\ Problem when trying to call FeeInWei function from Contract's class
    private final BigInteger FEE;

    private Modal pendingModal;
    public MarketHandler(User user,Web3j web3j)
    {   if(user == null && web3j == null)
    {
        throw new IllegalArgumentException("User or connection null");
    }
        this.userCrendentials = Credentials.create(user.getKey());
        this.web3jCon =web3j;
        this.ContractMarketLoad();
        this.FEE=Convert.toWei(new BigDecimal("0.05"), Convert.Unit.GWEI).toBigInteger();
    }

    private void ContractMarketLoad()
    {
        DefaultGasProvider gasProvider = new DefaultGasProvider();
        long chainId = 80002;
        RawTransactionManager txManager = new RawTransactionManager(web3jCon, userCrendentials, chainId);
        BigInteger gasPrice = BigInteger.valueOf(100000000000L); // Set gas price (example: 25 gwei or 25 * 10^9)
        BigInteger gasLimit = BigInteger.valueOf(500000);

        this.market = Market.load(MARKET_ADR,web3jCon, txManager,new StaticGasProvider(gasPrice, gasLimit));
        this.nft = NFT.load(NFT_ADR, web3jCon, userCrendentials,gasProvider);

    }

    public void addNFT(String songtitle)
    {
        try {
            RemoteFunctionCall<TransactionReceipt> remoteFunctionCall = market.addNFT(songtitle,FEE);
            handleTransaction(remoteFunctionCall);
        } catch (Exception e) {
            new Modal().launch("Error during the adding action ", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }

    public void removeNFT(String songtitle)
    {
        try {
            RemoteFunctionCall<TransactionReceipt> remoteFunctionCall  = market.removeNFT(songtitle);
            handleTransaction(remoteFunctionCall);
        } catch (Exception e) {
            new Modal().launch("Error during remove's action", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }

    public List getListOfNFT()
    {
        try {
            return test();
        } catch (Exception e) {
            new Modal().launch("Error when fetching the list", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }

    private void outputTransactionHash(TransactionReceipt receipt)
    {
        System.out.println("Action effectue : "+ receipt.getTransactionHash());
    }

    private void openPendingModal()
    {
        this.pendingModal=new Modal();
        this.pendingModal.launch("Pending","Transaction Pending, please wait !");
    }

    private void handleTransaction(RemoteFunctionCall<TransactionReceipt> rFC) {
        openPendingModal();
        rFC.sendAsync().thenAccept(receipt -> {
            Platform.runLater(() -> {
                pendingModal.updateModal("Transaction done ! ","Transaction completed with hash: " + receipt.getTransactionHash());
                pendingModal.close();
                outputTransactionHash(receipt);
            });
        });
    }

    private List<Utf8String> test()
    {
        Function function = new Function(
                "getMyNFTs",
                Collections.emptyList(), // No parameters
                Collections.singletonList(new TypeReference<DynamicArray<Utf8String>>() {}) // Return type
        );
        String encodedFunction = FunctionEncoder.encode(function);

        try {
            EthCall response = web3jCon.ethCall(
                    Transaction.createEthCallTransaction(userCrendentials.getAddress(), market.getContractAddress(), encodedFunction),
                    DefaultBlockParameterName.LATEST
            ).send();
            List<Type> decoded = FunctionReturnDecoder.decode(response.getResult(), function.getOutputParameters());
            List<Utf8String> nftList = (List<Utf8String>) decoded.get(0).getValue();

/*            for (Utf8String nft : nftList) {
                System.out.println(nft.getValue());
            }*/
            return nftList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
