package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }
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
    }
}
}
