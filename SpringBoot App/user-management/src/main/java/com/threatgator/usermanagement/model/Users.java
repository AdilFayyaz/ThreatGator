package com.threatgator.usermanagement.model;

import javax.persistence.*;

// Users class  -table
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //pk
    // other fields
    private String name;
    private String email;
    private String password;
//    @ManyToOne
//    @JoinColumn(name = "AdminID", referencedColumnName="")
//    private Admin admin;


//    public Users(String name, String email, String password, Admin admin) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.password = password;
////        this.admin = admin;
//    }

    // Constructor
    public Users(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Users() {

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
