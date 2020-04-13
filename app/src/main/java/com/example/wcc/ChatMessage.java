package com.example.wcc;

import java.util.Date;

public class ChatMessage {
    private String Text,uid,user,thumb;
    private Date date;

    public ChatMessage(){

    }
    public ChatMessage(String Text,String uid,String user,Date date,String thumb){
        this.date=date;
        this.Text=Text;
        this.uid=uid;
        this.user=user;
        this.thumb=thumb;
        }

    public String getText() {
        return this.Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}

