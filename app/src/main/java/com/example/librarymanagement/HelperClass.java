package com.example.librarymanagement;

public class HelperClass {
    public String name, phone, department, username, password, userType;

    public HelperClass() {} // required for Firebase

    public HelperClass(String name, String phone, String department, String username, String password, String userType) {
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // ✅ Getter for username (used in profile logic comparison)
    public String getUsername() {
        return username;
    }
}
