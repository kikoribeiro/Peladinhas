package com.example.peladinhas.Classes;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Message {
    private String sender;
    private String content;

    @ServerTimestamp
    private Date timestamp;


    public Message() {

    }

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
