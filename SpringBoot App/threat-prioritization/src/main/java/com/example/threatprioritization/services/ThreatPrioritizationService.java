package com.example.threatprioritization.services;

import com.example.threatprioritization.model.*;
import com.example.threatprioritization.repository.ThreatScoresRepository;
import com.example.threatprioritization.util.ConvertScore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpHost;
import org.apache.lucene.search.spell.LevenshteinDistance;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThreatPrioritizationService {

    @Autowired
    ThreatScoresRepository threatScoresRepository;

    String organizationPath = "http://127.0.0.1:8084/organization/getOrganization/{Org_id}";
    String getAssetsPath = "http://127.0.0.1:8084/assets/assetsByOrganization/{id}";
    String getAllOrganizationsPath = "http://127.0.0.1:8084/organization/getAll";
    String makeStix = "http://127.0.0.1:5000/makeStix";

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

    final Double High = 0.9;
    final Double Medium = 0.5;
    final Double Low = 0.2;

    public StixBundle getReport(Integer id, String indexName) throws IOException, JSONException { //tagged_bundle_data or ...
        GetRequest request = new GetRequest(indexName, String.valueOf(id));
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        if (response.isExists()) {
            String sourceAsString = response.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            if (indexName.equals("tagged_bundle_data")) {
                StixBundle obj = new StixBundle(jsonComplete.getString("bundleJson"), id, jsonComplete.getLong("time"));
                obj.standalone = true;
                return obj;
            } else if (indexName.equals("stix")) {
                StixBundle obj = new StixBundle(jsonComplete.getString("bundle"), id);
                obj.standalone = false;
                return obj;
            }
        }
        return null;
    }

    public Organization getOrganization(Integer org_id) {
        Organization org = this.restTemplate.getForObject(organizationPath, Organization.class, org_id);
        if (org == null)
            System.out.println("org not found");
        assert org != null;
        System.out.println("GOT ORGANIZATION: " + org.getId());
        return org;
    }

    public List<StixBundle> getAllReports() throws IOException, JSONException {

        ArrayList<StixBundle> reports = new ArrayList<>();

        //first from one index
        SearchRequest request = new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        for (SearchHit hit : response.getHits().getHits()) {
            JSONObject j = new JSONObject(hit.getSourceAsString());
            Integer id = j.getInt("hash");
            StixBundle obj = new StixBundle(j.getString("bundleJson"), id, j.getLong("time"));
            obj.standalone = true;
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
            Integer id = Integer.parseInt(hit.getId());
            StixBundle obj = new StixBundle(j.getString("bundle"), id);
            obj.standalone = false;
            reports.add(obj);
        }

        return reports;
    }

    public boolean assetMentioned(Assets[] assets, String name) {

        LevenshteinDistance lev = new LevenshteinDistance();

        if (assets != null) {
            for (int i = 0; i < assets.length; i++) {
                if (lev.getDistance(assets[i].getName().toLowerCase(), name) >= 0.7 || lev.getDistance(assets[i].getVendor().toLowerCase(), name) >= 0.7
                        || lev.getDistance(assets[i].getVersion().toLowerCase(), name) >= 0.7
                || name.contains(assets[i].getName().toLowerCase()) || assets[i].getName().toLowerCase().contains(name)
                || name.contains(assets[i].getVendor().toLowerCase()) || assets[i].getVendor().toLowerCase().contains(name)) {
                    System.out.println("*********Matching assetttt***********");
                    return true;
                }
            }
        }
        System.out.println("$$$$$$$$no assets :($$$$$$$$$$$$$");
        return false;
    }

    public boolean identityExists(String orgName, String orgSector, String orgCountry, Assets[] assets, String identity) {
        LevenshteinDistance lev = new LevenshteinDistance();
        orgName=orgName.toLowerCase();
        orgSector = orgSector.toLowerCase();
        orgCountry = orgCountry.toLowerCase();

        if (lev.getDistance(identity, orgName) >= 0.7 || identity.contains(orgSector) || orgSector.contains(identity)
                || identity.contains(orgCountry) || orgCountry.contains(identity)
                || assetMentioned(assets, identity)) {
            System.out.println("*********Matching identity***********");
            return true;
        }

        return false;
    }


    public Double getThreatScore(Organization organization, StixBundle report) {
        Double Score = 0.0;
        //get organization name, sector and country
        String orgName = organization.getName();
        String orgSector = organization.getSector();
        String orgCountry = organization.getCountry();
        //get organization's assets, vendor + name
        Assets[] assets = this.restTemplate.getForObject(getAssetsPath, Assets[].class, organization.getId());


        for (SRO relationship : report.relationships) {
            if (report.getType(relationship.source).equals("malware")) {
                if (relationship.name.equals("TARGETS")) {
                    if (report.getType(relationship.target).equals("identity")
                            || (report.getType(relationship.target)).equals("infrastructure")
                            || (report.getType(relationship.target)).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += High;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("ORIGINATES-FROM")) {
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")) {
                    if ((report.getType(relationship.target).equals("tool")
                            || (report.getType(relationship.target)).equals("infrastructure"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("threat-actor")) {
                if (relationship.name.equals("TARGETS")) {
                    if (report.getType(relationship.target).equals("identity")
                            || (report.getType(relationship.target)).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += High;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += High;
                        }
                    }
                }
                if (relationship.name.equals("LOCATED-AT")) {
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")) {
                    if ((report.getType(relationship.target).equals("tool")
                            || (report.getType(relationship.target)).equals("infrastructure"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
                if (relationship.name.equals("ATTR-TO")) {
                    if (report.getType(relationship.target).equals("identity")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("identity")) {
                if (relationship.name.equals("HAS")) {
                    if (report.getType(relationship.target).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)) {
                            Score += High;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("infrastructure")) {
                if (relationship.name.equals("HAS")) {
                    if (report.getType(relationship.target).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)) {
                            Score += High;
                        }
                    }
                }
                if (relationship.name.equals("DELIVERS")) {
                    if (report.getType(relationship.target).equals("malware")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)) {
                            Score += Medium;
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
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")) {
                    if ((report.getType(relationship.target).equals("tool"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("tool")) {
                if (relationship.name.equals("TARGETS")) {
                    if (report.getType(relationship.target).equals("identity")
                            || (report.getType(relationship.target)).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += High;
                        }
                    }
                    if (report.getType(relationship.target).equals("infrastructure")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += Medium;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("HAS")) {
                    if (report.getType(relationship.target).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)) {
                            Score += High;
                        }
                    }
                }
                if (relationship.name.equals("DELIVERS")) {
                    if (report.getType(relationship.target).equals("malware")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.source)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")) {
                    if (report.getType(relationship.target).equals("infrastructure")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }
            }
            if (report.getType(relationship.source).equals("campaign")) {
                if (relationship.name.equals("TARGETS")) {
                    if (report.getType(relationship.target).equals("identity")
                            || (report.getType(relationship.target)).equals("vulnerability")) {
                        if (identityExists(orgName, orgSector, orgCountry, assets, relationship.target)) {
                            Score += High;
                        }
                    }
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("ORIGINATES-FROM")) {
                    if (report.getType(relationship.target).equals("location")) {
                        if (relationship.target.contains(orgCountry) || orgCountry.contains(relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
                if (relationship.name.equals("USES")) {
                    if ((report.getType(relationship.target).equals("tool")
                            || (report.getType(relationship.target)).equals("infrastructure"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Low;
                        }
                    }
                }

            }
            if (report.getType(relationship.source).equals("indicator")) {
                if (relationship.name.equals("INDICATES")) {
                    if ((report.getType(relationship.target).equals("tool")
                            || (report.getType(relationship.target)).equals("infrastructure"))) {
                        if (assetMentioned(assets, relationship.target)) {
                            Score += Medium;
                        }
                    }
                }
            }
        }
        ConvertScore convertScore = new ConvertScore();
        Score = convertScore.convertScore(Score);
        if (!threatScoresRepository.existsThreatScoresByOrganizationIdAndReportId(organization.getId(), report.hash)) {
            System.out.println("First Score Calculation");
            ThreatScores t = new ThreatScores();
            t.setOrganizationId(organization.getId());
            t.setReportId(report.hash);
            t.setStandalone(report.standalone);
            t.setScore(Score);
            threatScoresRepository.save(t);
            System.out.println("Saved");
        } else {
            System.out.println("Repeat Score Calculation");
            List<ThreatScores> r = threatScoresRepository.findByOrganizationIdAndReportId(organization.getId(), report.hash);
            r.get(0).setScore(Score);
            threatScoresRepository.save(r.get(0));
            System.out.println("Updated");
        }

        return Score;
    }

    public ArrayList<ReportScores> getThreatScoresForOrganization(Organization organization) throws JSONException, IOException { //for all reports
        List<StixBundle> reports = getAllReports();
        ArrayList<ReportScores> reportScores = new ArrayList<ReportScores>();
        for (StixBundle report : reports) {
            ReportScores reportScores1 = new ReportScores();
            reportScores1.reportId = report.hash.toString();
            reportScores1.score = getThreatScore(organization, report);
            reportScores.add(reportScores1);
        }
        return reportScores;
    }


    public void updateThreatScoresForReport(StixBundle report) { //for all organizations
        // get all organizations
        Organization[] organizations = this.restTemplate.getForObject(getAllOrganizationsPath, Organization[].class);
        assert organizations != null;
        for (Organization organization : organizations) {
            getThreatScore(organization, report);
        }
    }

    public String filterStixObject(Organization organization, StixBundle report) {

        String s = report.bundleString;
        //get organization name, sector and country
        String orgName = organization.getName();
        String orgSector = organization.getSector();
        String orgCountry = organization.getCountry();
        //get organization's assets, vendor + name
        Assets[] assets = this.restTemplate.getForObject(getAssetsPath, Assets[].class, organization.getId());

        StixBundle tempStix = new StixBundle(report);
        for (SRO r : report.relationships) {
            if (!identityExists(orgName, orgSector, orgCountry, assets, r.source)
                    && !identityExists(orgName, orgSector, orgCountry, assets, r.target))
                tempStix.deleteRelationship(r);
        }
        for (SDO st : report.entities) {
            if (!tempStix.hasRelationship(st)) {
                tempStix.deleteEntity(st.name);
            }
        }

        try {
            ObjectMapper Nmapper = new ObjectMapper();
            String rep = Nmapper.writeValueAsString(tempStix);
            s = restTemplate.postForObject(makeStix, new HttpEntity<>(rep), String.class);
            System.out.println("*******************");
            System.out.println(s);

        } catch (HttpStatusCodeException | JsonProcessingException e) {
            System.out.println("Error Occurred in Making Filtered Stix");
        }
        s = StringEscapeUtils.unescapeJava(s);

        if (s.charAt(0) == '"')
            s = s.substring(1, s.length() - 1);
        return s;
    }

    public ArrayList<ReportScores> getTop5(ArrayList<ReportScores> reportScores) {

        //sort based on scores
        for (int i = 0; i < reportScores.size(); i++) {
            for (int j = 0; j < reportScores.size() - 1; j++) {
                if (reportScores.get(j).score < reportScores.get(j + 1).score) {
                    ReportScores temp = reportScores.get(j + 1);
                    reportScores.set(j + 1, reportScores.get(j));
                    reportScores.set(j, temp);
                }
            }
        }
        //get top 5
        ArrayList<ReportScores> top = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            top.add(reportScores.get(i));
        }
        //sort by time and return
        for (int i = 0; i < top.size(); i++) {
            for (int j = 0; j < top.size() - 1; j++) {
                if (top.get(j).time < top.get(j + 1).time) {
                    ReportScores temp = top.get(j + 1);
                    top.set(j + 1, top.get(j));
                    top.set(j, temp);
                }
            }
        }
        return top;
    }
}
