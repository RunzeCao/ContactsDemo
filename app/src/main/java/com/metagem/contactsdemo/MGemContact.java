package com.metagem.contactsdemo;


import java.io.Serializable;

class MGemContact implements Serializable {
    private String name;
    private String number;
    private String photoUrl;

    MGemContact(String name, String number, String photoUrl) {
        this.name = name;
        this.number = number;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}