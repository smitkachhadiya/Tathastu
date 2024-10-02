package com.example.tathastu.User_Package.user_DashBoard;

public class profile_getset {

    String fname,lname,mobile,dob,email,password,photo,userId,token;

    public profile_getset() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public profile_getset(String fname, String lname, String mobile, String dob, String email, String password, String photo, String userId, String token) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.userId = userId;
        this.token=token;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
