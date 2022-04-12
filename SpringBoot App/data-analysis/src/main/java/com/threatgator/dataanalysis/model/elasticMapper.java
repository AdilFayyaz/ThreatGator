package com.threatgator.dataanalysis.model;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.threatgator.dataanalysis.utils.SDO;
import com.threatgator.dataanalysis.utils.StixBundle;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpHost;
import com.threatgator.dataanalysis.model.geo;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.client.tasks.ElasticsearchException;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

// Accesses the elastic search data store
public class elasticMapper {

    public RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));

    private static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addParameter("size", "2000");

        COMMON_OPTIONS = builder.build();
    }

    // Pre process the strings of sentences
    public String preProcessStrings(String input) {
        String processed = input.trim();
        processed = processed.toLowerCase(Locale.ROOT);
        processed = processed.replaceAll("\\p{Punct}", "");
        return processed;
    }

    public void SearchMalwares(malwares mal) throws IOException, JSONException {
        // Get data with index tagged data from elastic search
        SearchRequest request = new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        SearchResponse response = client.search(request, COMMON_OPTIONS);

        SearchHit[] hits = response.getHits().getHits();
        // Loop over the hits - found results
        for (SearchHit hit : hits) {
//            System.out.println("Doc Id: "+hit.getId());
            String sourceAsString = hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            try {
                // get array of json elements about malwares
                JSONArray element = jsonComplete.getJSONArray("malwares");
                for (int i = 0; i < element.length(); i++) {
                    JSONObject e = element.getJSONObject(i);
                    String name = e.getString("name");
                    if (!name.isEmpty()) {
                        mal.addMalware(name);
                    }
                }
            } catch (JSONException e) {
                System.out.println("");
            }
        }
    }

    // Get notifications information from elastic search - malwares + vulnerabilities
    public void GetNotifications(notifications notif) throws IOException, JSONException {
        SearchRequest request = new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).sort("time", SortOrder.DESC);
        request.source(searchSourceBuilder);

        SearchResponse response = client.search(request, COMMON_OPTIONS);

        SearchHit[] hits = response.getHits().getHits();

        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);

            try {
                String notifAlert = "";
                // get malwares
                JSONArray element = jsonComplete.getJSONArray("malwares");
                for (int i = 0; i < element.length(); i++) {
                    JSONObject e = element.getJSONObject(i);
                    String name = e.getString("name");
                    if (i == 0 && !name.isEmpty()) {
                        notifAlert += "New Malware Detected: ";
                        notifAlert += name;
                    } else if (!name.isEmpty()) {
                        notifAlert += name;
                    }
                }
                // get vulnerabilities
                JSONArray element2 = jsonComplete.getJSONArray("vulnerabilities");
                for (int i = 0; i < element2.length(); i++) {
                    JSONObject e = element2.getJSONObject(i);
                    String name = e.getString("name");
                    if (i == 0 && !name.isEmpty()) {
                        notifAlert += " New Vulnerabilities Detected: ";
                        notifAlert += name;
                    } else if (!name.isEmpty()) {
                        notifAlert += name;
                    }
                }
                if (!notifAlert.isEmpty()) {
                    String _hash = jsonComplete.getString("hash");
                    notif.addNotifications(_hash, notifAlert);
                }

            } catch (JSONException e) {
                System.out.println("");
            }
        }
    }

    // get only vulnerabilities data
    public void SearchVulnerabilities(vulnerabilities vuln) throws IOException, JSONException {
        SearchRequest request = new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        SearchResponse response = client.search(request, COMMON_OPTIONS);

        SearchHit[] hits = response.getHits().getHits();

        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            try {

                JSONArray element = jsonComplete.getJSONArray("vulnerabilities");
                for (int i = 0; i < element.length(); i++) {
                    JSONObject e = element.getJSONObject(i);
                    String name = e.getString("name");

                    // Do not insert empty values here
                    if (!name.isEmpty()) {
                        vuln.addVulnerability(name);
                    }
                }
            } catch (JSONException e) {
                System.out.println("");
            }
        }
    }

    // get results for search keyword
    public void GetSearchResult(searchResults _reports, String keyword) throws IOException, JSONException {
        SearchRequest request = new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);


        SearchResponse response = client.search(request, COMMON_OPTIONS);

        SearchHit[] hits = response.getHits().getHits();

        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);

            try {
                // get raw text field
                String sentence = jsonComplete.getString("rawText");
                String processed_sentence = preProcessStrings(sentence);
                keyword = keyword.toLowerCase(Locale.ROOT);
                if (!sentence.isEmpty() && processed_sentence.contains(keyword)) {
                    String _hash = jsonComplete.getString("hash"); // get hash of the string
                    _reports.addSearchReports(_hash, sentence);
                }

            } catch (JSONException | NullPointerException n) {
                System.out.println("");
            }
        }
    }

    // helper function - get results from elastic given a hashed value
    public void GetAllResultsOnHash(allFields fields, String hash) throws IOException, JSONException {
//        SearchRequest request= new SearchRequest("tagged_bundle_data");
//        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//        request.source(searchSourceBuilder);

        GetRequest request1 = new GetRequest("tagged_bundle_data", String.valueOf(hash));
        GetResponse response = client.get(request1, RequestOptions.DEFAULT);

        if (response.isExists()) {
            String sourceAsString = response.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            String rawText = jsonComplete.getString("rawText");
            // Get hash and raw text
            fields.setHash(hash);
            fields.setRawText(rawText);

            // Get malware information
            String mals = "";
            JSONArray element = jsonComplete.getJSONArray("malwares");
            for (int i = 0; i < element.length(); i++) {
                JSONObject e = element.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    mals += name + ",";
                }
            }
            if (!mals.isEmpty()) {
                fields.setMalwares(mals);
            }
            // Get vulnerability information
            String vuln = "";
            JSONArray element2 = jsonComplete.getJSONArray("vulnerabilities");
            for (int i = 0; i < element2.length(); i++) {
                JSONObject e = element2.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    vuln += name + ",";
                }
            }
            if (!vuln.isEmpty()) {
                fields.setVulnerabilities(vuln);
            }

            // Get source information
            String source = jsonComplete.getString("source");
            if (!source.isEmpty()) {
                fields.setSource(source);
            }

            // Get threatActors information
            String ta = "";
            JSONArray element3 = jsonComplete.getJSONArray("threatActors");
            for (int i = 0; i < element3.length(); i++) {
                JSONObject e = element3.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    ta += name + ",";
                }
            }
            if (!ta.isEmpty()) {
                fields.setThreatActors(ta);
            }

            // Get identities information
            String iden = "";
            JSONArray element4 = jsonComplete.getJSONArray("identities");
            for (int i = 0; i < element4.length(); i++) {
                JSONObject e = element4.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    iden += name + ",";
                }
            }
            if (!iden.isEmpty()) {
                fields.setIdentities(iden);
            }

            // Get locations information
            String locations = "";
            JSONArray element5 = jsonComplete.getJSONArray("locations");
            for (int i = 0; i < element5.length(); i++) {
                JSONObject e = element5.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    locations += name + ",";
                }
            }
            if (!locations.isEmpty()) {
                fields.setLocations(locations);
            }

            // Get tools information
            String tools = "";
            JSONArray element6 = jsonComplete.getJSONArray("tools");
            for (int i = 0; i < element6.length(); i++) {
                JSONObject e = element6.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    tools += name + ",";
                }
            }
            if (!tools.isEmpty()) {
                fields.setTools(tools);
            }

            // Get infrastructure information
            String infra = "";
            JSONArray element7 = jsonComplete.getJSONArray("infrastructures");
            for (int i = 0; i < element7.length(); i++) {
                JSONObject e = element7.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    infra += name + ",";
                }
            }
            if (!infra.isEmpty()) {
                fields.setInfrastructures(infra);
            }

            // Get indicators information
            String indic = "";
            JSONArray element8 = jsonComplete.getJSONArray("indicators");
            for (int i = 0; i < element8.length(); i++) {
                JSONObject e = element8.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    indic += name + ",";
                }
            }
            if (!indic.isEmpty()) {
                fields.setIndicators(indic);
            }

            // Get campaigns information
            String camp = "";
            JSONArray element9 = jsonComplete.getJSONArray("campaigns");
            for (int i = 0; i < element9.length(); i++) {
                JSONObject e = element9.getJSONObject(i);
                String name = e.getString("name");
                if (!name.isEmpty()) {
                    camp += name + ",";
                }
            }
            if (!camp.isEmpty()) {
                fields.setCampaigns(camp);
            }

