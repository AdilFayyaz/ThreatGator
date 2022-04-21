package com.threatgator.heterogeneity.model;

// Model Class for Accounts

public class Account {

    protected int id_; // Primary key in database

    private transient int source_id; // will be fk in source table

    @Override
    public String toString() {
        return "Account{" +
                "id_=" + id_ +
                ", source_id=" + source_id +
                ", source=" + source +
                ", handle='" + handle + '\'' +
                ", username='" + username + '\'' +
                ", isHandle='" + isHandle + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
// defined relationship

    public Source source;

    // Protected variables
    protected String handle;
    protected String username;
    protected String isHandle;
    protected String Date;

    //Getter and Setters
    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Account() {

    }
}
