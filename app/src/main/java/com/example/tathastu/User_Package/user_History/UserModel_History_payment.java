package com.example.tathastu.User_Package.user_History;

// EventModel.java

public class UserModel_History_payment {
    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public UserModel_History_payment(String sentTo, String method, String amount, String dateTime, String status, String transactionId,String email) {
        this.sentTo = sentTo;
        this.method = method;
        this.amount = amount;
        this.dateTime = dateTime;
        this.status = status;
        this.transactionId = transactionId;
        this.email=email;
    }

    private String sentTo;
    private String method;
    private String amount;
    private String dateTime;
    private String status;
    private String transactionId;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