//            // Get Attack Patterns Information
//            String atk = "";
//            JSONArray element10 = jsonComplete.getJSONArray("attackPatterns");
//            for(int i=0;i<element10.length();i++){
//                JSONObject e = element10.getJSONObject(i);
//                String name = e.getString("name");
//                if(!name.isEmpty()){
//                    atk += name + ",";
//                }
//            }
//            if(!atk.isEmpty()){fields.setAttackPatterns(atk);}
        }
    }

    // get a list of all hashes for each index in elastic search
    public ArrayList<String> GetAllHashes() throws IOException, JSONException {
        SearchRequest request= new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).sort("time", SortOrder.DESC);
        request.source(searchSourceBuilder);
        searchSourceBuilder.fetchSource(false);
        SearchResponse response=client.search(request, COMMON_OPTIONS);
        SearchHit[] hits=response.getHits().getHits();
        ArrayList<String> hashes = new ArrayList<String>();
        for(SearchHit hit: hits) {
            hashes.add(hit.getId());
//            String sourceAsString = hit.getSourceAsString();
//            JSONObject jsonComplete = new JSONObject(sourceAsString);
//            String _hash = jsonComplete.getString("hash");
//            hashes.add(_hash);
        }
        return hashes;
    }

    public boolean hasMergedWithReport(JSONArray array, String hash) throws JSONException {
        if (array.length()==1 && array.getString(0).equals(hash)){ //it's only merged with one report, itself
            return false;
        }
        for (int i=0; i<array.length(); i++){
            if (array.getString(i).equals(hash))
                return true;
        }
        return false;
    }

    public boolean alreadyAdded(ArrayList<RelatedReport> relatedReports, String id){
        for (RelatedReport relatedReport : relatedReports) {
            if (relatedReport.id.equals(id))
                return true;
        }
        return false;
    }

    public ArrayList<RelatedReport> getRelatedReports(String hash) throws IOException, JSONException {
        SearchRequest request= new SearchRequest("stix");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);
        SearchResponse response=client.search(request, COMMON_OPTIONS);
        SearchHit[] hits=response.getHits().getHits();
        ArrayList<RelatedReport> relatedReports = new ArrayList<>();
        for(SearchHit hit: hits) {
            JSONObject object = new JSONObject(hit.getSourceAsString());
            JSONArray ids = object.getJSONArray("ids");
            if (hasMergedWithReport(ids, hash)){
                RelatedReport relatedReport = new RelatedReport();
                relatedReport.id=hit.getId();
                relatedReport.index="stix";

                relatedReports.add(relatedReport);

                for (int i=0; i<ids.length(); i++){
                    RelatedReport relatedReport1 = new RelatedReport();
                    relatedReport1.id=ids.getString(i);
                    relatedReport1.index="tagged_bundle_data";
                    if (!relatedReport1.id.equals(hash))
                        if (!alreadyAdded(relatedReports, relatedReport1.id))
                            relatedReports.add(relatedReport1);
                }
            }

        }
        return relatedReports;
    }

    // get weeks data from elasticsearch - using time attribute
    public ArrayList<Integer> GetWeekHits() throws IOException, JSONException {
        SearchRequest request= new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).sort("time", SortOrder.DESC);
        request.source(searchSourceBuilder);
        SearchResponse response=client.search(request, COMMON_OPTIONS);
        SearchHit[] hits=response.getHits().getHits();
        ArrayList<Integer> daysHit = new ArrayList<>(7);
        for(int i=0;i<7;i++){
            daysHit.add(i,0);
        }
        for(SearchHit hit: hits) {
            String sourceAsString = hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            String time_ = jsonComplete.getString("time");
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date(Long.parseLong(time_)));
            int dow = cal.get(Calendar.DAY_OF_WEEK);
            switch (dow) {
                case Calendar.MONDAY:
                    daysHit.set(0,daysHit.get(0)+1);
                    break;
                case Calendar.TUESDAY:
                    daysHit.set(1,daysHit.get(1)+1);
                    break;
                case Calendar.WEDNESDAY:
                    daysHit.set(2,daysHit.get(2)+1);
                    break;
                case Calendar.THURSDAY:
                    daysHit.set(3,daysHit.get(3)+1);
                    break;
                case Calendar.FRIDAY:
                    daysHit.set(4,daysHit.get(4)+1);
                    break;
                case Calendar.SATURDAY:
                    daysHit.set(5,daysHit.get(5)+1);
                    break;
                case Calendar.SUNDAY:
                    daysHit.set(6,daysHit.get(6)+1);
                    break;
            }
        }
        return daysHit;
    }

    // get data from one-tip-dumps
    public ArrayList<threatExchange> GetThreatExchangeData() throws IOException, JSONException {
        SearchRequest request= new SearchRequest("one_tip_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);
        SearchResponse response=client.search(request, COMMON_OPTIONS);
        SearchHit[] hits=response.getHits().getHits();
        ArrayList<threatExchange> tex = new ArrayList<threatExchange>();
        for(SearchHit hit: hits) {
            String sourceAsString = hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            String lastSeen, _id, title, type, threatScore, confidence, riskFactor, dataSources = "";
            try {
                _id = jsonComplete.getString("id");
            }
            catch(JSONException j){
                _id = "";
            }
            try {
                lastSeen = jsonComplete.getString("lastSeen");
            }
            catch(JSONException j ){
                lastSeen = "";
            }
            try {
                title = jsonComplete.getString("title");
            }
            catch (JSONException j){
                title = "";
            }
            try {
                type = jsonComplete.getString("type");
            }
            catch (JSONException j){
                type = "";
            }
            try {
                threatScore = jsonComplete.getString("threatScore");
            }
            catch (JSONException j){
                threatScore = "";
            }
            try{
                confidence = jsonComplete.getString("confidence");
            }
            catch (JSONException e ){
                confidence = "";
            }
            try{
                riskFactor = jsonComplete.getString("riskFactor");
            }catch (JSONException j){
                riskFactor = "";
            }
            try{
                JSONArray element = jsonComplete.getJSONArray("dataSources");
                for(int i=0; i<element.length(); i++){
                    dataSources += element.get(i).toString() + ",";
                }
            }catch (JSONException j){
            }
            String ipv4 = "";
            try {
                JSONObject element = jsonComplete.getJSONObject("iPv4Details");

                ipv4 = (element.toString());
            }catch(JSONException e){
                ipv4 = null;
            }
            tex.add(new threatExchange(_id,lastSeen,title,type,threatScore,confidence,
                    riskFactor, ipv4,dataSources));
        }
        return tex;
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
    public void SearchLocations(geo loc) throws IOException, JSONException {
        SearchRequest request= new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        SearchResponse response=client.search(request, COMMON_OPTIONS);

        SearchHit[] hits=response.getHits().getHits();

        for(SearchHit hit: hits){
            String sourceAsString=hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            try {

                JSONArray element = jsonComplete.getJSONArray("locations");
                for(int i=0; i<element.length(); i++){
                    JSONObject e = element.getJSONObject(i);
                    String name = e.getString("name");

                    // Do not insert empty values here
                    if(!name.isEmpty()) {
                        loc.addLocation(name);
                    }
                }
            }
            catch (JSONException e){
                System.out.println("--");
            }
        }
    }
    
     // remove document from elastic search
    public String removeFromElastic(int hash) throws  IOException, JSONException {
        try {
            DeleteRequest del_req = new DeleteRequest("tagged_bundle_data", String.valueOf(hash));
            DeleteResponse deleteResponse = client.delete(
                    del_req, RequestOptions.DEFAULT);
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                return "false";
            }
            return "true";
        }catch (HttpClientErrorException.BadRequest e){
            return "false";
        }
    }
    public boolean foundEntity(JSONArray array, String name) throws JSONException {
        for (int i=0; i< array.length(); i++){
            JSONObject temp = array.getJSONObject(i);
            if (temp.getString("name").equals(name)){
                return true;
            }
        }
        return false;
    }

    // update an elastic document
    public String updateDocument(String jsonInfo, Integer adminId, Integer orgId) throws JSONException, IOException, org.json.JSONException {

        //create log object
        
        Log log= new Log();
        log.timestamp = new SimpleDateFormat("yyy.MM.dd.HH.mm.ss").format(new Date());
        log.adminId=adminId;
        log.orgId=orgId;

        log.changedTypes = new ArrayList<>();
        log.newEntities = new ArrayList<>();
        log.deletedEntities = new ArrayList<>();

        // generate stix bundle again from the class using python
        JSONObject document = new JSONObject(jsonInfo);
        String hash = document.getString("hash");
        log.reportHash = hash;
        // get original document's stix bundle
        GetRequest request1= new GetRequest("tagged_bundle_data", String.valueOf(hash));
        GetResponse response = client.get(request1, RequestOptions.DEFAULT);
        String stixBundle="";
        if (response.isExists()) {
            String sourceAsString = response.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            stixBundle = jsonComplete.getString("bundleJson");
            // convert to StixBundle class
            StixBundle object = new StixBundle(stixBundle, Integer.parseInt(hash));
            System.out.println(object.relationships.size());

            // compare what has changed
            // apply changes
            JSONArray malwares = null;
            if(document.has("malwares")) {
                malwares = document.getJSONArray("malwares");
                for (int i = 0; i < malwares.length(); i++) {
                    JSONObject malware = malwares.getJSONObject(i);
                    if (object.exists(malware.getString("name"))) {
                        Integer index = object.getIndex(malware.getString("name"));
                        if (!object.entities.get(index).type.equals("malware")) {
                            object.editEntity(malware.getString("name"), "malware");
                            log.changedTypes.add(malware.getString("name"));
                            System.out.println("changed type");
                            System.out.println(log.changedTypes.size());
                        }
                    } else {
                        object.addEntity(new SDO("", malware.getString("name"), "malware"));
                        log.newEntities.add(malware.getString("name"));
                    }
                }
            }

            JSONArray threatActors = null;
            if(document.has("threatActors")) {
                threatActors = document.getJSONArray("threatActors");
                for (int i = 0; i < threatActors.length(); i++) {
                    JSONObject m = threatActors.getJSONObject(i);
                    if (object.exists(m.getString("name"))) {
                        Integer index = object.getIndex(m.getString("name"));
                        if (!object.entities.get(index).type.equals("threat-actor")) {
                            object.editEntity(m.getString("name"), "threat-actor");
                            log.changedTypes.add(m.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", m.getString("name"), "threat-actor"));
                        log.newEntities.add(m.getString("name"));
                    }
                }
            }

            JSONArray identities = null;
            if(document.has("identities")) {
                identities = document.getJSONArray("identities");
                for (int i = 0; i < identities.length(); i++) {
                    JSONObject m = identities.getJSONObject(i);
                    if (object.exists(m.getString("name"))) {
                        Integer index = object.getIndex(m.getString("name"));
                        if (!object.entities.get(index).type.equals("identity")) {
                            object.editEntity(m.getString("name"), "identity");
                            log.changedTypes.add(m.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", m.getString("name"), "identity"));
                        log.newEntities.add(m.getString("name"));
                    }
                }
            }

            JSONArray vulnerabilities = null;
            if(document.has("vulnerabilities")) {
                vulnerabilities = document.getJSONArray("vulnerabilities");
                for (int i = 0; i < vulnerabilities.length(); i++) {
                    JSONObject m = vulnerabilities.getJSONObject(i);
                    if (object.exists(m.getString("name"))) {
                        Integer index = object.getIndex(m.getString("name"));
                        if (!object.entities.get(index).type.equals("vulnerability")) {
                            object.editEntity(m.getString("name"), "vulnerability");
                            log.changedTypes.add(m.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", m.getString("name"), "vulnerability"));
                        log.newEntities.add(m.getString("name"));
                    }
                }
            }

            JSONArray campaigns = null;
            if(document.has("campaigns")) {
                campaigns = document.getJSONArray("campaigns");
                for (int i = 0; i < campaigns.length(); i++) {
                    JSONObject m = campaigns.getJSONObject(i);
                    if (object.exists(m.getString("name"))) {
                        Integer index = object.getIndex(m.getString("name"));
                        if (!object.entities.get(index).type.equals("campaign")) {
                            object.editEntity(m.getString("name"), "campaign");
                            log.changedTypes.add(m.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", m.getString("name"), "campaign"));
                        log.newEntities.add(m.getString("name"));
                    }
                }
            }

            JSONArray infrastructures = null;
            if(document.has("infrastructures")) {
                infrastructures = document.getJSONArray("infrastructures");
                for (int i = 0; i < infrastructures.length(); i++) {
                    JSONObject m = infrastructures.getJSONObject(i);
                    if (object.exists(m.getString("name"))) {
                        Integer index = object.getIndex(m.getString("name"));
                        if (!object.entities.get(index).type.equals("infrastructure")) {
                            object.editEntity(m.getString("name"), "infrastructure");
                            log.changedTypes.add(m.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", m.getString("name"), "infrastructure"));
                        log.newEntities.add(m.getString("name"));
                    }
                }
            }

            JSONArray locations = null;
            if(document.has("locations")) {
                locations = document.getJSONArray("locations");
                for (int i = 0; i < locations.length(); i++) {
                    JSONObject l = locations.getJSONObject(i);
                    if (object.exists(l.getString("name"))) {
                        Integer index = object.getIndex(l.getString("name"));
                        if (!object.entities.get(index).type.equals("location")) {
                            object.editEntity(l.getString("name"), "location");
                            log.changedTypes.add(l.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", l.getString("name"), "location"));
                        log.newEntities.add(l.getString("name"));
                    }
                }
            }

            JSONArray tools = null;
            if(document.has("tools")) {
                tools = document.getJSONArray("tools");
                for (int i = 0; i < tools.length(); i++) {
                    JSONObject t = tools.getJSONObject(i);
                    if (object.exists(t.getString("name"))) {
                        Integer index = object.getIndex(t.getString("name"));
                        if (!object.entities.get(index).type.equals("tool")) {
                            object.editEntity(t.getString("name"), "tool");
                            log.changedTypes.add(t.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", t.getString("name"), "tool"));
                        log.newEntities.add(t.getString("name"));
                    }
                }
            }

            JSONArray indicators = null;
            if(document.has("indicators")) {
                indicators = document.getJSONArray("indicators");
                for (int i = 0; i < indicators.length(); i++) {
                    JSONObject ind = indicators.getJSONObject(i);
                    if (object.exists(ind.getString("name"))) {
                        Integer index = object.getIndex(ind.getString("name"));
                        if (!object.entities.get(index).type.equals("indicator")) {
                            object.editEntity(ind.getString("name"), "indicator");
                            log.changedTypes.add(ind.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", ind.getString("name"), "indicator"));
                        log.newEntities.add(ind.getString("name"));
                    }
                }
            }
            JSONArray attackPatterns= null;
            if(document.has("attackPatterns")) {
                attackPatterns = document.getJSONArray("attackPatterns");
                for (int i = 0; i < attackPatterns.length(); i++) {
                    JSONObject att = attackPatterns.getJSONObject(i);
                    if (object.exists(att.getString("name"))) {
                        Integer index = object.getIndex(att.getString("name"));
                        if (!object.entities.get(index).type.equals("attack-pattern")) {
                            object.editEntity(att.getString("name"), "attack-pattern");
                            log.changedTypes.add(att.getString("name"));
                        }
                    } else {
                        object.addEntity(new SDO("", att.getString("name"), "attack-pattern"));
                        log.newEntities.add(att.getString("name"));
                    }
                }
            }


            //checking for deletion of entity, assuming that the jsonString being sent
            // will contain all arrays, so no array will be null

            for (int i=0; i<object.entities.size(); i++){
                if (!foundEntity(malwares, object.entities.get(i).name) &&
                        !foundEntity(threatActors, object.entities.get(i).name) &&
                        !foundEntity(identities, object.entities.get(i).name) &&
                        !foundEntity(indicators, object.entities.get(i).name) &&
                        !foundEntity(vulnerabilities, object.entities.get(i).name) &&
                        !foundEntity(campaigns, object.entities.get(i).name) &&
                        !foundEntity(tools, object.entities.get(i).name) &&
                        !foundEntity(locations, object.entities.get(i).name) &&
                        !foundEntity(attackPatterns, object.entities.get(i).name) &&
                        !foundEntity(infrastructures, object.entities.get(i).name)){
                    System.out.println("Object "+object.entities.get(i).name+ " needs to be deleted");
                    log.deletedEntities.add(object.entities.get(i).name);
                    object.deleteEntity(object.entities.get(i).name);
                }

            }
            System.out.println("************THE NUMBER OF ENTITIES**************");
            System.out.println(object.entities.size());
            //get stix from python
            String makeStix = "http://127.0.0.1:5000/makeStix";
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper Nmapper = new ObjectMapper();
            String r = Nmapper.writeValueAsString(object);
            System.out.println(r);
            try {
                String s = restTemplate.postForObject(makeStix, new HttpEntity<>(r.toString()), String.class);
                System.out.println("*******************");
                System.out.println(s);
                document.put("bundleJson", s);
            } catch (HttpStatusCodeException e) {
                System.out.println("Error Occurred in Making Stix");
            }

            String finaljsonstring = document.toString();
            UpdateRequest request = new UpdateRequest("tagged_bundle_data", hash);
            request.doc(finaljsonstring,XContentType.JSON);
            UpdateResponse updateResponse = client.update(
                    request, RequestOptions.DEFAULT);


            // push history object to elastic
            String jsonString =Nmapper.writeValueAsString(log);
            IndexRequest reqHis = new IndexRequest("history").source(jsonString, XContentType.JSON);
            IndexResponse responseHis = client.index(reqHis, RequestOptions.DEFAULT);

            return "true";

        }
        return "false";
    }

    ArrayList<String> getArrayList(JSONArray array) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i=0; i<array.length(); i++){
            arrayList.add(array.getString(i));
        }
        return arrayList;
    }

    public ArrayList<Log> getHistory(Integer hash) throws IOException, JSONException {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("reportHash", hash.toString()));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("history");
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, COMMON_OPTIONS);

        ArrayList<Log> history = new ArrayList<>();
        Integer i=0;
        for (SearchHit hit : searchResponse.getHits()){
            i++;
            String sourceAsString = hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            Log log = new Log();
            log.id=i.toString();
            log.reportHash=hash.toString();
            log.timestamp=jsonComplete.getString("timestamp");
            log.orgId=jsonComplete.getInt("orgId");
            log.adminId=jsonComplete.getInt("adminId");
            log.deletedEntities=getArrayList(jsonComplete.getJSONArray("deletedEntities"));
            log.newEntities=getArrayList(jsonComplete.getJSONArray("newEntities"));
            log.changedTypes=getArrayList(jsonComplete.getJSONArray("changedTypes"));

            history.add(log);
        }
        return history;
    }

    //returns the bundle as a jsonString
    public String getStix(int hash, String index) throws JSONException, IOException{
        String stixBundle = "";
        GetRequest request= new GetRequest(index ,String.valueOf(hash));
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        if (response.isExists() ){
            String sourceAsString = response.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            if(index.equals("tagged_bundle_data")) {
                stixBundle = jsonComplete.getString("bundleJson");
            }
            else if (index.equals("stix")){
                stixBundle = jsonComplete.getString("bundle");
            }
            stixBundle = StringEscapeUtils.unescapeJava(stixBundle);
            if (stixBundle.charAt(0) == '"')
                stixBundle = stixBundle.substring(1, stixBundle.length() - 1);
        }

        return stixBundle;

    }

    // return related reports
    public String getRelatedReports(int hash) throws JSONException, IOException{
        return "";
    }

}
