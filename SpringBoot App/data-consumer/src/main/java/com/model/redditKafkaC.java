package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
public class redditKafkaC {
    private String topicContent;
    public ArrayList<RedditComment> comments;

    // function to remove punctuation from string
    public String preProcessStrings(String input){
        String processed=input.trim();
        //processed=processed.toLowerCase(Locale.ROOT);
        processed=processed.replaceAll("\\p{Punct}","");
        return processed;
    }

    // map incoming message that contains multiple comments into an array of RedditComment
    public redditKafkaC(String topicContent) throws JSONException {

        comments= new ArrayList<>();
        this.topicContent = topicContent;
        JSONArray array= new JSONArray(topicContent); // convert string to JSON Array
        for (int i=0; i< array.length(); i++){
            JSONObject obj= array.getJSONObject(i); // get JSON Object at each index

            // get required attributes

            Boolean blocked, edited;
            if (obj.getDouble("is_blocked")==0.0)
                blocked=false;
            else
                blocked=true;
            if (obj.getDouble("is edited")==0.0)
                edited=false;
            else
                edited=true;
            RedditComment c = new RedditComment(
                    obj.getString("id"),
                    obj.getString("author"),
                    obj.getString("author fullname"),
                    blocked,
                    edited,
                    obj.getString("body"),
                    obj.getDouble("ups"),
                    obj.getDouble("downs"),
                    obj.getDouble("score"),
                    obj.getString("kind"));
            c.setBody(preProcessStrings(c.getBody())); // preprocess body to remove punctuation
            comments.add(c); // add to array
        }
    }

    @Override
    public String toString() {
        String returnStmt="";
        for (int i=0; i<comments.size(); i++){
            returnStmt+="Thread "+i+" "+comments.get(i)+"\n";
        }
        return returnStmt;
    }
}


