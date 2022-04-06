package com.example.threatprioritization.model;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;


public class Assets implements Serializable {

    private Integer id; //pk
    private String vendor;
    private String name;
    private String version;

    private Organization organization;

    public Assets(){}

    public Assets(Integer id, String vendor, String name, String version, Organization organization) {
        this.id = id;
        this.vendor = vendor;
        this.name = name;
        this.version = version;
        this.organization = organization;
    }

    public Assets(Assets assets){
        this.id=assets.getId();
        this.name=assets.getName();
        this.organization=assets.getOrganization();
        this.vendor=assets.getVendor();
        this.version=assets.getVersion();
    }

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
