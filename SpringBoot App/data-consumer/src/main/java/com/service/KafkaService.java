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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.service.ErrorHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.*;

@Service
@RestController
public class KafkaService {

    private ArrayList<RedditThread> threads= new ArrayList<>(); // to store incoming reddit threads
    private ArrayList<RedditComment> comments = new ArrayList<>(); // to store incoming reddit comments
    private ArrayList<Tweet> tweets = new ArrayList<>(); // to store incoming tweets
    private ArrayList<LinkedHashMap<String, String>> taggedData= new ArrayList<>(); // to maintain words and tags
    private ArrayList<ElasticModel> finalObjects=new ArrayList<ElasticModel>(); // to store mapped data to push to elastic
    public RestTemplate restTemplate = new RestTemplate(); // to send post request
    String inferencer="http://127.0.0.1:5000/getInference"; // url to access BERT Inferencer

    // Annotation required to listen
    // the message from Kafka server
    @KafkaListener(topics = "reddit-threads",
            groupId = "id", containerFactory
            = "RedditThreadListener")
    public void publish(redditKafka thread) throws JSONException { // listening for messages on reddit-threads
        // the constructor in redditKafka automatically maps incoming message into an array of 'RedditThread'
//        restTemplate.setErrorHandler(new ErrorHandler());
        System.out.println("New Entry: " + thread);
        for (int i=0; i<thread.threads.size(); i++){
            threads.add(thread.threads.get(i)); // store all incoming threads
            JSONObject r = new JSONObject();
            String total=thread.threads.get(i).getTitle()+" "+thread.threads.get(i).getSelftext(); // extract title and text
            byte[] bytes = total.getBytes();
            String utf_8_string = new String(bytes,StandardCharsets.UTF_8); //convert to utf_8
            r.put("sentence", utf_8_string);
            if(utf_8_string.equals(total)){ // confirm string is utf_8
//                System.out.println("MATCHEDDDD");
//                System.out.println(utf_8_string);
//                System.out.println(thread.threads.get(i).getSelftext());
                String[] words = utf_8_string.split("\\s+");
                if(words.length < 300) {
                    try {
                        String s = restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class); // send to inferencer
                        System.out.println(s);
                        taggedData.add(convertString(s, "Reddit",total )); // parse tags and words , map onto ElasticModel, store
                    } catch (HttpStatusCodeException e) {
                        System.out.println("Error Found");
                    }
                }

            }

        }
    }

    @KafkaListener(topics = "reddit-comments",
            groupId = "id", containerFactory
            = "RedditCommentListener")
    public void publish(redditKafkaC comment) throws JSONException { // listening for messages on reddit-comments
        // the constructor in redditKafkaC automatically maps incoming message into an array of 'RedditComment'
        System.out.println("New Entry: " + comment);
        for (int i=0; i<comment.comments.size(); i++){
            comments.add(comment.comments.get(i)); // store each comment
            JSONObject r = new JSONObject();
            r.put("sentence", comment.comments.get(i).getBody()); // extract text

            String s=restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class); // send to inferencer to get tags
            System.out.println(s);
            taggedData.add(convertString(s, "Reddit", comment.comments.get(i).getBody())); // map onto ElasticModel and store
        }

    }

    @KafkaListener(topics = "tweets",
    groupId = "id", containerFactory = "tweetListener")
    public void publish(tweetKafka tweet) throws JSONException { // listening for messages on tweets
        // the constructor in tweetKafka automatically maps incoming message into an array of 'Tweet'
        System.out.println("New Entry: " + tweet);
        for (int i=0; i<tweet.tweetList.size(); i++){
            tweets.add(tweet.tweetList.get(i)); //store each tweet
            JSONObject r = new JSONObject();
            String addTweet = tweet.tweetList.get(i).getBody(); // extract text
            r.put("sentence", addTweet);
            String[] words = addTweet.split("\\s+");
            if(words.length < 300) {
                try {
                    String s = restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class); // send to inferencer to get tags
                    System.out.println(s);
                    taggedData.add(convertString(s, "Twitter", addTweet)); // map onto ElasticModel and store
                }
                catch (HttpStatusCodeException e) {
                    System.out.println("Error Found");
                }
            }
        }
    }
    // endpoint to get tags for given sentence
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/getInference")
    public String getInferences(String sentence) throws JSONException {
        String s;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject r = new JSONObject();
        r.put("sentence", sentence);
        s=restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class); // send sentence to inferencer to get tags
        System.out.println(s);
        return s; // return the words and respective tags
    }



    // function to parse array string of words and respective tags then map onto elasticModel
    public LinkedHashMap<String, String> convertString(String s, String source, String text) throws JSONException {
        LinkedHashMap<String, String> h = new LinkedHashMap<>();
        JSONArray a= new JSONArray(s); //convert string to JSON Array
//        System.out.println(a.length());
        JSONArray Words= a.getJSONArray(0); // get array of words
//        System.out.println(Words.length());
//        for(int i=0; i< Words.length();i++){
//            System.out.println(Words.getString(i));
//        }

        JSONArray Tags= a.getJSONArray(1); // get array of tags
//        System.out.println(Tags.length());
//        for(int i=0; i< Tags.length();i++){
//            System.out.println(Tags.getString(i));
//        }
//        String[] arrays = s.split("],");
//        arrays[0] = arrays[0].substring(2);
//        arrays[1] = arrays[1].substring(1, arrays[1].length() - 2);
        String[] words= new String[Words.length()]; // convert JSON Array to Java Array
        String[] tags = new String[Tags.length()]; // convert JSON Array to Java Array
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
        Obj.time = System.currentTimeMillis(); // get time stamp of data being uploaded
        Obj.source=source; // assign source
        Obj.rawText=text; // assign original raw text
        Obj.hash=Obj.rawText.hashCode(); // generate hash code to be used as ID
        boolean wordStarted = false;
        String temp = "";
        String current = "";
        // loop will over the tagged data
        for (int i = 0; i < words.length; i++) {
//            tags[i]=tags[i].substring(1,tags[i].length()-1);
//            words[i]=words[i].substring(1, words[i].length()-1);
            if (Objects.equals(tags[i], "B-M")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj); // insert completed word into the object
                    temp = ""; // empty buffer
                    wordStarted = false;
                }
                temp += " " + words[i]; // concatenate
                current = "malware"; // currently parsing a malware object
                wordStarted = true;
            } else if (Objects.equals(tags[i], "I-M")) {
                temp += " " + words[i];
            } else if (Objects.equals(tags[i], "B-ID")) {
                if (wordStarted && i > 0) { //a word was ending just before
                    insertEntity(current, temp, Obj); // insert completed word into the object
                    temp = ""; // empty buffer
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

    // function to insert string into elasticModel based on what entity it is
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

    // check if all arrays in an ElasticModel object are empty
    public Boolean checkIfEmpty(ElasticModel obj){
        if (obj.campaigns.size()==0 && obj.vulnerabilities.size()==0 && obj.tools.size()==0 && obj.threatActors.size()==0
        && obj.locations.size()==0 && obj.identities.size()==0 && obj.infrastructures.size()==0
        && obj.indicators.size()==0 && obj.malwares.size()==0){
            return true;
        }
        return false;
    }

    // endpoint to push all mapped and stored elasticModel objects onto elasticsearch
    @RequestMapping("/pushToElastic")
    public String pushToElastic() throws IOException {
        String s="";
        //create connection to elasticsearch
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));


        // a loop will run over the finalObjects ArrayList
        for (int i=0; i<finalObjects.size(); i++){
            if (!checkIfEmpty(finalObjects.get(i))) { // if object is not empty
                ObjectMapper mapper = new ObjectMapper();
                String jsonstring = mapper.writeValueAsString(finalObjects.get(i)); // map as a json
                IndexRequest req = new IndexRequest("tagged_data")
                        .id(String.valueOf(finalObjects.get(i).hash)) // assign hash as id
                        .source(jsonstring, XContentType.JSON);
                IndexResponse response = client.index(req, RequestOptions.DEFAULT); //inserting to elasticsearch
                System.out.println("Response Id: " + response.getId());
            }

        }
        return s;
    }

    // endpoint to view raw tweets
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/viewRawTweets")
    public ArrayList<Tweet> getTweets() throws JsonProcessingException {
        return tweets;
    }

    // endpoint to view raw threads
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/viewRawThreads")
    public ArrayList<RedditThread> getThreads() throws JsonProcessingException {
        return threads;
    }

    // endpoint to view raw tweets
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/viewRawComments")
    public ArrayList<RedditComment> getComments() throws JsonProcessingException {
        return comments;
    }

    // endpoint to view tagged data
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/viewTaggedData")
    public ArrayList<ElasticModel> getTagged() throws JsonProcessingException {
        return finalObjects;
    }

}
