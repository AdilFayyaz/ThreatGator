package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
public class redditKafka {
    private String topicContent;
    public ArrayList<RedditThread> threads;

    public String preProcessStrings(String input){
        String processed=input.trim();
        //processed=processed.toLowerCase(Locale.ROOT);
        processed=processed.replaceAll("\\p{Punct}","");
        return processed;
    }

    public redditKafka(String topicContent) throws JSONException {
        threads= new ArrayList<>();
        this.topicContent = topicContent;
        JSONArray array= new JSONArray(topicContent);
        for (int i=0; i< array.length(); i++){
            JSONObject obj= array.getJSONObject(i);
            RedditThread r = new RedditThread(
                    obj.getString("subreddit"),
                    obj.getString("id"),
                    obj.getString("fullname"),
                    obj.getString("title"),
                    obj.getString("selftext"),
                    obj.getDouble("upvote_ratio"),
                    obj.getDouble("ups"),
                    obj.getDouble("downs"),
                    obj.getDouble("score"),
                    obj.getString("kind"));
            r.setTitle(preProcessStrings(r.getTitle()));
            r.setSelftext(preProcessStrings(r.getSelftext()));
            threads.add(r);
        }
    }

    @Override
    public String toString() {
        String returnStmt="";
        for (int i=0; i<threads.size(); i++){
            returnStmt+="Thread "+i+" "+threads.get(i)+"\n";
        }
        return returnStmt;
    }
}


