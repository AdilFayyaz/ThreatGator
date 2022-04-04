package com.threatgator.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

// Users assets - to be used in the 3rd iteration
@Entity
public class Assets {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //pk
    private String vendor;
    private String name;
    private String version;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;


    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getName(){
        return this.name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Organization getOrganization() {
        return organization;
    }
}
