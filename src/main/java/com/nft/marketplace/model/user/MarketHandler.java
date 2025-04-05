package com.nft.marketplace.model.user;

import com.nft.marketplace.model.contract.Market;
import com.nft.marketplace.model.contract.NFT;
import com.nft.marketplace.view.Modal;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class MarketHandler {

    private User userAddress;

    private Web3j web3jCon;

    private final String PRIVATE_KEY="0x7027ae27ebaf9370b5711911db1d9f27042a65be19aea0d95c5738e40f5ad787";
    private final String MARKET_ADR = "0xeCa17224556dFE004b0a5db8921a48cF2edd0d82";

    private final String NFT_ADR = "0xC4a0558b821D58E6aE98e091aB68E41D0fa86E9C";
    private Market market;

    private NFT nft;
    ///!\ Problem when trying to call FeeInWei function from Contract's class
    private final BigInteger FEE;
    public MarketHandler(User user,Web3j web3j)
    {   if(user == null && web3j == null)
    {
        throw new IllegalArgumentException("User or connection null");
    }
        this.userAddress=user;
        this.web3jCon =web3j;
        this.ContractMarketLoad();
        this.FEE=Convert.toWei(new BigDecimal("0.05"), Convert.Unit.GWEI).toBigInteger();
    }

    private void ContractMarketLoad()
    {
        Credentials credentials = Credentials.create(PRIVATE_KEY);
        DefaultGasProvider gasProvider = new DefaultGasProvider();
        long chainId = 80002;
        RawTransactionManager txManager = new RawTransactionManager(web3jCon, credentials, chainId);
        BigInteger gasPrice = BigInteger.valueOf(25000000000L); // Set gas price (example: 25 gwei or 25 * 10^9)
        BigInteger gasLimit = BigInteger.valueOf(500000);

        this.market = Market.load(MARKET_ADR,web3jCon, txManager,new StaticGasProvider(gasPrice, gasLimit));
        this.nft = NFT.load(NFT_ADR, web3jCon, credentials,gasProvider);

    }

    public void addNFT(String songtitle)
    {
        try {
            TransactionReceipt receipt = market.addNFT(songtitle,FEE).send();
            outputTransactionHash(receipt);
        } catch (Exception e) {
            new Modal().launch("Error during the adding action ", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }

    public void removeNFT(String songtitle)
    {
        try {
            TransactionReceipt receipt = market.removeNFT(songtitle).send();
            outputTransactionHash(receipt);
        } catch (Exception e) {
            new Modal().launch("Error during remove's action", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }

    public int getNumberOfNFT()
    {
        try {
            List a = market.getMyNFTs().send();
            return a.size();
        } catch (Exception e) {
            new Modal().launch("Error when fetching the list", e.getMessage().trim());
            throw new RuntimeException(e);
        }
    }

    private void outputTransactionHash(TransactionReceipt receipt)
    {
        System.out.println("Action effectue : "+ receipt.getTransactionHash());
    }

}
