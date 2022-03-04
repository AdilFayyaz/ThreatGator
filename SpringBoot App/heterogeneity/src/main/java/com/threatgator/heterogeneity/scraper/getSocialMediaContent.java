package com.threatgator.heterogeneity.scraper;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
// End points to scrape the data from different sources
@RestController
public class getSocialMediaContent {
    String url = "http://127.0.0.1:8000/source/reddit/mostRecentData";
    String url_twitter = "http://127.0.0.1:8000/source/twitter";
    Object[] reddits;

    // Get data from Reddit given a username
    @RequestMapping("/getReddits")
    public String getReddits(@RequestParam String username) throws JSONException {
        String s;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject r = new JSONObject();
        r.put("username", username);
        s = restTemplate.postForObject(url,new HttpEntity<String>(r.toString()), String.class);
        return s;
    }

    // Get data from tweets given a name
    @RequestMapping("/getTweetsFromAccount")
    public String getTweetsProfiles(@RequestParam String name) throws JSONException{
        String s;
        RestTemplate restTemplate = new RestTemplate();
        JSONObject r = new JSONObject();
        r.put("handle", name);
        Date today = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(today);
        int month = now.get(Calendar.MONTH) + 1;
        if (month ==12){
            month = 1;
        }
        else{
            month -=1;
        }
        int day = now.get(Calendar.DAY_OF_MONTH);
        Dictionary months = new Hashtable();
        months.put(1,"Jan");
        months.put(2,"Feb");
        months.put(3,"Mar");
        months.put(4,"Apr");
        months.put(5,"May");
        months.put(6,"Jun");
        months.put(7,"Jul");
        months.put(8,"Aug");
        months.put(9,"Sep");
        months.put(10,"Oct");
        months.put(11,"Nov");
        months.put(12,"Dec");

        String m = months.get(month).toString();
        m += String.valueOf(day);
        r.put("Date", "Oct 10");
        r.put("isHandle", 1);
        s = restTemplate.postForObject(url_twitter,new HttpEntity<String>(r.toString()), String.class);
        return s;
    }
}
