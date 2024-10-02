package com.example.tathastu.Admin_Package.Admin_NGO.All_Data;

public class Admin_NGO_DataModel {
    private String fname;
    private String mobile;
    private String email;
    private String ngoId;

    public Admin_NGO_DataModel() {
    }

    public Admin_NGO_DataModel(String fname, String mobile, String email, String ngoId) {
        this.fname = fname;
        this.mobile = mobile;
        this.email = email;
        this.ngoId = ngoId;
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

    public String getNgoId() {
        return ngoId;
    }

    public void setNgoId(String ngoId) {
        this.ngoId = ngoId;
    }
}


