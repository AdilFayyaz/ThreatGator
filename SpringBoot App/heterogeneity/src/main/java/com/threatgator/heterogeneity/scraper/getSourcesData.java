package com.threatgator.heterogeneity.scraper;

import com.threatgator.heterogeneity.model.Account;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

// End points to scrape the data from different sources
@RestController

public class getSourcesData {
    String url_reddit = "http://127.0.0.1:8000/source/reddit/mostRecentData";
    String url_twitter = "http://127.0.0.1:8000/source/twitter";

    String allAccounts = "http://localhost:8081/sources/getAll/listAccount";
    long scheudule_time = TimeUnit.DAYS.toMillis(2);
    String url_securelist = "http://127.0.0.1:8000/source/securelist/";


    @Scheduled(cron = "0 0 5 * * ?")
    public void scrapeReddit() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        Account[] s;
        s = restTemplate.getForObject(allAccounts, Account[].class);
        for (int i=0;i<s.length;i++) {
            if(s[i].getSource().getName().equals("Reddit")) {
                String handle = s[i].getHandle();
                JSONObject j = new JSONObject();
                j.put("username", handle);
                RestTemplate res = new RestTemplate();
                String response = res.postForObject(url_reddit,new HttpEntity<String>(j.toString()),String.class);
                System.out.println(response);
            }
        }
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void scrapeTwitter() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        Account[] s;
        s = restTemplate.getForObject(allAccounts, Account[].class);
        for (int i=0;i<s.length;i++) {
            if(s[i].getSource().getName().equals("Twitter")) {
                String handle = s[i].getUsername();
                JSONObject j = new JSONObject();
                j.put("handle", handle);
                j.put("Date", "Jan 1");
                j.put("isHandle", 1);
                RestTemplate res = new RestTemplate();
                String response = res.postForObject(url_twitter,new HttpEntity<String>(j.toString()),String.class);
                System.out.println(response);
            }
        }
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void scrapeSecureList() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        Account[] s;
        s = restTemplate.getForObject(allAccounts, Account[].class);
        for (int i=0;i<s.length;i++) {
            if(s[i].getSource().getName().equals("Secure List")) {
                String handle = s[i].getUsername();
                JSONObject j = new JSONObject();
                j.put("handle", handle);
                j.put("Date", "Jan 1");
                j.put("isHandle", 1);
                RestTemplate res = new RestTemplate();
                String response = res.getForObject(url_securelist+handle, String.class);
                System.out.println(response);
            }
        }
    }




}
