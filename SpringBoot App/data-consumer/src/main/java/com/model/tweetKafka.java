package com.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class tweetKafka {
    private String tweets;
    public ArrayList<Tweet> tweetList;

    public tweetKafka(String tweets) throws JSONException {
        tweetList= new ArrayList<>();
        this.tweets = tweets;
        JSONObject obj= new JSONObject(tweets);
        JSONArray array = obj.getJSONArray("tweets");
        for (int i=0; i< array.length(); i++){
            tweetList.add(new Tweet(array.getString(i)));
        }
    }

    @Override
    public String toString() {
        String returnStmt="";
        for (int i=0; i<tweetList.size(); i++){
            returnStmt+="Tweet "+i+" "+tweetList.get(i)+"\n";
        }
        return returnStmt;
    }
}
