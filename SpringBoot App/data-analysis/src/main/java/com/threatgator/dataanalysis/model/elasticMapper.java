package com.threatgator.dataanalysis.model;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import org.elasticsearch.client.tasks.ElasticsearchException;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.*;
// Accesses the elastic search data store
public class elasticMapper {
    public RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));

    private static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addParameter("size", "300");

        COMMON_OPTIONS = builder.build();
    }
    // Pre process the strings of sentences
    public String preProcessStrings(String input){
        String processed = input.trim();
        processed = processed.toLowerCase(Locale.ROOT);
        processed = processed.replaceAll("\\p{Punct}", "");
        return processed;
    }
    public void SearchMalwares(malwares mal) throws IOException, JSONException {
        // Get data with index tagged data from elastic search
        SearchRequest request= new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(searchSourceBuilder);

        SearchResponse response=client.search(request, COMMON_OPTIONS);

        SearchHit[] hits=response.getHits().getHits();
        // Loop over the hits - found results
        for(SearchHit hit: hits){
//            System.out.println("Doc Id: "+hit.getId());
            String sourceAsString=hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            try {
                // get array of json elements about malwares
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

    // Get notifications information from elastic search - malwares + vulnerabilities
    public void GetNotifications(notifications notif) throws IOException, JSONException {
        SearchRequest request= new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).sort("time", SortOrder.DESC);
        request.source(searchSourceBuilder);

        SearchResponse response=client.search(request, COMMON_OPTIONS);

        SearchHit[] hits=response.getHits().getHits();

        for(SearchHit hit: hits){
            String sourceAsString=hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);

            try {
                String notifAlert = "";
                // get malwares
                JSONArray element = jsonComplete.getJSONArray("malwares");
                for(int i=0; i<element.length(); i++){
                    JSONObject e = element.getJSONObject(i);
                    String name = e.getString("name");
                    if (i==0 && !name.isEmpty()) {
                        notifAlert += "New Malware Detected: ";
                        notifAlert += name;
                    }
                    else if(!name.isEmpty()){
                        notifAlert += name;
                    }
                }
                // get vulnerabilities
                JSONArray element2 = jsonComplete.getJSONArray("vulnerabilities");
                for(int i=0; i<element2.length(); i++){
                    JSONObject e = element2.getJSONObject(i);
                    String name = e.getString("name");
                    if (i==0 && !name.isEmpty()) {
                        notifAlert += " New Vulnerabilities Detected: ";
                        notifAlert += name;
                    }
                    else if(!name.isEmpty()){
                        notifAlert += name;
                    }
                }
                if(!notifAlert.isEmpty()) {
                    String _hash = jsonComplete.getString("hash");
                    notif.addNotifications(_hash, notifAlert);
                }

            }
            catch (JSONException e){
                System.out.println("");
            }
        }
    }
    // get only vulnerabilities data
    public void SearchVulnerabilities(vulnerabilities vuln) throws IOException, JSONException {
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

    // get results for search keyword
    public void GetSearchResult(searchResults _reports, String keyword) throws IOException, JSONException {
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
                // get raw text field
                String sentence = jsonComplete.getString("rawText");
                String processed_sentence = preProcessStrings(sentence);
                keyword = keyword.toLowerCase(Locale.ROOT);
                if (!sentence.isEmpty() && processed_sentence.contains(keyword)) {
                    String _hash = jsonComplete.getString("hash"); // get hash of the string
                    _reports.addSearchReports(_hash, sentence);
                }

            }
            catch (JSONException | NullPointerException n){
                System.out.println("");
            }
        }
    }

    // helper function - get results from elastic given a hashed value
    public void GetAllResultsOnHash(allFields fields, String hash) throws IOException, JSONException {
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
                String _hash = jsonComplete.getString("hash");
                if(_hash.equals(hash)){

                    String rawText = jsonComplete.getString("rawText");
                    // Get hash and raw text
                    fields.setHash(hash);
                    fields.setRawText(rawText);

                    // Get malware information
                    String mals = "";
                    JSONArray element = jsonComplete.getJSONArray("malwares");
                    for(int i=0; i<element.length(); i++){
                        JSONObject e = element.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            mals += name + ",";
                        }
                    }
                    if (!mals.isEmpty()){fields.setMalwares(mals);}

                    // Get vulnerability information
                    String vuln = "";
                    JSONArray element2 = jsonComplete.getJSONArray("vulnerabilities");
                    for(int i=0; i<element2.length(); i++){
                        JSONObject e = element2.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            vuln += name + ",";
                        }
                    }
                    if(!vuln.isEmpty()){fields.setVulnerabilities(vuln);}

                    // Get source information
                    String source = jsonComplete.getString("source");
                    if(!source.isEmpty()){fields.setSource(source);}

                    // Get threatActors information
                    String ta = "";
                    JSONArray element3 = jsonComplete.getJSONArray("threatActors");
                    for(int i=0; i<element3.length(); i++){
                        JSONObject e = element3.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            ta += name + ",";
                        }
                    }
                    if(!ta.isEmpty()){fields.setThreatActors(ta);}

                    // Get identities information
                    String iden = "";
                    JSONArray element4 = jsonComplete.getJSONArray("identities");
                    for(int i=0; i<element4.length(); i++){
                        JSONObject e = element4.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            iden += name + ",";
                        }
                    }
                    if(!iden.isEmpty()){fields.setIdentities(iden);}

                    // Get locations information
                    String locations = "";
                    JSONArray element5 = jsonComplete.getJSONArray("locations");
                    for(int i=0; i<element5.length(); i++){
                        JSONObject e = element5.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            locations += name + ",";
                        }
                    }
                    if(!locations.isEmpty()){fields.setLocations(locations);}

                    // Get tools information
                    String tools = "";
                    JSONArray element6 = jsonComplete.getJSONArray("tools");
                    for(int i=0; i<element6.length(); i++){
                        JSONObject e = element6.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            tools += name + ",";
                        }
                    }
                    if(!tools.isEmpty()){fields.setTools(tools);}

                    // Get infrastructure information
                    String infra = "";
                    JSONArray element7 = jsonComplete.getJSONArray("infrastructures");
                    for(int i=0; i<element7.length(); i++){
                        JSONObject e = element7.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            infra += name + ",";
                        }
                    }
                    if(!infra.isEmpty()){fields.setInfrastructures(infra);}

                    // Get indicators information
                    String indic = "";
                    JSONArray element8 = jsonComplete.getJSONArray("indicators");
                    for(int i=0; i<element8.length(); i++){
                        JSONObject e = element8.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            indic += name + ",";
                        }
                    }
                    if(!indic.isEmpty()){fields.setIndicators(indic);}

                    // Get campaigns information
                    String camp = "";
                    JSONArray element9 = jsonComplete.getJSONArray("campaigns");
                    for(int i=0; i<element9.length(); i++){
                        JSONObject e = element9.getJSONObject(i);
                        String name = e.getString("name");
                        if(!name.isEmpty()){
                            camp += name + ",";
                        }
                    }
                    if(!camp.isEmpty()){fields.setCampaigns(camp);}




                }


            }
            catch (JSONException | NullPointerException n){
                System.out.println("");
            }
        }
    }

    // get a list of all hashes for each index in elastic search
    public ArrayList<String> GetAllHashes() throws IOException, JSONException {
        SearchRequest request= new SearchRequest("tagged_bundle_data");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).sort("time", SortOrder.DESC);
        request.source(searchSourceBuilder);
        SearchResponse response=client.search(request, COMMON_OPTIONS);
        SearchHit[] hits=response.getHits().getHits();
        ArrayList<String> hashes = new ArrayList<String>();
        for(SearchHit hit: hits) {
            String sourceAsString = hit.getSourceAsString();
            JSONObject jsonComplete = new JSONObject(sourceAsString);
            String _hash = jsonComplete.getString("hash");
            hashes.add(_hash);
        }
        return hashes;
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

    // update an elastic document
    public String updateDocument(String jsonInfo) throws JSONException, IOException {
        JSONObject document = new JSONObject(jsonInfo);
        String hash = document.getString("hash");
        UpdateRequest request = new UpdateRequest("tagged_bundle_data", hash);
        request.doc(jsonInfo,XContentType.JSON);
        UpdateResponse updateResponse = client.update(
                request, RequestOptions.DEFAULT);
        return "true";
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
