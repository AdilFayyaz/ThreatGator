package com.model;

public class SRO {
    public String name = "";
    public String source = "";
    public String target = "";

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", source:'" + source + '\'' +
                ", target:'" + target + '\'' +
                '}';
    }

    public SRO(String name, String source, String target) {
        this.name = name;
        this.source = source;
        this.target = target;
    }
}
