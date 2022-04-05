package com.threatgator.dataanalysis.model;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
// Class representing all the fields in elastic search
public class allFields {
    String source;
    String hash;
    String rawText;
    String malwares;
    String threatActors;
    String identities;
    String locations;
    String tools;
    String vulnerabilities;
    String infrastructures;
    String indicators;
    String campaigns;

    public allFields() {
    }

    public allFields(String source, String hash, String rawText, String malwares, String threatActors, String identities, String locations, String tools, String vulnerabilities, String infrastructures, String indicators, String campaigns) {
        this.source = source;
        this.hash = hash;
        this.rawText = rawText;
        this.malwares = malwares;
        this.threatActors = threatActors;
        this.identities = identities;
        this.locations = locations;
        this.tools = tools;
        this.vulnerabilities = vulnerabilities;
        this.infrastructures = infrastructures;
        this.indicators = indicators;
        this.campaigns = campaigns;
    }
    // Getters and Setters
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getMalwares() {
        return malwares;
    }

    public void setMalwares(String malwares) {
        this.malwares = malwares;
    }

    public String getThreatActors() {
        return threatActors;
    }

    public void setThreatActors(String threatActors) {
        this.threatActors = threatActors;
    }

    public String getIdentities() {
        return identities;
    }

    public void setIdentities(String identities) {
        this.identities = identities;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(String vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public String getInfrastructures() {
        return infrastructures;
    }

    public void setInfrastructures(String infrastructures) {
        this.infrastructures = infrastructures;
    }

    public String getIndicators() {
        return indicators;
    }

    public void setIndicators(String indicators) {
        this.indicators = indicators;
    }

    public String getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(String campaigns) {
        this.campaigns = campaigns;
    }

    // get field results in JSON format
    public JSONObject getJSONFields() throws JSONException {
        JSONObject j = new JSONObject();
        j.put("hash", getHash());
        j.put("source", getSource());
        j.put("rawText",getRawText());
        j.put("malwares", getMalwares());
        j.put("threatActors", getThreatActors());
        j.put("identities",getIdentities());
        j.put("locations", getLocations());
        j.put("tools", getTools());
        j.put("vulnerabilities", getVulnerabilities());
        j.put("infrastructure", getInfrastructures());
        j.put("indicators", getIndicators());
        j.put("campaigns", getCampaigns());
        return j;
    }
    // Get field results in Map format
    public Map getLinkedMapFields(){
        Map j = new LinkedHashMap<String,String>();
        j.put("hash", getHash());
        j.put("source", getSource());
        j.put("rawText",getRawText());
        j.put("malwares", getMalwares());
        j.put("threatActors", getThreatActors());
        j.put("identities",getIdentities());
        j.put("locations", getLocations());
        j.put("tools", getTools());
        j.put("vulnerabilities", getVulnerabilities());
        j.put("infrastructure", getInfrastructures());
        j.put("indicators", getIndicators());
        j.put("campaigns", getCampaigns());
        return j;

    }
}
