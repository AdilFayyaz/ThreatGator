package com.example.threatprioritization.util;

public class ConvertScore {
    public double convertScore(double score){
        double sigmoid = 1.0 / (1.0+Math.exp(-1.0*score));
        double finalScore= (sigmoid-0.5)/(1-0.5);
        return Math.round(finalScore*100D)/100D;
    }
}
