package com.model;

public class IncidentSecureList {
    private String body;
    public IncidentSecureList(String body){
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
        return "Incident{" +
                "body='" + body + '\'' +
                '}';
    }
}
