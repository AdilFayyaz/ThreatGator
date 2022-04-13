package com.example.threatprioritization.model;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StixBundle {
    public Integer hash;
    public ArrayList<SDO> entities = new ArrayList<>();
    public ArrayList<SRO> relationships = new ArrayList<>();
    public boolean standalone;
    public ArrayList<Integer> mergedReports= new ArrayList<>();
    public String bundleString;
    public long time=0;

    public StixBundle(String b, Integer hash, long time) throws JSONException {

        this.time=time;
        this.hash=hash;
        b=StringEscapeUtils.unescapeJava(b);

        if (b.charAt(0) == '"')
            b=b.substring(1, b.length()-1);
        this.bundleString = b;
        // System.out.println(b);

        JSONObject bundle = new JSONObject(b);
        if (bundle.has("objects")) {
            JSONArray objects = bundle.getJSONArray("objects");
            for (int i = 0; i < objects.length(); i++) {
                if (!objects.getJSONObject(i).getString("type").equals("relationship")) { // if it's an SDO
                    if (!exists(objects.getJSONObject(i).getString("name"))) { //if not already added
                        entities.add(new SDO(objects.getJSONObject(i).getString("id"), objects.getJSONObject(i).getString("name"), objects.getJSONObject(i).getString("type")));
                    } else { //exists but has a different id
                        entities.get(get(objects.getJSONObject(i).getString("name"))).ids.add(objects.getJSONObject(i).getString("id")); // add id to that object
                    }
                }
            }
            for (int i = 0; i < objects.length(); i++) {
                if (objects.getJSONObject(i).getString("type").equals("relationship")) { //if its an SRO
                    if (!exists(objects.getJSONObject(i).getString("source_ref"), objects.getJSONObject(i).getString("target_ref"))) {
                        relationships.add(new SRO(objects.getJSONObject(i).getString("relationship_type"), getName(objects.getJSONObject(i).getString("source_ref")),
                                getName(objects.getJSONObject(i).getString("target_ref"))
                        ));
                    }
                }
            }
        }
    }

    public StixBundle(String b, Integer hash) throws JSONException {

        this.time=time;
        this.hash=hash;
        b=StringEscapeUtils.unescapeJava(b);

        if (b.charAt(0) == '"')
            b=b.substring(1, b.length()-1);
        this.bundleString = b;
        // System.out.println(b);

        JSONObject bundle = new JSONObject(b);
        if (bundle.has("objects")) {
            JSONArray objects = bundle.getJSONArray("objects");
            for (int i = 0; i < objects.length(); i++) {
                if (!objects.getJSONObject(i).getString("type").equals("relationship")) { // if it's an SDO
                    if (!exists(objects.getJSONObject(i).getString("name"))) { //if not already added
                        entities.add(new SDO(objects.getJSONObject(i).getString("id"), objects.getJSONObject(i).getString("name"), objects.getJSONObject(i).getString("type")));
                    } else { //exists but has a different id
                        entities.get(get(objects.getJSONObject(i).getString("name"))).ids.add(objects.getJSONObject(i).getString("id")); // add id to that object
                    }
                }
            }
            for (int i = 0; i < objects.length(); i++) {
                if (objects.getJSONObject(i).getString("type").equals("relationship")) { //if its an SRO
                    if (!exists(objects.getJSONObject(i).getString("source_ref"), objects.getJSONObject(i).getString("target_ref"))) {
                        relationships.add(new SRO(objects.getJSONObject(i).getString("relationship_type"), getName(objects.getJSONObject(i).getString("source_ref")),
                                getName(objects.getJSONObject(i).getString("target_ref"))
                        ));
                    }
                }
            }
        }
    }

    public StixBundle(StixBundle stixBundle){
        this.bundleString = stixBundle.bundleString;
        this.hash = stixBundle.hash;
        this.entities.addAll(stixBundle.entities);
        this.relationships.addAll(stixBundle.relationships);
    }

    public void addEntity(SDO s){
        if (!exists(s.name)) { //if not already added
            entities.add(s);
        } else { //exists but has a different id
            entities.get(get(s.name)).ids.addAll(s.ids); // add id to that object
        }
    }

    public boolean hasRelationship(SDO s){
        for (SRO rel : relationships){
            if (rel.target.equals(s.name) || rel.source.equals(s.name))
                return true;
        }
        return false;
    }

    public void deleteRelationship(SRO r){
        relationships.remove(r);
    }
    public void addEntity(SDO s, Integer hash){
        if (!exists(s.name)) { //if not already added
            entities.add(s);
        } else { //exists but has a different id
            entities.get(get(s.name)).ids.addAll(s.ids); // add id to that object
        }
        if (!mergedReports.contains(hash)){
            mergedReports.add(hash);
        }
    }

    public void deleteEntity(String name){
        for (int i=0; i< entities.size(); i++){
            if (entities.get(i).name.equals(name)){
                entities.remove(i);
            }
        }

        for (int i=0; i< relationships.size(); i++){
            if (relationships.get(i).target.equals(name) || relationships.get(i).source.equals(name)){
                relationships.remove(i);
            }
        }
    }

    public void editEntity(String name, String newType){
        for (int i=0; i< entities.size(); i++){
            if (entities.get(i).name.equals(name)){
                entities.get(i).type=newType;
            }
        }
    }
    public String getType(String name){
        for (int i=0; i< entities.size(); i++){
            if (entities.get(i).name.equals(name)){
                return entities.get(i).type;
            }
        }
        return "";
    }
    public void addRelationship(SRO s){
        if (!existsN(s.source, s.target)) {
            relationships.add(s);

        }
    }

    public void addRelationship(SRO s, Integer hash){
        if (!existsN(s.source, s.target)) {
            relationships.add(s);
            if (!mergedReports.contains(hash))
                mergedReports.add(hash);
        }
    }



    boolean exists(String name){
        for(int i=0; i< entities.size(); i++){
            if (entities.get(i).name.equals(name)){
                return true;
            }
        }
        return false;
    }

    boolean exists(String src, String dest){
        for (int i=0; i< relationships.size(); i++){

            if (getName(src).equals(relationships.get(i).source) && getName(dest).equals(relationships.get(i).target)  )
                return true;
        }
        return false;
    }

    boolean existsN(String src, String dest){
        for (int i=0; i< relationships.size(); i++){

            if (src.equals(relationships.get(i).source) && dest.equals(relationships.get(i).target)  )
                return true;
        }
        return false;
    }

    int get(String name) {
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    String getName(String id){
        for (int i=0; i<entities.size(); i++){
            if (entities.get(i).ids.contains(id))
                return entities.get(i).name;
        }
        return "";
    }

    public void print(){
        for (int i=0; i<entities.size(); i++)
            System.out.println(entities.get(i).name);

        for (int i=0; i<relationships.size(); i++)
            System.out.println(relationships.get(i).source + " " + relationships.get(i).target+" "
            + relationships.get(i).name);
    }
}
