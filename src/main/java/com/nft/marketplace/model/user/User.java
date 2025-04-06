package com.nft.marketplace.model.user;

import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.web3j.crypto.WalletUtils.isValidAddress;

/**
 * Represent the connected user
 */
public class User {

    private String address;
    //The user's wallet value in POL
    private BigDecimal polWallet;

    private String key;
    //List of title owned by the user
    private List<Utf8String> ownedNFT=new ArrayList<>();

    public User(UserInputStorage inputStorage, Web3j web3j) {
        if(isValidAddress(inputStorage.getAddress()))
        {
            this.address = inputStorage.getAddress();
            this.key = inputStorage.getKey();
            this.loadWallet(web3j);
        }else
        {
            throw new IllegalArgumentException("Address not valid");
        }
    }

    private void loadWallet(Web3j webEntry)
    {
        EthGetBalance balanceResponse = null;
        try {
            balanceResponse = webEntry.ethGetBalance(this.address,
                            org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BigInteger wei = balanceResponse.getBalance();
        this.polWallet =  Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
    }

    public BigDecimal getPOLvalue()
    {
        return polWallet;
    }

    public String getAddress()
    {
        return address;
    }

    public String getKey(){return  key;}

    public List<Utf8String> getOwnedNFT(){return ownedNFT;}

    public void setOwnedNFT(List<Utf8String> ownedNFT) {
        this.ownedNFT = ownedNFT;
    }
}
