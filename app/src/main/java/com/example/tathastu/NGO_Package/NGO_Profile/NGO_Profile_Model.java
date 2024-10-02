package com.example.tathastu.NGO_Package.NGO_Profile;

public class NGO_Profile_Model {


    private String fname;
    private String email;
    private String mobile;
    private String optional_mobile;
    private String address;
    private String password;
    private String website;
    private String type;

    private String ngoId;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NGO_Profile_Model(){}

    public String getNgoId() {
        return ngoId;
    }

    public void setNgoId(String ngoId) {
        this.ngoId = ngoId;
    }

    public NGO_Profile_Model(String ngoId, String fname, String email, String mobile, String optional_mobile, String type, String photo, String address, String password, String website, String instagram, String linkedin, String facebook, String twitter, String youtube) {
        this.ngoId=ngoId;
        this.fname = fname;
        this.email = email;
        this.mobile = mobile;
        this.type=type;
        this.optional_mobile = optional_mobile;
        this.address = address;
        this.password = password;
        this.website = website;
        this.instagram = instagram;
        this.linkedin = linkedin;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.photo=photo;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
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

    public String getOptional_mobile() {
        return optional_mobile;
    }

    public void setOptional_mobile(String optional_mobile) {
        this.optional_mobile = optional_mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    private String instagram;
    private String linkedin;
    private String facebook;
    private String twitter;
    private String youtube;

}
