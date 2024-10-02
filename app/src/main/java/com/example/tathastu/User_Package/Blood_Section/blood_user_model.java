package com.example.tathastu.User_Package.Blood_Section;

public class blood_user_model {

    String name,age,weight,blood_group,mobile,address,description,key,userId;

    public blood_user_model() {
    }

    public blood_user_model(String name, String age, String weight, String blood_group, String mobile, String address, String description, String key, String userId) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.blood_group = blood_group;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
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
