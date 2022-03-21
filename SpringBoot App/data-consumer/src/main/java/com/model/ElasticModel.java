package com.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//this class can then be inherited and implemented for different SDOs like malware, TA, etc...

public class ElasticModel {
    //attributes can be added later as needed for prioritization module like score, time etc
    public int hash=0; //to uniquely identify
    public String source="";
    public String rawText="";
    public long time=0;
    public ArrayList<Entity> malwares=new ArrayList<>();
    public ArrayList<Entity> threatActors=new ArrayList<>();
    public ArrayList<Entity> identities=new ArrayList<>();
    public ArrayList<Entity> locations=new ArrayList<>();
    public ArrayList<Entity> tools=new ArrayList<>();
    public ArrayList<Entity> vulnerabilities=new ArrayList<>();
    public ArrayList<Entity> infrastructures=new ArrayList<>();
    public ArrayList<Entity> indicators=new ArrayList<>();
    public ArrayList<Entity> campaigns=new ArrayList<>();
    public HashMap<List<Entity>, String> relation = new HashMap<>();
    public String bundleJson = new String();

    @Override
    public String toString() {
        return "ElasticModel{" +
                "hash=" + hash +
                ", source='" + source + '\'' +
                ", rawText='" + rawText + '\'' +
                ", malwares=" + malwares +
                ", threatActors=" + threatActors +
                ", identities=" + identities +
                ", locations=" + locations +
                ", tools=" + tools +
                ", vulnerabilities=" + vulnerabilities +
                ", infrastructures=" + infrastructures +
                ", indicators=" + indicators +
                ", campaigns=" + campaigns +
                ", relations=" + relation.toString() +
                ", bundle=" + bundleJson +
                '}';
    }
}
