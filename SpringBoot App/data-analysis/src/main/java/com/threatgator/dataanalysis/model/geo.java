package com.threatgator.dataanalysis.model;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// Class of geo information - not being used right now
public class geo {
    public String charset;
    public String areaCode;
    public String dmaCode;
    public String city;
    public String countryCode;
    public String latitude;
    public String cityData;
    public String countryName;
    public String asn;
    public String longitude;
    Map locationMap = new HashMap<String, Integer>();
    public geo(String charset, String areaCode, String dmaCode, String city, String countryCode, String latitude, String cityData, String countryName, String asn, String longitude) {
        this.charset = charset;
        this.areaCode = areaCode;
        this.dmaCode = dmaCode;
        this.city = city;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.cityData = cityData;
        this.countryName = countryName;
        this.asn = asn;
        this.longitude = longitude;
    }

    public geo() {

    }

    // add vulnerabilities to map
    void addLocation(String name){
        if(locationMap.get(name) == null){
            locationMap.put(name, 1);
        }
        else{
            Integer val = (Integer) locationMap.get(name);
            locationMap.put(name,val+1);
        }
    }

    // get JSON object of all vulnerabilities
    public JSONObject getJSONLocations() throws JSONException {
        JSONObject j = new JSONObject();
        Iterator hmIterator = locationMap.entrySet().iterator();
        while(hmIterator.hasNext()){
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            j.put((String) mapElement.getKey(), mapElement.getValue());
        }
        return j;
    }

}
