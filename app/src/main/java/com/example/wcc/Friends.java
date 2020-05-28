package com.example.wcc;

public class Friends {
    public String name;
    public String image;
    public String thumb;
    public String status;


    public Friends(String name, String image, String thumb, String status) {
        this.name = name;
        this.image = image;
        this.thumb = thumb;
        this.status = status;
    }

    public Friends(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
