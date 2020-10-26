package com.example.herculesbusiness.Trading;


public class UploadPDF {

    public String name;
    public String url;
    public String date;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UploadPDF() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UploadPDF(String name, String url, String date) {
        this.name = name;
        this.url = url;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}