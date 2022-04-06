package com.example.threatprioritization.model;

public class Organization {
    private Integer id;
    private String name;
    private String sector;
    private String country;

    public Organization() {
    }

    public Organization(Integer id, String name, String sector, String country) {
        this.id = id;
        this.name = name;
        this.sector = sector;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
