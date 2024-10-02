package com.example.tathastu.User_Package.Food_Section.History;

public class food_user_model {

    String name,email,mobile,address,description,key,userId;


    public food_user_model() {
    }

    public food_user_model(String name, String email, String mobile, String address, String description, String key, String userId) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.description = description;
        this.key = key;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
