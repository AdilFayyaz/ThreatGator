package com.threatgator.dataanalysis.model;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.metrics.InternalHDRPercentiles;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class elasticMapper {
    public RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));

    private static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addParameter("size", "300");
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    public void SearchMalwares(malwares mal) throws IOException, JSONException {
        SearchRequest request= new SearchRequest("tagged_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        SearchResponse response=client.search(request, COMMON_OPTIONS);

        SearchHit[] hits=response.getHits().getHits();

        for(SearchHit hit: hits){
//            System.out.println("Doc Id: "+hit.getId());
            String sourceAsString=hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            try {
                JSONArray element = jsonComplete.getJSONArray("malwares");
                for(int i=0; i<element.length(); i++){
                    JSONObject e = element.getJSONObject(i);
                    String name = e.getString("name");
                    if (!name.isEmpty()) {
                        mal.addMalware(name);
                    }
                }
            }
            catch (JSONException e){
                System.out.println("");
            }
        }
    }

    public void GetNotifications(notifications notif) throws IOException, JSONException {
        SearchRequest request= new SearchRequest("tagged_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        SearchResponse response=client.search(request, COMMON_OPTIONS);

        SearchHit[] hits=response.getHits().getHits();

        for(SearchHit hit: hits){
            String sourceAsString=hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            try {
                String alert = "ALERT - ";
                JSONArray malware = jsonComplete.getJSONArray("malwares");
                for(int i=0; i<malware.length(); i++){
                    JSONObject e = malware.getJSONObject(i);
                    String name = e.getString("name");
                    if (!name.isEmpty()) {
                        if (i == 0){
                            alert += "Malwares active keywords: ";
                        }
                       alert += name + " ";
                    }
                }


                JSONArray element = jsonComplete.getJSONArray("vulnerabilities");
                for(int i=0; i<element.length(); i++){
                    JSONObject e = element.getJSONObject(i);
                    String name = e.getString("name");

                    // Do not insert empty values here
                    if(!name.isEmpty()) {

                        if (i==0 ){

                            alert += "\n Vulnerabilities keywords: ";
                        }
                       alert += name + " ";
                    }
                }


                JSONArray iden = jsonComplete.getJSONArray("identities");
                for(int i=0; i<iden.length(); i++){
                    JSONObject e = iden.getJSONObject(i);
                    String name = e.getString("name");

                    // Do not insert empty values here
                    if(!name.isEmpty()) {
                        if (i == 0){
                            alert += "\n Identities keyword: ";
                        }
                        alert += name + " ";
                    }
                }
                if(!alert.equals("ALERT - ")) {
                    notif.addNotifications(alert);
                }

            }
            catch (JSONException e){
                System.out.println("");
            }
        }
    }
    public void SearchVulnerabilities(vulnerabilities vuln) throws IOException, JSONException {
        SearchRequest request= new SearchRequest("tagged_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        SearchResponse response=client.search(request, COMMON_OPTIONS);

        SearchHit[] hits=response.getHits().getHits();

        for(SearchHit hit: hits){
            String sourceAsString=hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            try {

                JSONArray element = jsonComplete.getJSONArray("vulnerabilities");
                for(int i=0; i<element.length(); i++){
                    JSONObject e = element.getJSONObject(i);
                    String name = e.getString("name");

                    // Do not insert empty values here
                    if(!name.isEmpty()) {
                        vuln.addVulnerability(name);
                    }
                }
            }
            catch (JSONException e){
                System.out.println("");
            }
        }
    }


    public static void main(String[] args) throws IOException, JSONException {
        elasticMapper connect= new elasticMapper();
        malwares mal = new malwares();
        vulnerabilities vuln = new vulnerabilities();
        connect.SearchMalwares(mal);
        connect.SearchVulnerabilities(vuln);

        System.out.println("***************************************************");
        System.out.println(mal.getMalwaresMap());
        System.out.println(vuln.getVulnerabilitiesMap());
        System.out.println(mal.getTopMalwares(3));


        return;

    }

}
