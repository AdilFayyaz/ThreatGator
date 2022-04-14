package com.threatgator.usermanagement.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Admin class - table in the DB
@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // primary key
    // other fields
    private String name;
    private String sector;
    private String country;

//    @OneToMany(
//            mappedBy = "organization",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<Admin> admins = new ArrayList<>();
//
//    @OneToMany(
//            mappedBy = "organization",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<Users> users = new ArrayList<>();

//    @OneToMany(
//            mappedBy = "organization",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//    private List<Assets> assets = new ArrayList<>();

    public Organization(String name, String sector, String country) {
        this.name = name;
        this.sector = sector;
        this.country = country;
    }

    public Organization() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public void setCountry(String country) {
        this.country = country;
    }

//    public void setAdmins(List<Admin> admins) {
//        this.admins = admins;
//    }

//    public void setUsers(List<Users> users) {
//        this.users = users;
//    }
//
////    public void setAssets(List<Assets> assets) {
////        this.assets = assets;
////    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSector() {
        return sector;
    }

    public String getCountry() {
        return country;
    }

//    public List<Admin> getAdmins() {
//        return admins;
//    }
//
//    public List<Users> getUsers() {
//        return users;
//    }

//    public List<Assets> getAssets() {
//        return assets;
//    }
}