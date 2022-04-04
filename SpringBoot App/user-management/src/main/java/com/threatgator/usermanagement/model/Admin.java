package com.threatgator.usermanagement.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// Admin class - table in the DB
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // primary key
    // other fields
    private String name;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;


    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
