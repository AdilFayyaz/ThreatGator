package com.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IncidentSecureListKafka {
    public String Incident_data;
    public ArrayList<IncidentSecureList> IncidentList;

    public String preProcessStrings(String input){
        String processed = input.trim();
        processed = processed.replaceAll("\\p{Punct}", "");
        return processed;
    }

    public IncidentSecureListKafka(String spam) throws JSONException {
        IncidentList = new ArrayList<>();
        this.Incident_data = spam;
        JSONObject obj = new JSONObject(this.Incident_data);
        JSONArray array = obj.getJSONArray("data");
        for(int i=0; i<array.length();i++){
            IncidentList.add(new IncidentSecureList(preProcessStrings(array.getString(i))));
        }
    }
    @Override
    public String toString() {
        String returnStmt="";
        for (int i=0; i<IncidentList.size(); i++){
            returnStmt+="Incident-Data "+i+" "+IncidentList.get(i)+"\n";
        }
        return returnStmt;
    }
}
