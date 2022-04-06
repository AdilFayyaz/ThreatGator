package com.example.threatprioritization.services;

import com.example.threatprioritization.model.*;
import com.example.threatprioritization.repository.ThreatScoresRepository;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
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

    String organizationPath = "http://127.0.0.1:8084/organization/getOrganization{Org_id}";
    String getAssetsPath = "http://127.0.0.1:8084/assets/assetsByOrganization/{id}";
    String getAllOrganizationsPath = "http://127.0.0.1:8084/organization/getAll";

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

    final Double High=0.9;
    final Double Medium=0.5;
    final Double Low=0.2;

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

    public Organization getOrganization(Integer org_id){
        Organization org=this.restTemplate.getForObject(organizationPath, Organization.class, org_id);
        System.out.println("GOT ORGANIZATION: "+org.getId());
        return org;
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

    public boolean assetMentioned(Assets[] assets, String name){
        if (assets!=null) {
            for (int i = 0; i < assets.length; i++) {
                if (assets[i].getName().equals(name) || assets[i].getVendor().equals(name)
                        || assets[i].getVersion().equals(name))
                    return true;
            }
        }
        return false;
    }

    public boolean identityExists(String orgName, String orgSector, String orgCountry, Assets[] assets, String identity){
        if (identity.contains(orgName) || identity.contains(orgSector) || identity.contains(orgCountry) || orgCountry.contains(identity)
        || assetMentioned(assets, identity))
            return true;
        return false;
    }


    public Double getThreatScore(Organization organization, StixBundle report){
        Double Score=0.0;
        //get organization name, sector and country
        String orgName=organization.getName();
        String orgSector=organization.getSector();
        String orgCountry=organization.getCountry();
        //get organization's assets, vendor + name
        Assets[] assets = this.restTemplate.getForObject(getAssetsPath, Assets[].class, organization.getId());


        for (SRO relationship : report.relationships){
            if (report.getType(relationship.source).equals("malware")){
                if (relationship.name.equals("TARGETS")){
                    if (report.getType(relationship.target).equals("identity")
                    || (report.getType(relationship.target)).equals("infrastructure")
                    || (report.getType(relationship.target)).equals("vulnerability")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)){
                            Score+=High;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")){
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)){
                            Score+=Medium;
                        }
                    }
                }
                if (relationship.name.equals("ORIGINATES-FROM")){
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")){
                    if ((report.getType(relationship.target).equals("tool")
                            || (report.getType(relationship.target)).equals("infrastructure"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("threat-actor")) {
                if (relationship.name.equals("TARGETS")){
                    if (report.getType(relationship.target).equals("identity")
                            || (report.getType(relationship.target)).equals("vulnerability")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)){
                            Score+=High;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")){
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)){
                            Score+=High;
                        }
                    }
                }
                if (relationship.name.equals("LOCATED-AT")){
                    if (report.getType(relationship.target).equals("location")){
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)){
                            Score+=Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")){
                    if ((report.getType(relationship.target).equals("tool")
                            || (report.getType(relationship.target)).equals("infrastructure"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
                if (relationship.name.equals("ATTR-TO")){
                    if (report.getType(relationship.target).equals("identity")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)){
                            Score+=Low;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("identity")) {
                if (relationship.name.equals("HAS")){
                    if (report.getType(relationship.target).equals("vulnerability")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)){
                            Score+=High;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("infrastructure")) {
                if (relationship.name.equals("HAS")){
                    if (report.getType(relationship.target).equals("vulnerability")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)){
                            Score+=High;
                        }
                    }
                }
                if (relationship.name.equals("DELIVERS")){
                    if (report.getType(relationship.target).equals("malware")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)){
                            Score+=Medium;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("attack-pattern")) {
                if (relationship.name.equals("TARGETS")) {
                    if (report.getType(relationship.target).equals("identity")
                            || (report.getType(relationship.target)).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += High;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")){
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)){
                            Score+=Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")){
                    if ((report.getType(relationship.target).equals("tool"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("tool")){
                if (relationship.name.equals("TARGETS")) {
                    if (report.getType(relationship.target).equals("identity")
                            || (report.getType(relationship.target)).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += High;
                        }
                    }
                    if (report.getType(relationship.target).equals("infrastructure")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += Medium;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")){
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)){
                            Score+=Medium;
                        }
                    }
                }
                if (relationship.name.equals("HAS")){
                    if (report.getType(relationship.target).equals("vulnerability")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)){
                            Score+=High;
                        }
                    }
                }
                if (relationship.name.equals("DELIVERS")){
                    if (report.getType(relationship.target).equals("malware")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)){
                            Score+=Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")){
                    if (report.getType(relationship.target).equals("infrastructure")){
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("campaign")){
                if (relationship.name.equals("TARGETS")) {
                    if (report.getType(relationship.target).equals("identity")
                            || (report.getType(relationship.target)).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += High;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")){
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)){
                            Score+=Medium;
                        }
                    }
                }
                if (relationship.name.equals("ORIGINATES-FROM")){
                    if (report.getType(relationship.target).equals("location")){
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)){
                            Score+=Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")){
                    if ((report.getType(relationship.target).equals("tool")
                            || (report.getType(relationship.target)).equals("infrastructure"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }

            }
            if (report.getType(relationship.source).equals("indicator")){
                if (relationship.name.equals("INDICATES")){
                    if ((report.getType(relationship.target).equals("tool")
                            || (report.getType(relationship.target)).equals("infrastructure"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
            }
        }

        if (!threatScoresRepository.existsThreatScoresByOrganizationIdAndReportId(organization.getId(), report.hash)) {
            System.out.println("First Score Calculation");
            ThreatScores t= new ThreatScores();
            t.setOrganizationId(organization.getId());
            t.setReportId(report.hash);
            t.setStandalone(report.standalone);
            t.setScore(Score);
            threatScoresRepository.save(t);
            System.out.println("Saved");
        }
        else{
            System.out.println("Repeat Score Calculation");
            List<ThreatScores> r=threatScoresRepository.findByOrganizationIdAndReportId(organization.getId(), report.hash);
            r.get(0).setScore(Score);
            threatScoresRepository.save(r.get(0));
            System.out.println("Updated");
        }

        return Score;
    }

    public void updateThreatScoresForOrganization(Organization organization) throws JSONException, IOException { //for all reports
        List<StixBundle> reports = getAllReports();
        for (StixBundle report: reports){
            getThreatScore(organization, report);
        }
    }

    public void updateThreatScoresForReport(StixBundle report){ //for all organizations
        // get all organizations
        Organization[] organizations =this.restTemplate.getForObject(getAllOrganizationsPath, Organization[].class);
        for (Organization organization:organizations){
            getThreatScore(organization,report);
        }
    }

}
