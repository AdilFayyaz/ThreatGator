package com.threatgator.dataanalysis;

import com.threatgator.dataanalysis.model.elasticMapper;
import com.threatgator.dataanalysis.model.malwares;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.IOException;

@SpringBootApplication
public class DataAnalysisApplication {

    public static void main(String[] args) throws JSONException, IOException {

        SpringApplication.run(DataAnalysisApplication.class, args);
        elasticMapper connect= new elasticMapper();
        malwares mal = new malwares();
        connect.SearchMalwares(mal);
        System.out.println("***************************************************");
        System.out.println(mal.getMalwaresMap());
    }

}
