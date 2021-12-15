package com.model;

import java.util.Date;

public class Tweet {
    private String username;
    private String body;
    private Date date;
    private Integer likes;
    private Integer retweets;

    public Tweet(String body) {
        this.body = body;
        this.username=null;
        this.date=null;
        this.likes=0;
        this.retweets=0;
    }

    public Tweet(String username, String body, Date date, Integer likes, Integer retweets) {
        this.username = username;
        this.body = body;
        this.date = date;
        this.likes = likes;
        this.retweets = retweets;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getRetweets() {
        return retweets;
    }

    public void setRetweets(Integer retweets) {
        this.retweets = retweets;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "body='" + body + '\'' +
                '}';
    }
}
