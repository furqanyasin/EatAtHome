package com.example.eatathome.Client.Model;

public class  Sender {

    public String to;
    public com.example.eatathome.Client.Model.Notification notification;


    public Sender(String to, com.example.eatathome.Client.Model.Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public void setNotification(com.example.eatathome.Client.Model.Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
