package com.threatgator.usermanagement.controller;
// Class is used for authenticating the user and checking their credentials in the DB
public class user_details {
    String username;
    String password;

    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public user_details(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
