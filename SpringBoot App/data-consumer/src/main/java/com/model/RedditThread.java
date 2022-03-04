package com.model;

public class RedditThread {
    public String subReddit;
    public String id;
    public String fullName;
    public String title;
    public String selftext;
    public Double upvote_ratio;
    public Double ups;
    public Double downs;
    public Double score;
    public String kind;


    public RedditThread(String subReddit, String id, String fullName, String title, String selftext, Double upvote_ratio, Double ups, Double downs, Double score, String kind) {
        this.subReddit = subReddit;
        this.id = id;
        this.fullName = fullName;
        this.title = title;
        this.selftext = selftext;
        this.upvote_ratio = upvote_ratio;
        this.ups = ups;
        this.downs = downs;
        this.score = score;
        this.kind = kind;
    }

    // getters and setters

    public String getSubReddit() {
        return subReddit;
    }

    public void setSubReddit(String subReddit) {
        this.subReddit = subReddit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelftext() {
        return selftext;
    }

    public void setSelftext(String selftext) {
        this.selftext = selftext;
    }

    public Double getUpvote_ratio() {
        return upvote_ratio;
    }

    public void setUpvote_ratio(Double upvote_ratio) {
        this.upvote_ratio = upvote_ratio;
    }

    public Double getUps() {
        return ups;
    }

    public void setUps(Double ups) {
        this.ups = ups;
    }

    public Double getDowns() {
        return downs;
    }

    public void setDowns(Double downs) {
        this.downs = downs;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "RedditThread{" +
                "subReddit='" + subReddit + '\'' +
                ", id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", title='" + title + '\'' +
                ", selftext='" + selftext + '\'' +
                ", upvote_ratio=" + upvote_ratio +
                ", ups=" + ups +
                ", downs=" + downs +
                ", score=" + score +
                ", kind='" + kind + '\'' +
                '}';
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
