package com.model;

public class RedditComment {
    private String id;
    private String author;
    private String authorFullName;
    private Boolean is_blocked;
    private Boolean is_edited;
    private String body;
    private Double ups;
    private Double downs;
    private Double score;
    private String kind;

    public RedditComment(String id, String author, String authorFullName, Boolean is_blocked, Boolean is_edited, String body, Double ups, Double downs, Double score, String kind) {
        this.id = id;
        this.author = author;
        this.authorFullName = authorFullName;
        this.is_blocked = is_blocked;
        this.is_edited = is_edited;
        this.body = body;
        this.ups = ups;
        this.downs = downs;
        this.score = score;
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public Boolean getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(Boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public Boolean getIs_edited() {
        return is_edited;
    }

    public void setIs_edited(Boolean is_edited) {
        this.is_edited = is_edited;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "RedditComment{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", authorFullName='" + authorFullName + '\'' +
                ", is_blocked=" + is_blocked +
                ", is_edited=" + is_edited +
                ", body='" + body + '\'' +
                ", ups=" + ups +
                ", downs=" + downs +
                ", score=" + score +
                ", kind='" + kind + '\'' +
                '}';
    }
}
