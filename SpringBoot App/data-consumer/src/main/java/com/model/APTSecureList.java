package com.model;

public class APTSecureList {
    private String body;
    public APTSecureList(String body){
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
        return "APT{" +
                "body='" + body + '\'' +
                '}';
    }

}
