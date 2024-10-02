package com.example.tathastu.User_Package.user_NGO_list;

public class NGOData {
    private String ngoName;
    private String ngoAddress;
    private String ngoCategory;
    private String ngoMno;
    private String ngoWebsite;
    private String ngoEmail;
    private String ngoInstagram;
    private String ngoLinkedIn;
    private String ngoFacebook;
    private String ngoTwitter;
    private String ngoYoutube;

    private boolean expanded;


    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public NGOData(String ngoName, String ngoAddress, String ngoCategory, String ngoMno, String ngoWebsite, String ngoEmail, String ngoInstagram, String ngoLinkedIn, String ngoFacebook, String ngoTwitter, String ngoYoutube) {
        this.ngoName = ngoName;
        this.ngoAddress = ngoAddress;
        this.ngoCategory = ngoCategory;
        this.ngoMno = ngoMno;
        this.ngoWebsite = ngoWebsite;
        this.ngoEmail = ngoEmail;
        this.ngoInstagram = ngoInstagram;
        this.ngoLinkedIn = ngoLinkedIn;
        this.ngoFacebook = ngoFacebook;
        this.ngoTwitter = ngoTwitter;
        this.ngoYoutube = ngoYoutube;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getNgoAddress() {
        return ngoAddress;
    }

    public void setNgoAddress(String ngoAddress) {
        this.ngoAddress = ngoAddress;
    }

    public String getNgoCategory() {
        return ngoCategory;
    }

    public void setNgoCategory(String ngoCategory) {
        this.ngoCategory = ngoCategory;
    }

    public String getNgoMno() {
        return ngoMno;
    }

    public void setNgoMno(String ngoMno) {
        this.ngoMno = ngoMno;
    }

    public String getNgoWebsite() {
        return ngoWebsite;
    }

    public void setNgoWebsite(String ngoWebsite) {
        this.ngoWebsite = ngoWebsite;
    }

    public String getNgoEmail() {
        return ngoEmail;
    }

    public void setNgoEmail(String ngoEmail) {
        this.ngoEmail = ngoEmail;
    }

    public String getNgoInstagram() {
        return ngoInstagram;
    }

    public void setNgoInstagram(String ngoInstagram) {
        this.ngoInstagram = ngoInstagram;
    }

    public String getNgoLinkedIn() {
        return ngoLinkedIn;
    }

    public void setNgoLinkedIn(String ngoLinkedIn) {
        this.ngoLinkedIn = ngoLinkedIn;
    }

    public String getNgoFacebook() {
        return ngoFacebook;
    }

    public void setNgoFacebook(String ngoFacebook) {
        this.ngoFacebook = ngoFacebook;
    }

    public String getNgoTwitter() {
        return ngoTwitter;
    }

    public void setNgoTwitter(String ngoTwitter) {
        this.ngoTwitter = ngoTwitter;
    }

    public String getNgoYoutube() {
        return ngoYoutube;
    }

    public void setNgoYoutube(String ngoYoutube) {
        this.ngoYoutube = ngoYoutube;
    }
}
