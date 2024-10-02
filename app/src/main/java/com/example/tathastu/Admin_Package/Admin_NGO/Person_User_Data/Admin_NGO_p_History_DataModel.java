package com.example.tathastu.Admin_Package.Admin_NGO.Person_User_Data;

// EventModel.java

public class Admin_NGO_p_History_DataModel {

    private String receivedFrom;
    private String amount;
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public Admin_NGO_p_History_DataModel() {
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Admin_NGO_p_History_DataModel(String receivedFrom, String amount, String dateTime, String email, String mobile) {
        this.receivedFrom = receivedFrom;
        this.amount = amount;
        this.dateTime = dateTime;
        this.email = email;
        this.mobile=mobile;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String dateTime;
    private String email;

}

