package com.app.dabshi_test_graphic.Users;

public class Complain {
    private String idUser;
    private String idComplain;
    private String useImage;
    private String username;
    private String userEmail;
    private String subject;
    private String message;

    public Complain() {
    }


    public Complain(String idUser, String idComplain, String useImage, String username, String userEmail, String subject, String message) {
        this.idUser = idUser;
        this.idComplain = idComplain;
        this.useImage = useImage;
        this.username = username;
        this.userEmail = userEmail;
        this.subject = subject;
        this.message = message;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdComplain() {
        return idComplain;
    }

    public void setIdComplain(String idComplain) {
        this.idComplain = idComplain;
    }

    public String getUseImage() {
        return useImage;
    }

    public void setUseImage(String useImage) {
        this.useImage = useImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


}
