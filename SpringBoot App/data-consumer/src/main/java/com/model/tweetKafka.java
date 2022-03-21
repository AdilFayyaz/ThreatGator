package com.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class tweetKafka {
    private String tweets;
    public ArrayList<Tweet> tweetList;

    public String preProcessStrings(String input){
        String processed=input.trim();
        //processed=processed.toLowerCase(Locale.ROOT);
        processed=processed.replaceAll("\\p{Punct}","");
        return processed;
    }


    public tweetKafka(String tweets) throws JSONException {
        tweetList= new ArrayList<>();
        this.tweets = tweets;
        JSONObject obj= new JSONObject(tweets);
        JSONArray array = obj.getJSONArray("tweets");
        for (int i=0; i< array.length(); i++){
            tweetList.add(new Tweet(preProcessStrings(array.getString(i))));
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
