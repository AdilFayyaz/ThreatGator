package com.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

//this class can then be inherited and implemented for different SDOs like malware, TA, etc...

public class ElasticModel {
    //attributes can be added later as needed for prioritization module like score, time etc
    public ArrayList<Entity> malwares=new ArrayList<>();
    public ArrayList<Entity> threatActors=new ArrayList<>();
    public ArrayList<Entity> identities=new ArrayList<>();
    public ArrayList<Entity> locations=new ArrayList<>();
    public ArrayList<Entity> tools=new ArrayList<>();
    public ArrayList<Entity> vulnerabilities=new ArrayList<>();
    public ArrayList<Entity> infrastructures=new ArrayList<>();
    public ArrayList<Entity> indicators=new ArrayList<>();
    public ArrayList<Entity> campaigns=new ArrayList<>();

    @Override
    public String toString() {
        return "ElasticModel{" +
                "malwares=" + malwares +
                ", threatActors=" + threatActors +
                ", identities=" + identities +
                ", locations=" + locations +
                ", tools=" + tools +
                ", vulnerabilities=" + vulnerabilities +
                ", infrastructures=" + infrastructures +
                ", indicators=" + indicators +
                '}';
    }
}
