package com.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpamSecureListKafka {
    public String Spam_data;
    public ArrayList<SpamSecureList> SpamList;

    public String preProcessStrings(String input){
        String processed = input.trim();
        processed = processed.replaceAll("\\p{Punct}", "");
        return processed;
    }

    public SpamSecureListKafka(String spam) throws JSONException {
        SpamList = new ArrayList<>();
        this.Spam_data = spam;
        JSONObject obj = new JSONObject(this.Spam_data);
        JSONArray array = obj.getJSONArray("data");
        for(int i=0; i<array.length();i++){
            SpamList.add(new SpamSecureList(preProcessStrings(array.getString(i))));
        }
    }
    @Override
    public String toString() {
        String returnStmt="";
        for (int i=0; i<SpamList.size(); i++){
            returnStmt+="Spam-Data "+i+" "+SpamList.get(i)+"\n";
        }
        return returnStmt;
    }
}
