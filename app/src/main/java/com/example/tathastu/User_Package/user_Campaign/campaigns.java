package com.example.tathastu.User_Package.user_Campaign;

public class campaigns {

    //Integer cid;
    private String description;
    private String donation_received;

    private String name;
    private String imageUrl;
    private String organizer_contact;
    private String organizer_name;

    private  String ngoId;


    public String getNgoId() {
        return ngoId;
    }

    public void setNgoId(String ngoId) {
        this.ngoId = ngoId;
    }

    private  String key;

    public campaigns(){
    }

    public campaigns(String ngoId,String description, String donation_received,String imageUrl, String name, String organizer_contact, String organizer_name,String key) {
        //this.cid = cid;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.organizer_name = organizer_name;
        this.organizer_contact = organizer_contact;
        this.donation_received = donation_received;
        this.key=key;
        this.ngoId=ngoId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
//    public Integer getCid() {
//        return cid;
//    }

    public String getDescription() {return description;}

    public String getImageUrl() {return imageUrl;}

    public String getDonation_received() {return donation_received;}

    public String getName() {return name;}

    public String getOrganizer_contact() {return organizer_contact;}

    public String getOrganizer_name() {return organizer_name;}



    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDonation_received(String donation_received) {
        this.donation_received = donation_received;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizer_contact(String organizer_contact) {
        this.organizer_contact = organizer_contact;
    }

    public void setOrganizer_name(String organizer_name) {
        this.organizer_name = organizer_name;
    }

}
