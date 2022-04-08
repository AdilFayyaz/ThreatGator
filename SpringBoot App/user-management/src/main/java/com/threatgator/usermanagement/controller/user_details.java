package com.threatgator.usermanagement.controller;

import com.threatgator.usermanagement.model.Organization;

// Class is used for authenticating the user and checking their credentials in the DB
public class user_details {
    String username;
    String password;
    Organization organization;
    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization org_id) {
        this.organization=org_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public user_details(String username, String password, Organization org_id) {
        this.username = username;
        this.password = password;
        this.organization= org_id;
    }
}
