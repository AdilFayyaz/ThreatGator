package com.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.model.*;
import org.apache.http.HttpHost;
import org.apache.lucene.search.spell.LevenshteinDistance;
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
    private static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addParameter("size", "2000");

        COMMON_OPTIONS = builder.build();
    }

    private ArrayList<RedditThread> threads= new ArrayList<>();
    private ArrayList<RedditComment> comments = new ArrayList<>();
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private ArrayList<APTSecureList> aptSecureLists = new ArrayList<>();
    private ArrayList<MalwareSecureList> malSecureLists = new ArrayList<>();
    private ArrayList<SpamSecureList> spamSecureLists = new ArrayList<>();
    private ArrayList<IncidentSecureList> incSecureLists = new ArrayList<>();
    private ArrayList<LinkedHashMap<String, String>> taggedData= new ArrayList<>();
    private ArrayList<ElasticModel> finalObjects=new ArrayList<ElasticModel>();
    public RestTemplate restTemplate = new RestTemplate();
    private JSONObject finalJsonObject = new JSONObject();

    String inferencer="http://127.0.0.1:5000/getInference";
    String getBundle ="http://127.0.0.1:5000/getBundle";
    String makeStix = "http://127.0.0.1:5000/makeStix";
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

    @KafkaListener(topics= "APTsecurelist", groupId = "id",
            containerFactory = "APTSecureListListener")
    public void publish(APTSecureListKafka apt) throws JSONException{
        for(int i=0; i<apt.APTList.size();i++){
            aptSecureLists.add(apt.APTList.get(i));
            JSONObject r = new JSONObject();
            String addAPT = apt.APTList.get(i).getBody();
            byte[] bytes = addAPT.getBytes(StandardCharsets.UTF_8);
            String utf8String = new String(bytes);
            String normalized;
            normalized = utf8String.replace("\\","");
            normalized = normalized.replaceAll("0x*", "");
            normalized = normalized.replaceAll("[^a-zA-Z0-9_ !@#$%^&*()-<>:;.~=]+", "");
            r.put("sentence", normalized);

            System.out.println(r);
            String[] words = addAPT.split("\\s+");
//            System.out.println(addAPT);
            if (words.length < 512){
                try{
                    String s = restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
                    System.out.println(s);
                    String bundle = restTemplate.postForObject(getBundle, new HttpEntity<>(s), String.class);
                    taggedData.add(convertString(s, "APTSecureList", normalized, bundle));
                }
                catch (HttpStatusCodeException e) {
                    System.out.println("Error Found");
                }
            }
        }
    }


    @KafkaListener(topics= "Malwaresecurelist", groupId = "id",
            containerFactory = "MalwareSecureListListener")
    public void publish(MalwareSecureListKafka mal) throws JSONException{
        for(int i=0; i<mal.MalwareList.size();i++){
            malSecureLists.add(mal.MalwareList.get(i));
            JSONObject r = new JSONObject();
            String addMalware = mal.MalwareList.get(i).getBody();
            byte[] bytes = addMalware.getBytes(StandardCharsets.UTF_8);
            String utf8String = new String(bytes);
            String normalized;
            normalized = utf8String.replace("\\","");
            normalized = normalized.replaceAll("0x*", "");
            normalized = normalized.replaceAll("[^a-zA-Z0-9_ !@#$%^&*()-<>:;.~=]+", "");
            r.put("sentence", normalized);

            System.out.println(r);
            String[] words = addMalware.split("\\s+");
//            System.out.println(addAPT);
            if (words.length < 512){
                try{
                    String s = restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
                    System.out.println(s);
                    String bundle = restTemplate.postForObject(getBundle, new HttpEntity<>(s), String.class);
                    taggedData.add(convertString(s, "MalwareSecureList", normalized, bundle));
                }
                catch (HttpStatusCodeException e) {
                    System.out.println("Error Found");
                }
            }
        }
    }

    @KafkaListener(topics= "Spamsecurelist", groupId = "id",
            containerFactory = "SpamSecureListListener")
    public void publish(SpamSecureListKafka spam) throws JSONException{
        for(int i=0; i<spam.SpamList.size();i++){
            spamSecureLists.add(spam.SpamList.get(i));
            JSONObject r = new JSONObject();
            String addSpam = spam.SpamList.get(i).getBody();
            byte[] bytes = addSpam.getBytes(StandardCharsets.UTF_8);
            String utf8String = new String(bytes);
            String normalized;
            normalized = utf8String.replace("\\","");
            normalized = normalized.replaceAll("0x*", "");
            normalized = normalized.replaceAll("[^a-zA-Z0-9_ !@#$%^&*()-<>:;.~=]+", "");
            r.put("sentence", normalized);

            System.out.println(r);
            String[] words = addSpam.split("\\s+");
//            System.out.println(addAPT);
            if (words.length < 512){
                try{
                    String s = restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
                    System.out.println(s);
                    String bundle = restTemplate.postForObject(getBundle, new HttpEntity<>(s), String.class);
                    taggedData.add(convertString(s, "SpamSecureList", normalized, bundle));
                }
                catch (HttpStatusCodeException e) {
                    System.out.println("Error Found");
                }
            }
        }
    }

    @KafkaListener(topics= "Incidentsecurelist", groupId = "id",
            containerFactory = "IncidentSecureListListener")
    public void publish(IncidentSecureListKafka inc) throws JSONException{
        for(int i=0; i<inc.IncidentList.size();i++){
            incSecureLists.add(inc.IncidentList.get(i));
            JSONObject r = new JSONObject();
            String addIncident = inc.IncidentList.get(i).getBody();
            byte[] bytes = addIncident.getBytes(StandardCharsets.UTF_8);
            String utf8String = new String(bytes);
            String normalized;
            normalized = utf8String.replace("\\","");
            normalized = normalized.replaceAll("0x*", "");
            normalized = normalized.replaceAll("[^a-zA-Z0-9_ !@#$%^&*()-<>:;.~=]+", "");
            r.put("sentence", normalized);

            System.out.println(r);
            String[] words = addIncident.split("\\s+");
//            System.out.println(addAPT);
            if (words.length < 512){
                try{
                    String s = restTemplate.postForObject(inferencer, new HttpEntity<>(r.toString()), String.class);
                    System.out.println(s);
                    String bundle = restTemplate.postForObject(getBundle, new HttpEntity<>(s), String.class);
                    taggedData.add(convertString(s, "IncidentSecureList", normalized, bundle));
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

    public boolean found(ArrayList<Entity> array, String x){
        for (Entity entity : array) {
            if (entity.name.equals(x)) {
                return true;
            }
        }
        return false;
    }
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
        ArrayList<String> attackPatterns = new ArrayList<>();
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
            
            else if(Objects.equals(jsonArrayObjects.getJSONArray(i).getString(2), "AP")) {
                insertEntity("attackPattern", jsonArrayObjects.getJSONArray(i).getString(1), Obj);
                attackPatterns.add(jsonArrayObjects.getJSONArray(i).getString(1));
            }
        }

        Obj.bundleJson = bundle;
        finalObjects.add(Obj);

        return h;
    }

    public void insertRelation(String a, String b, String relation,ElasticModel Obj){
        // Make a list of the two identified entities and add their relation to the hashmap
        List<Entity> list = Arrays.asList(new Entity(a), new Entity(b));
        Obj.relation.put(list, relation);
    }

    public void insertEntity(String current, String temp, ElasticModel Obj){
        if (Objects.equals(current, "malware") && !found(Obj.malwares, temp))
            Obj.malwares.add(new Entity(temp));
        else if (Objects.equals(current, "indicator") && !found(Obj.indicators, temp))
            Obj.indicators.add(new Entity(temp) );
        else if (Objects.equals(current, "infrastructure") && !found(Obj.infrastructures, temp))
            Obj.infrastructures.add(new Entity(temp));
        else if (Objects.equals(current, "identity") && !found(Obj.identities, temp))
            Obj.identities.add(new Entity(temp));
        else if (Objects.equals(current, "location") && !found(Obj.locations, temp))
            Obj.locations.add(new Entity(temp));
        else if (Objects.equals(current, "threatactor") && !found(Obj.threatActors, temp))
            Obj.threatActors.add(new Entity(temp));
        else if (Objects.equals(current, "tool") && !found(Obj.tools, temp))
            Obj.tools.add(new Entity(temp));
        else if (Objects.equals(current, "vulnerability") && !found(Obj.vulnerabilities, temp))
            Obj.vulnerabilities.add(new Entity(temp));
        else if (Objects.equals(current, "campaign") && !found(Obj.campaigns, temp))
            Obj.campaigns.add(new Entity(temp));
        else if (Objects.equals(current, "attackPattern") && !found(Obj.attackPatterns, temp))
            Obj.attackPatterns.add(new Entity(temp));
    }

    public Boolean checkIfEmpty(ElasticModel obj){
        if (obj.campaigns.size()==0 && obj.vulnerabilities.size()==0 && obj.tools.size()==0 && obj.threatActors.size()==0
        && obj.locations.size()==0 && obj.identities.size()==0 && obj.infrastructures.size()==0
        && obj.indicators.size()==0 && obj.malwares.size()==0 && obj.attackPatterns.size()==0){
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


    public void correlateArrays(ArrayList<StixBundle> initials, ArrayList<StixBundle> finals, int start, int end){
        LevenshteinDistance lev = new LevenshteinDistance();
        for (int i=start; i<end; i++) {
            for (Iterator<StixBundle> iterator = finals.iterator(); iterator.hasNext();) {
                StixBundle aFinal = iterator.next();
                StixBundle tempAFinal = new StixBundle(aFinal);
                for (SDO ent1 : initials.get(i).entities) {

                    for (SDO ent2 : aFinal.entities) {

                        if (ent1.type.equals("malware") || ent1.type.equals("identity")
                                || ent1.type.equals("threat-actor") || (ent2.type.equals("malware")
                                || ent2.type.equals("identity") || ent2.type.equals("threat-actor"))) {

                            if ( lev.getDistance(ent1.name, ent2.name) >=0.7) { //use levenshtein distance here
                                // add all entities and relationships of initialBundle to finalBundle
                                for (SDO ent : initials.get(i).entities)
                                    tempAFinal.addEntity(ent, initials.get(i).hash);
                                for (SRO rel : initials.get(i).relationships)
                                    tempAFinal.addRelationship(rel, initials.get(i).hash);
                            }
                        }
                    }
                }
                aFinal.entities = tempAFinal.entities;
                aFinal.relationships = tempAFinal.relationships;
                aFinal.mergedReports = tempAFinal.mergedReports;
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
        SearchResponse response = client.search(request, COMMON_OPTIONS);
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
            initials.add(new StixBundle(unMerged.get(i).bundleJson, unMerged.get(i).hash));
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
                finals.add(new StixBundle(j.getString("bundle"), 0, j.getJSONArray("ids")));
            }
        }
        System.out.println("initials" + initials);
        System.out.println("finals" + finals);

        // now initials has just the new bundles and finals has old + new bundles
        try {
            int start;
            int end;
            for (int i=0;i<initials.size();i++) {
                start = i;
                end = i + 10;
                if (end >= initials.size()){
                    end = initials.size()-1;
                }
                correlateArrays(initials, finals, start, end);
                i = end;
            }
        }
        catch (NullPointerException e){
            String u= e.toString();
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
                toUpload.ids.addAll(finalBundle.mergedReports);
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
