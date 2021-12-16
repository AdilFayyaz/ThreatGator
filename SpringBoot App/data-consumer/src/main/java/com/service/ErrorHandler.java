package com.service;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class ErrorHandler implements ResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // your error handling here
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
      return true;
    }
}