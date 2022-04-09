package com.model;

import java.util.ArrayList;

public class Stix {
    public String bundle="";

    @Override
    public String toString() {
        return "{" +
                "bundle:'" + bundle + '\'' +
                ", ids:" + ids +
                '}';
    }

    public ArrayList<Integer> ids = new ArrayList<>();
}
