package com.example.tathastu.NGO_Package.NGO_Event;

public class volunteer {

    private String vid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    private String name;
    private String email;
    private String contact_no;
    private String address;
    private String age;

    private  String ngoId;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public volunteer(){
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNgoId() {
        return ngoId;
    }

    public void setNgoId(String ngoId) {
        this.ngoId = ngoId;
    }

    public volunteer(String vid, String name, String email, String contact_no, String address, String age, String ngoId,String userId) {
        this.vid = vid;
        this.name = name;
        this.email = email;
        this.contact_no = contact_no;
        this.address = address;
        this.age = age;
        this.ngoId = ngoId;
        this.userId=userId;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }


}
