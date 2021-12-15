package com.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.*;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
@RestController
public class KafkaService {

    private ArrayList<RedditThread> threads= new ArrayList<>();
    private ArrayList<RedditComment> comments = new ArrayList<>();
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> taggedData= new ArrayList<>();
    private ArrayList<ElasticModel> finalObjects=new ArrayList<ElasticModel>();
    private RestTemplate restTemplate = new RestTemplate();

    String inferencer="http://127.0.0.1:5000/getInference";
    // Annotation required to listen
    // the message from Kafka server
    @KafkaListener(topics = "reddit-threads",
            groupId = "id", containerFactory
            = "RedditThreadListener")
    public void publish(redditKafka thread) throws JSONException {
        System.out.println("New Entry: " + thread);
        for (int i=0; i<thread.threads.size(); i++){
            threads.add(thread.threads.get(i));
//            JSONObject r = new JSONObject();
//            r.put("sentence", thread.threads.get(i).getSelftext());
//            String s=restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
//            System.out.println(s);
//            taggedData.add(convertString(s));
        }
    }

    @KafkaListener(topics = "reddit-comments",
            groupId = "id", containerFactory
            = "RedditCommentListener")
    public void publish(redditKafkaC comment) throws JSONException {
        System.out.println("New Entry: " + comment);
        for (int i=0; i<comment.comments.size(); i++){
            comments.add(comment.comments.get(i));
            JSONObject r = new JSONObject();
            r.put("sentence", comment.comments.get(i).getBody());
            String s=restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
            System.out.println(s);
            taggedData.add(convertString(s));
        }

    }

    @KafkaListener(topics = "tweets",
    groupId = "id", containerFactory = "tweetListener")
    public void publish(tweetKafka tweet) throws JSONException {

        System.out.println("New Entry: " + tweet);
        for (int i=0; i<tweet.tweetList.size(); i++){
            tweets.add(tweet.tweetList.get(i));
            JSONObject r = new JSONObject();
            r.put("sentence", tweet.tweetList.get(i).getBody());
            String s=restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
            System.out.println(s);
            taggedData.add(convertString(s));
        }
    }
    @RequestMapping("/getInference")
    public String getInferences() throws JSONException {
        String s;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject r = new JSONObject();
        r.put("sentence", tweets.get(2).getBody());
        s=restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
        System.out.println(s);
        return s;
    }
    @RequestMapping("/getTaggedData")
    public void getTaggedData() {
        System.out.println(taggedData.toString());
    }
    public LinkedHashMap<String, String> convertString(String s) throws JSONException {
        LinkedHashMap<String, String> h = new LinkedHashMap<>();
        JSONArray a= new JSONArray(s);

        JSONArray Words= a.getJSONArray(0);
        JSONArray Tags= a.getJSONArray(1);
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
            tags[i]=tags[i].substring(1,tags[i].length()-1);
            words[i]=words[i].substring(1, words[i].length()-1);
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
        finalObjects.add(Obj);
        return h;
    }

    public void insertEntity(String current, String temp, ElasticModel Obj){
        if (Objects.equals(current, "malware"))
            Obj.malwares.add(new Entity(temp));
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

    @RequestMapping("/pushToElastic")
    public String pushToElastic() throws IOException {
        String s="";
        //this function will create connection to elasticsearch
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        // a loop will run over the finalObjects ArrayList
        for (int i=0; i<finalObjects.size(); i++){
            ObjectMapper mapper= new ObjectMapper();
            String jsonstring=mapper.writeValueAsString(finalObjects.get(i));
            IndexRequest req= new IndexRequest("taggedData")
                    //.id(new_cve.id)
                    .source(jsonstring, XContentType.JSON);
            IndexResponse response=client.index(req, RequestOptions.DEFAULT); //inserting to elasticsearch
            System.out.println("Response Id: "+response.getId());

        }
        // then for each index, using object mapper as in FileAccess.java will map that to json
        //that json will be added to elasticsearch
        // at the end display success?
        return s;
    }
}
