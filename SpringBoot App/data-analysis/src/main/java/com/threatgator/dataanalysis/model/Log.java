package com.threatgator.dataanalysis.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Log implements Serializable {
    public String id;
    public String reportHash;
    public String timestamp;
    public Integer adminId;
    public Integer orgId;
    public ArrayList<String> newEntities;
    public ArrayList<String> deletedEntities;
    public ArrayList<String> changedTypes;

    public Log(){

    }
    
}
