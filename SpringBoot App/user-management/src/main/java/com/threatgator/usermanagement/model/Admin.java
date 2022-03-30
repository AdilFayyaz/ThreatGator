package com.threatgator.usermanagement.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// Admin class - table in the DB
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // primary key
    // other fields
    private String name;
    private String email;
    private String password;

//    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
//    Set<Users> user= new HashSet<Users>();

    // Constructor
    public Admin(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Admin() {

    }
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
}
