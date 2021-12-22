package com.threatgator.dataanalysis.model;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
// notifications related class
public class notifications {
    // notifications map
    Map notifications = new HashMap<String,String>();
    // add notifications function
    void addNotifications(String notif, String _hash){
        notifications.put(notif,_hash);
    }
    // get notifications function
    public JSONObject getNotifications(int value) throws JSONException {
        Iterator hmIterator = notifications.entrySet().iterator();
        JSONObject j = new JSONObject();
        int count = 0;
        while(hmIterator.hasNext()){
            if(count>=value){break;}
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            j.put( (String) mapElement.getKey(),mapElement.getValue());
            count += 1;
        }
        return j;
    }
}
