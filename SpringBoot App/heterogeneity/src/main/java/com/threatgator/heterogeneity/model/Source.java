package com.threatgator.heterogeneity.model;
import java.util.List;

// Source Model Class

public class Source {
    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dataFormat='" + dataFormat + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", url='" + url + '\'' +
                ", accountsList=" + accountsList +
                '}';
    }

    protected int id; // Primary key
    // Other table entries
    protected String name;
    protected String dataFormat;
    protected String sourceType;
    protected String url;
    // defined relationship

//    @JoinColumn(name = "src_ac_fk",referencedColumnName = "id")
    private List<Account> accountsList;

    public Source() {

    }
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
