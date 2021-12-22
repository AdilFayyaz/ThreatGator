package com.threatgator.dataanalysis.model;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class threatExchange {
    public String id;
    public String lastSeen;
    public String title;
    public String type;
    public String threatScore;
    public String confidence;
    public String riskFactor;
    public String iPv4Details;
    public String dataSources;

    public threatExchange(String id, String lastSeen, String title, String type, String threatScore, String confidence, String riskFactor, String iPv4Details, String dataSources) {
        this.id = id;
        this.lastSeen = lastSeen;
        this.title = title;
        this.type = type;
        this.threatScore = threatScore;
        this.confidence = confidence;
        this.riskFactor = riskFactor;
        this.iPv4Details = iPv4Details;
        this.dataSources = dataSources;
    }


    public JSONObject getJSONExchangeData() throws JSONException{
        JSONObject j = new JSONObject();
        j.put("id", id);
        j.put("title",title);
        j.put("type", type);
        j.put("threatScore", threatScore);
        j.put("confidence", confidence);
        j.put("riskFactor", riskFactor);
        j.put("dataSources", dataSources);
//        j.put("iPv4Details", new JSONObject(iPv4Details));
        j.put("iPv4Details", iPv4Details);
        return j;
    }
}
