package com.threatgator.usermanagement.service;

import com.threatgator.usermanagement.model.Bookmarks;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public interface BookmarkService {
    public boolean toggleBookmark(Integer userId, String reportHash) throws IOException, JSONException;
    public ArrayList<String> getBookmarks(Integer userId) throws IOException, JSONException;
    public boolean isBookmarked(Integer userId, String reportHash) throws JSONException, IOException;
}
