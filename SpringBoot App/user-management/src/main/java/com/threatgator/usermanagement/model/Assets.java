package com.threatgator.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

// Users assets - to be used in the 3rd iteration
@Entity
public class Assets {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //pk
    private String vendor;
    private String name;
    private String version;
    private int  admin; //fk

    public Assets(){}
    public Assets(int id, String vendor, String name, String version, int admin) {
        this.id = id;
        this.vendor = vendor;
        this.name = name;
        this.version = version;
        this.admin = admin;
    }
}
