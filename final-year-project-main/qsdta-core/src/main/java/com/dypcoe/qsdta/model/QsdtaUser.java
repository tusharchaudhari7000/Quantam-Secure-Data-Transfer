package com.dypcoe.qsdta.model;

public class QsdtaUser {
    private String uuid;
    private String email;
    private String firstname;
    private String lastName;
    private String imageURL;
    public String getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstname;
    }    

    public String getLastName() {
        return lastName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
