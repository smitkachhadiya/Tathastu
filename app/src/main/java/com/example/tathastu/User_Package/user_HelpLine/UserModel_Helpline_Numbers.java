package com.example.tathastu.User_Package.user_HelpLine;

public class UserModel_Helpline_Numbers {
    private String name;
    private String number;
    private String category;
    private boolean expanded;

    public UserModel_Helpline_Numbers(String name, String number, String category) {
        this.name = name;
        this.number = number;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getCategory() {
        return category;
    }
}