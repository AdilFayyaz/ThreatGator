package com.threatgator.usermanagement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threatgator.usermanagement.model.Bookmarks;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class BookmarkServiceImpl implements BookmarkService{
    public RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));

    private static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addParameter("size", "2000");

        COMMON_OPTIONS = builder.build();
    }

    @Override
    public boolean toggleBookmark(Integer userId, String reportHash) throws IOException, JSONException {
        GetIndexRequest req = new GetIndexRequest("bookmarks");
        boolean exists = client.indices().exists(req, RequestOptions.DEFAULT);

        if (!exists) // index not created yet, so this will be the first entry
        {
            return addNewEntry(userId, reportHash);
        }
        else{
            // index exists, now see if this user exists already
            GetRequest request1 = new GetRequest("bookmarks", String.valueOf(userId));
            GetResponse response = client.get(request1, RequestOptions.DEFAULT);

            if (!response.isExists()) { //user not added before, so this is their first bookmark
                return addNewEntry(userId, reportHash);
            }
            else{ //user already exists, just need to update their bookmark
                boolean adding=false;
                String sourceAsString = response.getSourceAsString();
                JSONObject jsonComplete = new JSONObject(sourceAsString);
                JSONArray currentBookmarks = jsonComplete.getJSONArray("bookmarkedReports");
                ArrayList<String> finalBookmarks = new ArrayList<>();
                for ( int i=0; i< currentBookmarks.length(); i++){
                    finalBookmarks.add(currentBookmarks.getString(i));
                }
                if (finalBookmarks.contains(reportHash)){ //removing report from bookmarks
                    finalBookmarks.remove(reportHash);
                    adding=false;
                }
                else{ //not already bookmarked, so adding to bookmarks;
                    finalBookmarks.add(reportHash);
                    adding=true;
                }

                Bookmarks bookmarks = new Bookmarks();
                bookmarks.bookmarkedReports=finalBookmarks;
                ObjectMapper Nmapper = new ObjectMapper();
                String r = Nmapper.writeValueAsString(bookmarks);

                UpdateRequest request2 = new UpdateRequest("bookmarks", String.valueOf(userId));
                request2.doc(r,XContentType.JSON);
                UpdateResponse updateResponse = client.update(
                        request2, RequestOptions.DEFAULT);

                return adding;
            }
        }
    }

    @Override
    public boolean isBookmarked(Integer userId, String reportHash) throws JSONException, IOException {
        GetIndexRequest req = new GetIndexRequest("bookmarks");
        boolean exists = client.indices().exists(req, RequestOptions.DEFAULT);

        if (!exists) // index not created yet, so not bookmarked
        {
            return false;
        }
        else {
            // index exists, now see if this user exists already
            GetRequest request1 = new GetRequest("bookmarks", String.valueOf(userId));
            GetResponse response = client.get(request1, RequestOptions.DEFAULT);

            if (!response.isExists()) { //user not added before, so not bookmarked
                return false;
            } else { //user already exists, just need to update their bookmark
                String sourceAsString = response.getSourceAsString();
                JSONObject jsonComplete = new JSONObject(sourceAsString);
                JSONArray currentBookmarks = jsonComplete.getJSONArray("bookmarkedReports");
                ArrayList<String> finalBookmarks = new ArrayList<>();
                for (int i = 0; i < currentBookmarks.length(); i++) {
                    finalBookmarks.add(currentBookmarks.getString(i));
                }
                return (finalBookmarks.contains(reportHash));
            }
        }
    }

    private boolean addNewEntry(Integer userId, String reportHash) throws IOException {
        Bookmarks bookmarks = new Bookmarks();
        bookmarks.bookmarkedReports = new ArrayList<>();
        bookmarks.bookmarkedReports.add(reportHash);

        ObjectMapper Nmapper = new ObjectMapper();
        String r = Nmapper.writeValueAsString(bookmarks);
        try {
            IndexRequest rQ = new IndexRequest("bookmarks")
                    .id(String.valueOf(userId))
                    .source(r, XContentType.JSON);
            IndexResponse res = client.index(rQ, RequestOptions.DEFAULT);
            System.out.println("Response Id: " + res.getId());
            return true;
        } catch (HttpStatusCodeException e) {
            System.out.println("Error Occurred in Adding Bookmark");
        }
        return false;
    }

    @Override
    public ArrayList<String> getBookmarks(Integer userId) throws IOException, JSONException {
        GetIndexRequest req = new GetIndexRequest("bookmarks");
        boolean exists = client.indices().exists(req, RequestOptions.DEFAULT);

        if (!exists) // index not created yet, so no bookmarks
        {
            return null;
        }
        else {
            // index exists, now see if this user exists already
            GetRequest request1 = new GetRequest("bookmarks", String.valueOf(userId));
            GetResponse response = client.get(request1, RequestOptions.DEFAULT);

            if (!response.isExists()) { //user not added before, so no bookmarks
                return null;
            } else { //user already exists, just need to update their bookmark
                String sourceAsString = response.getSourceAsString();
                JSONObject jsonComplete = new JSONObject(sourceAsString);
                JSONArray currentBookmarks = jsonComplete.getJSONArray("bookmarkedReports");
                ArrayList<String> finalBookmarks = new ArrayList<>();
                for ( int i=0; i< currentBookmarks.length(); i++){
                    finalBookmarks.add(currentBookmarks.getString(i));
                }
                return finalBookmarks;
            }
        }
    }
}
