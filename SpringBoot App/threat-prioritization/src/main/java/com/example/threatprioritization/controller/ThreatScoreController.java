package com.example.threatprioritization.controller;

import com.example.threatprioritization.model.Organization;
import com.example.threatprioritization.model.ReportScores;
import com.example.threatprioritization.model.StixBundle;
import org.elasticsearch.ResourceNotFoundException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.threatprioritization.services.ThreatPrioritizationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/threatScore")
public class ThreatScoreController {
    @Autowired
    private ThreatPrioritizationService threatPrioritizationService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getThreatScoreByOrganizationReport")
    public Double getThreatScore(Integer org_id, Integer report_id, String index) throws JSONException, IOException {
        Organization org = threatPrioritizationService.getOrganization(org_id);
        StixBundle report = threatPrioritizationService.getReport(report_id, index);
        if (org!=null && report!=null){
            return threatPrioritizationService.getThreatScore(org, report);
        }
        else{
            return null;
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/filterStixBundle")
    public String filterStixBundle(Integer org_id, Integer report_id, String index) throws JSONException, IOException {
        Organization org = threatPrioritizationService.getOrganization(org_id);
        StixBundle report = threatPrioritizationService.getReport(report_id, index);
        if (org!=null && report!=null){
            return threatPrioritizationService.filterStixObject(org, report);
        }
        else{
            return null;
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getScoreForOrganization")
    public ArrayList<ReportScores> getByOrganization(Integer org_id) throws JSONException, IOException {
        Organization org = threatPrioritizationService.getOrganization(org_id);
        if (org!=null){
            return threatPrioritizationService.getThreatScoresForOrganization(org);
        }
        else{
            System.out.println("Org not found :(");
            return null;
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getTopReports")
    public ArrayList<ReportScores> getTopReports(Integer org_id) throws JSONException, IOException {
        Organization org = threatPrioritizationService.getOrganization(org_id);
        if (org!=null){
            ArrayList<ReportScores> scores=threatPrioritizationService.getThreatScoresForOrganization(org);
            return threatPrioritizationService.getTop5(scores);
        }
        else{
            System.out.println("Org not found :(");
            return null;
        }
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/updateScoreForReports")
    public void updateByReport(Integer report_id, String index) throws JSONException, IOException {
        StixBundle report = threatPrioritizationService.getReport(report_id, index);
        if (report!=null){
            threatPrioritizationService.updateThreatScoresForReport(report);
        }
        else{
            System.out.println("Report not found :(");
        }
    }
}
