package com.example.threatprioritization.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ThreatScores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer report_id;
    private Integer organization_id;
    private Boolean standalone; // 1 means its in stix_bundle_data, 0 means its in mergedReports

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public Integer getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Integer organization_id) {
        this.organization_id = organization_id;
    }

    public Boolean getStandalone() {
        return standalone;
    }

    public void setStandalone(Boolean standalone) {
        this.standalone = standalone;
    }
}
