package com.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.model.*;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.python.core.*;
import org.springframework.http.HttpEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
//Install jython 2.7.2 and add the jar file here


interface PyFunc{
    public void makeStixObjects(String x);
}

@Service
@RestController
public class KafkaService {

    private ArrayList<RedditThread> threads= new ArrayList<>();
    private ArrayList<RedditComment> comments = new ArrayList<>();
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> taggedData= new ArrayList<>();
    private ArrayList<ElasticModel> finalObjects=new ArrayList<ElasticModel>();
    public RestTemplate restTemplate = new RestTemplate();
    private JSONObject finalJsonObject = new JSONObject();

    String inferencer="http://127.0.0.1:5000/getInference";
    String getBundle ="http://127.0.0.1:5000/getBundle";
    String makeStix = "http://127.0.0.1:5001/makeStix";
    // Annotation required to listen
    // the message from Kafka server
    @KafkaListener(topics = "reddit-threads",
            groupId = "id", containerFactory
            = "RedditThreadListener")
    public void publish(redditKafka thread) throws JSONException {
//        restTemplate.setErrorHandler(new ErrorHandler());
//        System.out.println("New Entry: " + thread);
        for (int i=0; i<thread.threads.size(); i++){
            threads.add(thread.threads.get(i));
            JSONObject r = new JSONObject();
            String total=thread.threads.get(i).getTitle()+" "+thread.threads.get(i).getSelftext();
            byte[] bytes = total.getBytes();
            String utf_8_string = new String(bytes,StandardCharsets.UTF_8);
            r.put("sentence", utf_8_string);
            if(utf_8_string.equals(total)){
//                System.out.println("MATCHEDDDD");
//                System.out.println(utf_8_string);
//                System.out.println(thread.threads.get(i).getSelftext());
                String[] words = utf_8_string.split("\\s+");
                if(words.length < 300) {
                    try {
                        String s = restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
                        String bundle = restTemplate.postForObject(getBundle, new HttpEntity<>(s), String.class);
                        taggedData.add(convertString(s, "Reddit",total,bundle ));
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
    public void publish(redditKafkaC comment) throws JSONException {
//        System.out.println("New Entry: " + comment);
        for (int i=0; i<comment.comments.size(); i++){
            comments.add(comment.comments.get(i));
            JSONObject r = new JSONObject();
            r.put("sentence", comment.comments.get(i).getBody());

            String s=restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
            String bundle = restTemplate.postForObject(getBundle, new HttpEntity<>(s), String.class);
            taggedData.add(convertString(s, "Reddit", comment.comments.get(i).getBody(), bundle));
        }

    }

    @KafkaListener(topics = "tweets",
    groupId = "id", containerFactory = "tweetListener")
    public void publish(tweetKafka tweet) throws JSONException {

//        System.out.println("New Entry: " + tweet);
        for (int i=0; i<tweet.tweetList.size(); i++){
            tweets.add(tweet.tweetList.get(i));
            JSONObject r = new JSONObject();
            String addTweet = tweet.tweetList.get(i).getBody();
            r.put("sentence", addTweet);
            String[] words = addTweet.split("\\s+");
            if(words.length < 300) {
                try {
                    String s = restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
                    String bundle = restTemplate.postForObject(getBundle, new HttpEntity<>(s), String.class);
                    taggedData.add(convertString(s, "Twitter", addTweet, bundle));
                }
                catch (HttpStatusCodeException e) {
                    System.out.println("Error Found");
                }
            }
        }
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/getInference")
    public String getInferences(String sentence) throws JSONException {
        String s;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject r = new JSONObject();
        r.put("sentence", sentence);
        s=restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
//        System.out.println(s);
        return s;
    }
//    @RequestMapping("/getTaggedData")
//    public void getTaggedData() {
//        System.out.println(taggedData.toString());
//    }



    public LinkedHashMap<String, String> convertString(String s, String source, String text, String bundle) throws JSONException {
        LinkedHashMap<String, String> h = new LinkedHashMap<>();
        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonArrayObjects = jsonObject.getJSONArray("entity tags").getJSONArray(0);
        JSONArray jsonArrayRelations = jsonObject.getJSONArray("relationships");

        // Create an Elastic Model Object
        ElasticModel Obj = new ElasticModel();
        Obj.time = System.currentTimeMillis();
        Obj.source=source;
        Obj.rawText=text;
        Obj.hash=Obj.rawText.hashCode();
        ArrayList<String> malwares = new ArrayList<String>();
        ArrayList<String> vulnerabilities = new ArrayList<String>();
        ArrayList<String> threatactors = new ArrayList<String>();
        ArrayList<String> identities = new ArrayList<String>();
        ArrayList<String> indicators = new ArrayList<String>();
        ArrayList<String> infrastructures = new ArrayList<String>();
        ArrayList<String> tools = new ArrayList<String>();
        ArrayList<String> locations = new ArrayList<String>();
        ArrayList<String> campaigns = new ArrayList<String>();
        // Insert the Named Entities
        for(int i =0; i< jsonArrayObjects.length(); i++){

            if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "M")){
                insertEntity("malware", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                malwares.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "V")){
                insertEntity("vulnerability", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                vulnerabilities.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "TA")){
                insertEntity("threatactor", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                threatactors.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "ID")){
                insertEntity("identity", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                identities.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "IND")){
                insertEntity("indicator", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                indicators.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "INF")){
                insertEntity("infrastructure", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                infrastructures.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "T")){
                insertEntity("tool", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                tools.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "C")){
                insertEntity("campaign", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                campaigns.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "L")) {
                insertEntity("location", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                locations.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
        }

//        // Insert the Relationships between entities
//        for(int i=0;i<jsonArrayRelations.length();i++){
//            insertRelation(jsonArrayRelations.getJSONObject(i).getJSONArray("entities").getString(0),
//                    jsonArrayRelations.getJSONObject(i).getJSONArray("entities").getString(1),
//                    jsonArrayRelations.getJSONObject(i).getString("predicted_relations"),
//                    Obj);
//        }
//        System.out.println(Obj.toString());
        Obj.bundleJson = bundle;
        finalObjects.add(Obj);

        return h; //This is the tagged data, empty right now - not required technically
//        JSONArray a  = new JSONArray(jsonObject.getJSONObject("entity tags"));
//        System.out.println(a);
//        JSONArray x = a.getJSONArray(0);
//        System.out.println(x);
//        JSONArray Words= a.getJSONArray(0);
//        System.out.println(Words.length());
//        for(int i=0; i< Words.length();i++){
//            System.out.println(Words.getString(i));
//        }

//        JSONArray Tags= a.getJSONArray(1);
//        System.out.println(Tags.length());
//        for(int i=0; i< Tags.length();i++){
//            System.out.println(Tags.getString(i));
//        }
//        String[] arrays = s.split("],");
//        arrays[0] = arrays[0].substring(2);
//        arrays[1] = arrays[1].substring(1, arrays[1].length() - 2);



        // OLD CODE - FYP-1 (Below)
//        String[] words= new String[Words.length()];
//        String[] tags = new String[Tags.length()];
//        for (int i=0; i< Words.length(); i++){
//            words[i]=Words.getString(i);
//            tags[i]= Tags.getString(i);
//        }
//        for (int i = 0; i < words.length; i++) {
//            h.put(words[i], tags[i]);
//        }
//        System.out.println("The tagged data is " + h.toString());
//        //create a new object of type elastic model
//        ElasticModel Obj = new ElasticModel();
//        Obj.time = System.currentTimeMillis();
//        Obj.source=source;
//        Obj.rawText=text;
//        Obj.hash=Obj.rawText.hashCode();
//        boolean wordStarted = false;
//        String temp = "";
//        String current = "";
//        //after this a loop will run over the tagged data
//        for (int i = 0; i < words.length; i++) {
////            tags[i]=tags[i].substring(1,tags[i].length()-1);
////            words[i]=words[i].substring(1, words[i].length()-1);
//            if (Objects.equals(tags[i], "B-M")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "malware";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-M")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "B-ID")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "identity";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-ID")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "B-IND")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "indicator";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-IND")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "B-INF")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "infrastructure";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-INF")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "B-TA")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "threatactor";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-TA")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "B-T")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "tool";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-T")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "B-V")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "vulnerability";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-V")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "B-L")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "location";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-L")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "B-C")) {
//                if (wordStarted && i > 0) { //a word was ending just before
//                    insertEntity(current, temp, Obj);
//                    temp = "";
//                    wordStarted = false;
//                }
//                temp += " " + words[i];
//                current = "campaign";
//                wordStarted = true;
//            } else if (Objects.equals(tags[i], "I-C")) {
//                temp += " " + words[i];
//            } else if (Objects.equals(tags[i], "O") && wordStarted) {
//                insertEntity(current, temp, Obj);
//                temp = "";
//                wordStarted = false;
//            }
//        }
//        insertEntity(current, temp, Obj);
//        // it will concatenate whole entities as needed
//
//        //the entities will be added to their respective arrays inside that object
//        //this object will be added to the ArrayList declared above called finalobjects
//        System.out.println(Obj.toString());
//        finalObjects.add(Obj);
//        return h;
    }

    public void insertRelation(String a, String b, String relation,ElasticModel Obj){
        // Make a list of the two identified entities and add their relation to the hashmap
        List<Entity> list = Arrays.asList(new Entity(a), new Entity(b));
        Obj.relation.put(list, relation);
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

    public Boolean checkIfEmpty(ElasticModel obj){
        if (obj.campaigns.size()==0 && obj.vulnerabilities.size()==0 && obj.tools.size()==0 && obj.threatActors.size()==0
        && obj.locations.size()==0 && obj.identities.size()==0 && obj.infrastructures.size()==0
        && obj.indicators.size()==0 && obj.malwares.size()==0){
            return true;
        }
        return false;
    }

    // Add the new data (tagged) to elastic search
    @RequestMapping("/addToElastic")
    public void addToElastic() throws IOException{
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        for (int i=0; i<finalObjects.size(); i++){
            if (!checkIfEmpty(finalObjects.get(i))) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonstring = mapper.writeValueAsString(finalObjects.get(i));
                IndexRequest req = new IndexRequest("tagged_bundle_data")
                        .id(String.valueOf(finalObjects.get(i).hash))
                        .source(jsonstring, XContentType.JSON);
                IndexResponse response = client.index(req, RequestOptions.DEFAULT); //inserting to elasticsearch
//                System.out.println("Response Id: " + response.getId());
            }

        }

    }



    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/correlate")
    public void correlateBundles() throws IOException, JSONException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        
        // fetch all documents in the tagged_bundle_data
        SearchRequest request = new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder= new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        ObjectMapper mapper = new ObjectMapper();
        List<ElasticModel> unMerged = new ArrayList<ElasticModel>();
        for(SearchHit hit : response.getHits().getHits()) {
            JSONObject j = new JSONObject(hit.getSourceAsString());
            ElasticModel e = new ElasticModel();
            e.hash=j.getInt("hash");
            e.source=j.getString("source");
            e.rawText=j.getString("rawText");
            e.time=j.getLong("time");
            e.bundleJson=j.getString("bundleJson");
            unMerged.add(e);
            System.out.println(e.source);
        }
        // clean the bundles
        ArrayList<StixBundle> initials = new ArrayList<>();
        for (int i=0; i< unMerged.size(); i++){
            initials.add(new StixBundle(unMerged.get(i).bundleJson));
            //initials.get(i).print();
        }
        // now get all the existing merged bundles, if any
        GetIndexRequest req = new GetIndexRequest("stix");
        boolean exists = client.indices().exists(req, RequestOptions.DEFAULT);

        ArrayList<StixBundle> finals= new ArrayList<>();

        if (!exists){
            finals.addAll(initials);
        }
        else {
            SearchRequest request2 = new SearchRequest("stix");
            SearchSourceBuilder searchSourceBuilder2 = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            request2.source(searchSourceBuilder2);
            SearchResponse response2 = client.search(request2, RequestOptions.DEFAULT);

            for (SearchHit hit : response2.getHits().getHits()) {
                JSONObject j = new JSONObject(hit.getSourceAsString());
                finals.add(new StixBundle(j.getString("bundle")));
            }
        }
        // now initials has just the new bundles and finals has old + new bundles

        for (StixBundle initial : initials) {
            for (StixBundle aFinal : finals) {

                for (SDO ent1 : initial.entities) {
                    for (SDO ent2 : aFinal.entities) {
                        if (ent1.type.equals("malware") || ent1.type.equals("identity")
                                || ent1.type.equals("threat-actor") || (ent2.type.equals("malware")
                                || ent2.type.equals("identity") || ent2.type.equals("threat-actor"))) {

                            if (ent1.name.equals(ent2.name)) { //use levenshtein distance here
                                // add all entities and relationships of initialBundle to finalBundle
                                for (SDO ent : initial.entities)
                                    aFinal.addEntity(ent);
                                for (SRO rel : initial.relationships)
                                    aFinal.addRelationship(rel);
                            }
                        }
                    }
                }

            }
        }

        // now finals has all the final stix bundles, these will be converted to stix via python and added to elastic

        //emptying the previous index to only have new merged ones now
        if (exists) {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("stix");
            AcknowledgedResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        }

        int i=0;
        for (StixBundle finalBundle: finals){
            i++;
            ObjectMapper Nmapper = new ObjectMapper();
            String r = Nmapper.writeValueAsString(finalBundle);
            try {
                String s = restTemplate.postForObject(makeStix, new HttpEntity<>(r.toString()), String.class);
                System.out.println("*******************");
                System.out.println(s);
                Stix toUpload = new Stix();
                toUpload.bundle=s;
                String jsonstring = Nmapper.writeValueAsString(toUpload);
                IndexRequest rQ = new IndexRequest("stix")
                        .id(String.valueOf(i))
                        .source(jsonstring, XContentType.JSON);
                IndexResponse res = client.index(rQ, RequestOptions.DEFAULT);
                System.out.println("Response Id: " + res.getId());

            } catch (HttpStatusCodeException e) {
                System.out.println("Error Occurred in Making Stix");
            }
        }
        // all the docs with same id as the unmerged ones will get a field telling that theyve been merged

    }


    // Old function
    @RequestMapping("/pushToElastic")
    public String pushToElastic() throws IOException {
        String s="";
        //this function will create connection to elasticsearch
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        // a loop will run over the finalObjects ArrayList
        for (int i=0; i<finalObjects.size(); i++){
            if (!checkIfEmpty(finalObjects.get(i))) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonstring = mapper.writeValueAsString(finalObjects.get(i));
                IndexRequest req = new IndexRequest("tagged_data")
                        .id(String.valueOf(finalObjects.get(i).hash))
                        .source(jsonstring, XContentType.JSON);
                IndexResponse response = client.index(req, RequestOptions.DEFAULT); //inserting to elasticsearch
//                System.out.println("Response Id: " + response.getId());
            }

        }
        // then for each index, using object mapper as in FileAccess.java will map that to json
        //that json will be added to elasticsearch
        // at the end display success?
        return s;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/viewRawTweets")
    public ArrayList<Tweet> getTweets() throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString = mapper.writeValueAsString(tweets);
//        return jsonString;
        return tweets;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/viewRawThreads")
    public ArrayList<RedditThread> getThreads() throws JsonProcessingException {
        return threads;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/viewRawComments")
    public ArrayList<RedditComment> getComments() throws JsonProcessingException {
        return comments;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping("/viewTaggedData")
    public ArrayList<ElasticModel> getTagged() throws JsonProcessingException {
        return finalObjects;
    }

}
