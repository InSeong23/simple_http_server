package com.nhnacademy.http.service;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.CounterUtils;
import com.nhnacademy.http.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class RegisterService implements HttpService {
    String userId;
    String userPassword;
    String userEmail;
    String responseBody;

    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {

        userId = httpRequest.getParameter("userId");
        userPassword = httpRequest.getParameter("userPassword");
        userEmail = httpRequest.getParameter("userEmail");

        // 라이터 불러와서 바로 리스폰으로 301로 임의로 보냄
        try {
            PrintWriter printWriter = httpResponse.getWriter();
            log.info("writer {} ", printWriter);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }
}
