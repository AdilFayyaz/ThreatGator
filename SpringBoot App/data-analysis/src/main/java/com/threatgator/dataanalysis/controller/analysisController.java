package com.threatgator.dataanalysis.controller;
import com.threatgator.dataanalysis.model.*;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
// Controller class communicating with the front end
@RestController
@RequestMapping("/dataAnalysis")
public class analysisController {
    public elasticMapper connect;
    public malwares mal;
    public notifications notif;

    analysisController() throws JSONException, IOException {
        connect= new elasticMapper();
        mal = new malwares();
        notif = new notifications();
        connect.SearchMalwares(mal);
        connect.GetNotifications(notif);
    }
    // Fetch the top malwares from elastic search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getTopMalwares/{value}")
    public String getTopMalwares(@PathVariable int value) throws JSONException, IOException {
        return mal.getTopMalwares(value).toString();
    }
    // Get top notification values from elastic search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getNotifications/{value}")
    public String getNotifications(@PathVariable int value) throws JSONException, IOException{
        return notif.getNotifications(value).toString();
    }
    // return search result related reports from the elastic store
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getSearchResults")
    public String getSearchResults(@RequestBody String keyword) throws JSONException, IOException {
        searchResults search = new searchResults();
        connect.GetSearchResult(search, keyword);
        String r = search.getSearchResults().toString();
        return r;
    }
    // helper function that fetches result given the hash of a document from elastic store
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getResultOnHash")
    public String getResultsOnHash(@RequestBody String hash) throws JSONException, IOException {
        allFields fields = new allFields();
        JSONObject j = new JSONObject(hash);
        fields = connect.GetAllResultsOnHash(fields, j.getString("hash"));
        String r = fields.getJSONFields().toString();
        System.out.println(r);
        return r;
    }

    // this function gives an array that tells what reports are related and which index that related report is in
    // a check should be added so that same report is not shown that there should be more than 1 tagged stix bundle report
    // as one will be itself
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/getRelatedReports")
    public ArrayList<RelatedReport> getRelatedReports(@RequestBody String hash) throws JSONException, IOException {
        JSONObject j = new JSONObject(hash);
        return connect.getRelatedReports(j.getString("hash"));
    }

    // get vulnerabilities list from elasticsearch
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getVulnerabilities")
    public String getVulnerabilities() throws JSONException, IOException {
        vulnerabilities vuln = new vulnerabilities();
        connect.SearchVulnerabilities(vuln);
        String r = vuln.getJSONVulnerabilities().toString();
        System.out.println(r);
        return r;
    }

    // get a list of the reports from elastic search
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getReports")
    public String getReports() throws JSONException, IOException {
        ArrayList<String> hashes = new ArrayList<String>();
        hashes = connect.GetAllHashes();
        allFields fields[] = new allFields[hashes.size()];
        JSONObject j = new JSONObject();
        for(int i=0; i<hashes.size();i++){
            allFields f = new allFields();
            connect.GetAllResultsOnHash(f, hashes.get(i));
            JSONObject r = f.getJSONFields();
            j.put(String.valueOf(i),r);
        }
        return j.toString();

    }

    // get the number of reports fetched in the week
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getWeekHits")
    public ArrayList<Integer> getWeekHits() throws JSONException, IOException {
        ArrayList<Integer> hits;
        hits = connect.GetWeekHits();
        return hits;
    }

