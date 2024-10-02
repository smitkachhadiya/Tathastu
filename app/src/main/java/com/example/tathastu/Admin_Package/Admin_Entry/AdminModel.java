package com.example.tathastu.Admin_Package.Admin_Entry;

public class AdminModel {
    private String adminId; // Add this field for Firebase key
    private String email;
    private String name;
    private String pwd; // Update this field based on the actual name in the database
    private String mno;

    // Empty constructor is required for Firebase to deserialize the data
    public AdminModel() {
        // Default constructor required for calls to DataSnapshot.getValue(AdminModel.class)
    }

    public AdminModel(String adminId, String email, String name, String pwd, String mno) {
        this.adminId = adminId;
        this.email = email;
        this.name = name;
        this.pwd = pwd;
        this.mno = mno;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getMno() {
        return mno;
    }
}
