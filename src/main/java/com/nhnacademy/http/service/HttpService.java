package com.nhnacademy.http.service;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;

public interface HttpService {

    default void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getMethod().equals("GET")) {
            doGet(httpRequest, httpResponse);
        } else if (httpRequest.getMethod().equals("POST")) {
            doPost(httpRequest, httpResponse);
        }
    }

    default void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        throw new RuntimeException("405 - Method Not Allowed");
    }

    default void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        throw new RuntimeException("405 - Method Not Allowed");
    }
}
