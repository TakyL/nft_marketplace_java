package com.nft.marketplace.model.user;

/**
 * Store the user input from login page
 */
public class UserInputStorage {

    private String address;
    private String key;

    public UserInputStorage(String address, String key) {
        this.address = address;
        this.key = key;
    }

    public String getAddress() {
        return address;
    }

    public String getKey() {
        return key;
    }
}