    // get threat data from dumps
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getThreatExchangeData")
    public String getThreatExchangeData() throws JSONException, IOException {

        ArrayList<threatExchange> tex ;
        tex = connect.GetThreatExchangeData();
        JSONObject exJson = new JSONObject();
        for(int i=0;i<tex.size();i++){
            if (i==50){break;}
            JSONObject r = tex.get(i).getJSONExchangeData();
            exJson.put(String.valueOf(i),r);
        }
        return exJson.toString();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getLocations")
    public String getLocations() throws JSONException, IOException {
        geo loc = new geo();
        connect.SearchLocations(loc);
        String r = loc.getJSONLocations().toString();
        System.out.println(r);
        return r;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/updateElasticDocument")
    public String updateElasticDocument(@RequestBody String json_info, Integer adminId, Integer orgId) throws JSONException, IOException, org.json.JSONException {
        //        Manipulate the json string into the correct format
        JSONObject input = new JSONObject(json_info);
        JSONObject output = new JSONObject();
        output.put("hash", input.getString("hash"));
//        Malwares
        JSONArray malwares = new JSONArray();
        if (input.getString("malwares").length()>0) {
            String[] malwareList = input.getString("malwares").split(",");
            for (String mal : malwareList) {
                JSONObject m = new JSONObject();
                m.put("name", mal);
                malwares.put(m);
            }
        }
        output.put("malwares", malwares);

//        Vulnerabilities
        JSONArray vuln = new JSONArray();
        if (input.getString("vulnerabilities").length()>0) {
            String[] vulnList = input.getString("vulnerabilities").split(",");
            for (String v : vulnList) {
                JSONObject m = new JSONObject();
                m.put("name", v);
                vuln.put(m);
            }
        }
        output.put("vulnerabilities", vuln);

//        Threat Actors
        JSONArray threatActors = new JSONArray();
        if (input.getString("threatActors").length()>0) {
            String[] threatActorsList = input.getString("threatActors").split(",");
            System.out.println(threatActorsList);
            for (String v : threatActorsList) {
                System.out.println(v);
                JSONObject m = new JSONObject();
                m.put("name", v);
                threatActors.put(m);
            }
        }
        output.put("threatActors", threatActors);

//        Identities
        JSONArray identities = new JSONArray();
        if (input.getString("identities").length()>0) {
            String[] identitiesList = input.getString("identities").split(",");
            for (String v : identitiesList) {
                JSONObject m = new JSONObject();
                m.put("name", v);
                identities.put(m);
            }
        }
        output.put("identities", identities);

//        Locations
        JSONArray locations = new JSONArray();
        if (input.getString("locations").length()>0) {
            String[] locationsList = input.getString("locations").split(",");
            for (String v : locationsList) {
                JSONObject m = new JSONObject();
                m.put("name", v);
                locations.put(m);
            }
        }
        output.put("locations", locations);

//        Tools
        JSONArray tools = new JSONArray();
        if (input.getString("tools").length()>0) {
            String[] toolsList = input.getString("tools").split(",");
            for (String v : toolsList) {
                JSONObject m = new JSONObject();
                m.put("name", v);
                tools.put(m);
            }
        }
        output.put("tools", tools);

//        Infrastructures
        JSONArray infrastructures = new JSONArray();
        if (input.getString("infrastructures").length()>0) {
            String[] infrastructuresList = input.getString("infrastructures").split(",");
            for (String v : infrastructuresList) {
                JSONObject m = new JSONObject();
                m.put("name", v);
                infrastructures.put(m);
            }
        }
        output.put("infrastructures", infrastructures);

//        Indicators
        JSONArray indicators = new JSONArray();
        if (input.getString("indicators").length()>0) {
            String[] indicatorsList = input.getString("indicators").split(",");
            for (String v : indicatorsList) {
                JSONObject m = new JSONObject();
                m.put("name", v);
                indicators.put(m);
            }
        }
        output.put("indicators", indicators);

//        Campaigns
        JSONArray campaigns = new JSONArray();
        if (input.getString("campaigns").length()>0) {
            String[] campaignsList = input.getString("campaigns").split(",");
            for (String v : campaignsList) {
                JSONObject m = new JSONObject();
                m.put("name", v);
                campaigns.put(m);
            }
        }
        output.put("campaigns", campaigns);

//        Attack Patterns
        JSONArray attackPatterns = new JSONArray();
        if (input.getString("attackPatterns").length()>0) {
            String[] attackPatternsList = input.getString("attackPatterns").split(",");
            for (String v : attackPatternsList) {
                JSONObject m = new JSONObject();
                m.put("name", v);
                attackPatterns.put(m);
            }
        }
        output.put("attackPatterns", attackPatterns);
        System.out.println(output.toString());
        connect.updateDocument(output.toString(), adminId, orgId);
        return output.toString();
    }

    // get History elements of a report
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getHistory")
    public ArrayList<Log> getHistory(@RequestBody String json_info) throws JSONException, IOException{
        JSONObject input = new JSONObject(json_info);
        return connect.getHistory(input.getInt("hash"));
    }

    // Delete an element from elasticsearch
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/deleteFromElastic/{hash}")
    public String deleteFromElastic(@PathVariable int hash) throws JSONException, IOException{
        return connect.removeFromElastic(hash);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getStixBundle/{hash}/{index}")
    public String getStixBundle(@PathVariable int hash, @PathVariable String index) throws JSONException, IOException{
//        System.out.println("INSIDE****************************");
        return connect.getStix(hash, index);
    }


}
