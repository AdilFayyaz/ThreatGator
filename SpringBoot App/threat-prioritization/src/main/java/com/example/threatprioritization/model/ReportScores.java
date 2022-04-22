package com.example.threatprioritization.model;

import java.io.Serializable;

public class ReportScores implements Serializable {
    public String reportId;
    public double score;
    public long time;

    @Override
    public String toString() {
        return "ReportScores{" +
                "reportId='" + reportId + '\'' +
                ", score=" + score +
                ", time=" + time +
                '}';
    }

    public ReportScores() {}



}
