package com.threatgator.dataanalysis.model;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;


import java.util.ArrayList;

public class notifications {
    ArrayList<String> notifications = new ArrayList<String>();
    void addNotifications(String notifiction){
        notifications.add(notifiction);
    }

    public JSONObject getNotifications(int value) throws JSONException {
        JSONObject j = new JSONObject();
        for(int i=0;i<value;i++){
            j.put("Threat " + String.valueOf(i) ,notifications.get(i));
        }
        return j;
    }
}
