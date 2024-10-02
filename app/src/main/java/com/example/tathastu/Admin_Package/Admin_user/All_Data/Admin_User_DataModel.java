package com.example.tathastu.Admin_Package.Admin_user.All_Data;

public class Admin_User_DataModel {
    private String fname;
    private String mobile;
    private String email;
    private String userId;

    public Admin_User_DataModel(String fname, String mobile, String email, String userId) {
        this.fname = fname;
        this.mobile = mobile;
        this.email = email;
        this.userId = userId;
    }

    public Admin_User_DataModel() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}


