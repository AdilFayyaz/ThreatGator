package com.testing;

import com.model.ElasticModel;
import com.model.Entity;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Objects;


public class Main {
    public LinkedHashMap<String, String> convertString(String s) throws JSONException {
        LinkedHashMap<String, String> h = new LinkedHashMap<>();
        JSONArray a= new JSONArray(s);
//        System.out.println(a.length());
        JSONArray Words= a.getJSONArray(0);
        System.out.println(Words.length());
//        for(int i=0; i< Words.length();i++){
//            System.out.println(Words.getString(i));
//        }

        JSONArray Tags= a.getJSONArray(1);
        System.out.println(Tags.length());
//        for(int i=0; i< Tags.length();i++){
//            System.out.println(Tags.getString(i));
//        }
//        String[] arrays = s.split("],");
//        arrays[0] = arrays[0].substring(2);
//        arrays[1] = arrays[1].substring(1, arrays[1].length() - 2);
        String[] words= new String[Words.length()];
        String[] tags = new String[Tags.length()];
        for (int i=0; i< Words.length(); i++){
            words[i]=Words.getString(i);
            tags[i]= Tags.getString(i);
        }
        for (int i = 0; i < words.length; i++) {
            h.put(words[i], tags[i]);
        }
        System.out.println("The tagged data is " + h.toString());
        //create a new object of type elastic model
        ElasticModel Obj = new ElasticModel();
        boolean wordStarted = false;
        String temp = "";
        String current = "";
        //after this a loop will run over the tagged data
        for (int i = 0; i < words.length; i++) {
//            tags[i]=tags[i].substring(1,tags[i].length()-1);
//            words[i]=words[i].substring(1, words[i].length()-1);
            if (Objects.equals(tags[i], "B-M")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "malware";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-M")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-ID")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "identity";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-ID")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-IND")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "indicator";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-IND")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-INF")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "infrastructure";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-INF")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-TA")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "threatactor";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-TA")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-T")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "tool";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-T")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-V")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "vulnerability";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-V")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-L")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "location";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-L")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-C")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj);
                    temp = "";
                    wordStarted = false;
                }
                temp += " " + words[i];
                current = "campaign";
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-C")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "O") && wordStarted) {
                insertEntity(current, temp, Obj);
                temp = "";
                wordStarted = false;
            }
        }
        insertEntity(current, temp, Obj);
        // it will concatenate whole entities as needed

        //the entities will be added to their respective arrays inside that object
        //this object will be added to the ArrayList declared above called finalobjects
        System.out.println(Obj.toString());
//        finalObjects.add(Obj);
        return h;
    }

    public void insertEntity(String current, String temp, ElasticModel Obj) {
        if (Objects.equals(current, "malware")) {
            System.out.println("Malware ending");
            Obj.malwares.add(new Entity(temp));
        }
        else if (Objects.equals(current, "indicator"))
            Obj.indicators.add(new Entity(temp));
        else if (Objects.equals(current, "infrastructure"))
            Obj.infrastructures.add(new Entity(temp));
        else if (Objects.equals(current, "identity"))
            Obj.identities.add(new Entity(temp));
        else if (Objects.equals(current, "location"))
            Obj.locations.add(new Entity(temp));
        else if (Objects.equals(current, "threatactor"))
            Obj.threatActors.add(new Entity(temp));
        else if (Objects.equals(current, "tool"))
            Obj.tools.add(new Entity(temp));
        else if (Objects.equals(current, "vulnerability"))
            Obj.vulnerabilities.add(new Entity(temp));
        else if (Objects.equals(current, "campaign"))
            Obj.campaigns.add(new Entity(temp));
    }

    public static void main(String args[]) throws JSONException {
        String s= "[[\"All\",\"jokes\",\"to\",\"the\",\"side\",\"this\",\"is\",\"a\",\"generalist\",\"post\",\"and\",\"I've\",\"tried\",\"finding\",\"the\",\"suitor\",\"on\",\"the\",\"internet\",\"for\",\"this\",\"exploit\",\"and\",\"can't\",\"find\",\"legit\",\"threat\",\"actors.\",\"So\",\"my\",\"question\",\"is\",\"WHO\",\"and\",\"WHY\",\"would\",\"someone\",\"want\",\"to\",\"hack\",\"MineCraft\",\"out\",\"of\",\"all\",\"games\",\"?\",\"Is\",\"it\",\"someone\",\"that\",\"was\",\"bored\",\"and\",\"testing\",\"their\",\"skilkset?\",\"Maybe\",\"disgruntled\",\"employees\",\"or\",\"ex\",\"employees?\",\"Someone\",\"that\",\"got\",\"mad\",\"over\",\"the\",\"outcome\",\"of\",\"playing\",\"the\",\"game\",\"?\",\"What\",\"reasoning\",\"would\",\"someone\",\"have\",\"?\",\"Can\",\"this\",\"exploit\",\"lead\",\"to\",\"randsomware\",\"or\",\"something?\",\"This\",\"game\",\"came\",\"out\",\"2011\",\"and\",\"although\",\"it\",\"is\",\"very\",\"popular\",\"its\",\"moreso\",\"an\",\"outdated\",\"game\",\"and\",\"only\",\"people\",\"really\",\"playing\",\"it\",\"are\",\"kids\",\"and\",\"loyalist.....\",\"This\",\"maybe\",\"a\",\"biased\",\"opinion\",\"but\",\"I\",\"don't\",\"think\",\"people\",\"are\",\"waking\",\"up\",\"in\",\"2021\",\"saying\",\"\\\"hey\",\"I\",\"have\",\"to\",\"get\",\"Minecraft\",\"it\",\"is\",\"a\",\"must\",\"have\",\"game\",\"\\\"\"],[\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"B-M\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"B-M\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"B-INF\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\",\"O\"]]";
        Main m = new Main();
        m.convertString(s);
    }
}


