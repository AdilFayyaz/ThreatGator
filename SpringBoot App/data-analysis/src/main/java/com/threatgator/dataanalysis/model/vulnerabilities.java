package com.threatgator.dataanalysis.model;

import java.util.HashMap;
import java.util.Map;

public class vulnerabilities {
    Map vulnerabilitiesMap = new HashMap<String, Integer>();

    void addVulnerability(String name){
        if(vulnerabilitiesMap.get(name) == null){
            vulnerabilitiesMap.put(name, 1);
        }
        else{
            Integer val = (Integer) vulnerabilitiesMap.get(name);
            vulnerabilitiesMap.put(name,val+1);
        }
    }
    public Map getVulnerabilitiesMap(){
        return vulnerabilitiesMap;
    }

}
