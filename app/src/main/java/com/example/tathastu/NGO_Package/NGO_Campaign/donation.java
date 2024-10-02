package com.example.tathastu.NGO_Package.NGO_Campaign;

public class donation {

    private String amount;
    private String contact_no;
    private String date;
    private String email;
    private String name;
    private String transaction_id;
    private String userId;
    private String organizer_name;

    public donation(){
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrganizer_name() {
        return organizer_name;
    }

    public void setOrganizer_name(String organizer_name) {
        this.organizer_name = organizer_name;
    }

    public donation(String amount, String contact_no, String date, String email, String name, String transaction_id, String userId, String organizer_name) {
        this.amount = amount;
        this.contact_no = contact_no;
        this.date = date;
        this.email = email;
        this.name = name;
        this.transaction_id = transaction_id;
        this.userId = userId;
        this.organizer_name = organizer_name;
    }
}
