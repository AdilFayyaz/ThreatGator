package com.example.loaddumps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public String pushToElastic(JSONObject obj) throws IOException {
        String s = "";
        //this function will create connection to elasticsearch
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        // a loop will run over the finalObjects ArrayList
        for (int i = 0; i < obj.size(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonstring = mapper.writeValueAsString(obj.get(i));
            IndexRequest req = new IndexRequest("one_tip_data")
                    .source(jsonstring, XContentType.JSON);
            IndexResponse response = client.index(req, RequestOptions.DEFAULT); //inserting to elasticsearch
            System.out.println("Response Id: " + response.getId());
            
        }
        return s;
    }
    public static void main(String[] args) throws IOException {
        String luceneIndexPath = "ElasticDump/nodes/0/indices/b1uciOAMQWqOQ2xgolssmA/0/index";
        Directory index = FSDirectory.open(Paths.get(luceneIndexPath));

        IndexReader reader = DirectoryReader.open(index);
        JSONObject obj = new JSONObject();

        System.out.println(reader.maxDoc());
        for(int i = 0; i < reader.maxDoc(); i++){
            if(((DirectoryReader) reader).isCurrent()){
                Document document = reader.document(i);
                try {

                    String source = document.getBinaryValue("_source").utf8ToString();
                    JSONParser jp = new JSONParser();
                    JSONObject j = (JSONObject) jp.parse(source);
                    obj.put(i,j);
//                    System.out.println(source);
                }
                catch (NullPointerException n) {
                    System.out.println("Null Exception");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        reader.close();
        index.close();
        System.out.println(obj);
//        FileWriter file = new FileWriter("JsonDump.json");
//        file.write(obj.toJSONString());
//        file.close();

        // Push to elastic
        //this function will create connection to elasticsearch
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        // a loop will run over the finalObjects ArrayList
        for (int i = 0; i < obj.size(); i++) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonstring = mapper.writeValueAsString(obj.get(i));
            IndexRequest req = new IndexRequest("one_tip_data")
                    .source(jsonstring, XContentType.JSON);
            IndexResponse response = client.index(req, RequestOptions.DEFAULT); //inserting to elasticsearch
            System.out.println("Response Id: " + response.getId());

        }
        
    }
}