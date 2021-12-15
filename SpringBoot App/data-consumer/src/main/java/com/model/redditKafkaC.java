package com.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
public class redditKafkaC {
    private String topicContent;
    public ArrayList<RedditComment> comments;

    public redditKafkaC(String topicContent) throws JSONException {
        comments= new ArrayList<>();
        this.topicContent = topicContent;
        JSONArray array= new JSONArray(topicContent);
        for (int i=0; i< array.length(); i++){
            JSONObject obj= array.getJSONObject(i);
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
            comments.add(c);
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


