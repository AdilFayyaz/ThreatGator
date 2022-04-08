package com.example.threatprioritization;

import org.apache.lucene.search.spell.LevenshteinDistance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThreatPrioritizationApplication {


    public static void main(String[] args) {
//        LevenshteinDistance lev = new LevenshteinDistance();
//        System.out.println(lev.getDistance("helloo", "hello"));
        SpringApplication.run(ThreatPrioritizationApplication.class, args);
    }

}
