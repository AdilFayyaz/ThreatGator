package com.example.threatprioritization.services;

import com.example.threatprioritization.model.Assets;
import com.example.threatprioritization.model.Organization;
import com.example.threatprioritization.model.StixBundle;
import com.example.threatprioritization.repository.ThreatScoresRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThreatPrioritizationService {

    @Autowired
    ThreatScoresRepository threatScoresRepository;

    String organizationPath = "http://127.0.0.1:8084/organization/";
    String getAssetsPath = "http://127.0.0.1:8084/assets/assetsByOrganization/{id}";

    public RestTemplate restTemplate = new RestTemplate();

    public RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));

    private static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addParameter("size", "300");

        COMMON_OPTIONS = builder.build();
    }

    public StixBundle getReport(Integer id, String indexName) throws IOException, JSONException { //tagged_bundle_data or ...
        GetRequest request= new GetRequest(indexName ,String.valueOf(id));
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        if (response.isExists() ){
            String sourceAsString = response.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
                if(indexName.equals("tagged_bundle_data")) {
                    StixBundle obj=new StixBundle(jsonComplete.getString("bundleJson"), id);
                    obj.standalone=true;
                    return obj;
                }
                else if (indexName.equals("stix")){
                    StixBundle obj= new StixBundle(jsonComplete.getString("bundle"), id);
                    obj.standalone=false;
                    return obj;
                }
            }
        return null;
    }

    public List<StixBundle> getAllReports() throws IOException, JSONException {

        ArrayList<StixBundle> reports = new ArrayList<>();

        //first from one index
        SearchRequest request = new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder= new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        for(SearchHit hit : response.getHits().getHits()) {
            JSONObject j = new JSONObject(hit.getSourceAsString());
            Integer id= j.getInt("hash");
            StixBundle obj= new StixBundle(j.getString("bundleJson"), id);
            obj.standalone=true;
            reports.add(obj);
        }

        //now the other index
        SearchRequest request2 = new SearchRequest("stix");
        SearchSourceBuilder searchSourceBuilder2 = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request2.source(searchSourceBuilder2);
        SearchResponse response2 = client.search(request2, RequestOptions.DEFAULT);

        for (SearchHit hit : response2.getHits().getHits()) {
            JSONObject j = new JSONObject(hit.getSourceAsString());
            Integer id=Integer.parseInt(hit.getId());
            StixBundle obj = new StixBundle(j.getString("bundle"), id);
            obj.standalone=false;
            reports.add(obj);
        }

        return reports;
    }

    public Double getThreatScore(Organization organization, StixBundle report){
        //get organization name, sector and country
        String orgName=organization.getName();
        String orgSector=organization.getSector();
        String orgCountry=organization.getCountry();
        //get organization's assets, vendor + name
        Assets[] assets = this.restTemplate.getForObject(getAssetsPath, Assets[].class, organization.getId().toString());



        return null;
    }

    public void updateThreatScoresForOrganization(Organization organization){

    }

    public void updateThreatScoresForReport(StixBundle report){

    }

}
