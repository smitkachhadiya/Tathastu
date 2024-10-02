package com.example.tathastu.NGO_Package.NGO_Food_Camp.History;

public class NGO_food_History_model {

    String ngo_name,start_date,end_date,mobile,c_address,description,userId,key;

    public NGO_food_History_model() {
    }

    public NGO_food_History_model(String ngo_name, String start_date, String end_date, String mobile, String c_address, String description, String userId, String key) {
        this.ngo_name = ngo_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.mobile = mobile;
        this.c_address = c_address;
        this.description = description;
        this.userId = userId;
        this.key = key;
    }

    public String getNgo_name() {
        return ngo_name;
    }

    public void setNgo_name(String ngo_name) {
        this.ngo_name = ngo_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getC_address() {
        return c_address;
    }

    public void setC_address(String c_address) {
        this.c_address = c_address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
