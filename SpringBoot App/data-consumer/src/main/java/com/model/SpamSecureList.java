package com.model;

public class SpamSecureList {
    private String body;
    public SpamSecureList(String body){
        this.body = body;
    }
    public String getBody(){
        return body;
    }
    public void setBody(){
        this.body = body;
    }

    @Override
    public String toString() {
        return "Spam{" +
                "body='" + body + '\'' +
                '}';
    }
}
