package com.threatgator.dataanalysis.controller;
import com.threatgator.dataanalysis.model.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
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
        connect.GetAllResultsOnHash(fields, hash);
        String r = fields.getJSONFields().toString();
        System.out.println(r);
        return r;
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
            if(i==150){break;}
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
    public String updateElasticDocument(@RequestBody String json_info) throws JSONException, IOException {
        connect.updateDocument(json_info);
        return json_info;
    }
    // Delete an element from elasticsearch
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/deleteFromElastic/{hash}")
    public String deleteFromElastic(@PathVariable int hash) throws JSONException, IOException{
        return connect.removeFromElastic(hash);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getStixBundle/{hash}")
    public String getStixBundle(@PathVariable int hash) throws JSONException, IOException{
//        System.out.println("INSIDE****************************");
        return connect.getStix(hash);
    }


}
