package com.nft.marketplace.model.user;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.web3j.crypto.WalletUtils.isValidAddress;

public class User {

    private String Address;
    private BigDecimal polWallet;

    public User(String address, Web3j web3j) {
        if(isValidAddress(address))
        {
            Address = address;
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
            balanceResponse = webEntry.ethGetBalance(this.Address,
                            org.web3j.protocol.core.DefaultBlockParameterName.LATEST)
                    .send();
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

    protected String getAddress()
    {
        return Address;
    }
}
