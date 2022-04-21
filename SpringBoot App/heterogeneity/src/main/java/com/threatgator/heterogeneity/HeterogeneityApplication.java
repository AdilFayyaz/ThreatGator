package com.threatgator.heterogeneity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HeterogeneityApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeterogeneityApplication.class, args);
        // Here read the accounts table and call the endpoint listed here

    }

}
