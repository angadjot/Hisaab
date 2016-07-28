package com.tech.petabyteboy.hisaab.Models;

/**
 * Created by petabyteboy on 24/07/16.
 */
public class HomeDuesModel {

    private String amount;
    private String payType;
    private String userID;

    public HomeDuesModel(){
        amount = "";
        payType = "";
        userID = "";
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
