package com.example.tathastu.User_Package.user_Event;

public class events {

    private String address;
    private String city;
    private String date;
    private String description;
    private String imageUrl;
    private String name;
    private String organizer_name;
    private String total_volunteer;
    private String volunteer_get;

    public String getNgoId() {
        return ngoId;
    }

    public void setNgoId(String ngoId) {
        this.ngoId = ngoId;
    }

    private  String ngoId;

    private String key;

    public events(){
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public events(String ngoId,String key, String address, String city, String date, String description, String imageUrl, String name, String organizer_name, String total_volunteer, String volunteer_get) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.description = description;
        this.imageUrl = imageUrl;
        this.name = name;
        this.organizer_name = organizer_name;
        this.total_volunteer = total_volunteer;
        this.volunteer_get = volunteer_get;
        this.key=key;
        this.ngoId=ngoId;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getOrganizer_name() {
        return organizer_name;
    }

    public String getTotal_volunteer() {
        return total_volunteer;
    }

    public String getVolunteer_get() {
        return volunteer_get;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizer_name(String organizer_name) {
        this.organizer_name = organizer_name;
    }

    public void setTotal_volunteer(String total_volunteer) {
        this.total_volunteer = total_volunteer;
    }

    public void setVolunteer_get(String volunteer_get) {
        this.volunteer_get = volunteer_get;
    }
}
