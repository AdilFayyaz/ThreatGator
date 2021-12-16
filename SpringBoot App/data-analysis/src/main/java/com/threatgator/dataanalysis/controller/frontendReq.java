package com.threatgator.dataanalysis.controller;
import com.threatgator.dataanalysis.model.elasticMapper;
import com.threatgator.dataanalysis.model.malwares;
import com.threatgator.dataanalysis.model.notifications;
import com.threatgator.dataanalysis.model.vulnerabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dataAnalysis")
public class frontendReq {
    public elasticMapper connect;
    public malwares mal;
    public notifications notif;
    frontendReq() throws JSONException, IOException {
        connect= new elasticMapper();
        mal = new malwares();
        notif = new notifications();
        connect.SearchMalwares(mal);
        connect.GetNotifications(notif);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getTopMalwares/{value}")
    public String getTopMalwares(@PathVariable int value) throws JSONException, IOException {
        return mal.getTopMalwares(value).toString();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getNotifications/{value}")
    public String getNotifications(@PathVariable int value) throws JSONException, IOException{
        return notif.getNotifications(value).toString();
    }

}
