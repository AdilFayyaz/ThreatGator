package com.threatgator.dataanalysis.model;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
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
    public JSONObject getJSONVulnerabilities() throws JSONException {
        JSONObject j = new JSONObject();
        Iterator hmIterator = vulnerabilitiesMap.entrySet().iterator();
        while(hmIterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            j.put((String) mapElement.getKey(), mapElement.getValue());
        }
        return j;
    }
}
