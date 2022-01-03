package com.app.dabshi_test_graphic.Users;

public class User {
    public String id, username, email, userType, phone, profileImage;

    public User() {
    }


    public User(String id, String username, String email, String userType, String phone, String profileImage) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.phone = phone;
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
