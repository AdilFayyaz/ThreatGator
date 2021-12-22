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

    // function to remove punctuation from string
    public String preProcessStrings(String input){
        String processed=input.trim();
        //processed=processed.toLowerCase(Locale.ROOT);
        processed=processed.replaceAll("\\p{Punct}","");
        return processed;
    }

    // map incoming message that contains multiple threads into an array of RedditThread
    public redditKafka(String topicContent) throws JSONException {
        threads= new ArrayList<>();
        this.topicContent = topicContent;
        JSONArray array= new JSONArray(topicContent); // convert string to JSON Array
        for (int i=0; i< array.length(); i++){
            JSONObject obj= array.getJSONObject(i); // get JSON Object at each index
            // get required attributes
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
            r.setTitle(preProcessStrings(r.getTitle())); // preprocess title to remove punctuation
            r.setSelftext(preProcessStrings(r.getSelftext())); // preprocess text to remove punctuation
            threads.add(r); // add to array
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


