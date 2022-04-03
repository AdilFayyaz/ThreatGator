package com.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class APTSecureListKafka {
    public String APT_data;
    public ArrayList<APTSecureList> APTList;

    public String preProcessStrings(String input){
        String processed = input.trim();
        processed = processed.replaceAll("\\p{Punct}", "");
        return processed;
    }

    public APTSecureListKafka(String apt) throws JSONException {
        APTList = new ArrayList<>();
        this.APT_data = apt;
        JSONObject obj = new JSONObject(this.APT_data);
        JSONArray array = obj.getJSONArray("data");
        for(int i=0; i<array.length();i++){
            APTList.add(new APTSecureList(preProcessStrings(array.getString(i))));
        }
    }
    @Override
    public String toString() {
        String returnStmt="";
        for (int i=0; i<APTList.size(); i++){
            returnStmt+="APT-Data "+i+" "+APTList.get(i)+"\n";
        }
        return returnStmt;
    }
}
