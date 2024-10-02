package com.example.tathastu.Admin_Package.Admin_user.Person_User_Data;

public class Admin_person_DataModel {
    private String fname;
    private String lname;
    private String mobile;
    private String email;
    private String dob;

    public Admin_person_DataModel() {
    }

    public Admin_person_DataModel(String fname, String lname, String mobile, String email, String dob) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.email = email;
        this.dob = dob;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}


