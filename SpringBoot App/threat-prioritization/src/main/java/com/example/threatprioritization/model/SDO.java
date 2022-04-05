package com.example.threatprioritization.model;

import java.util.ArrayList;

public class SDO {
    public ArrayList<String> ids=new ArrayList<String>();
    public String name = "";
    public String type = "";

    public SDO(String id, String name, String type) {
        this.ids.add(id);
        this.name = name;
        this.type = type;
    }
}
