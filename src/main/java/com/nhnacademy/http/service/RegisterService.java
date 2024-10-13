package com.nhnacademy.http.service;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.ResponseUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class RegisterService implements HttpService {

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        String method = httpRequest.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            doGet(httpRequest, httpResponse);
        } else if ("POST".equalsIgnoreCase(method)) {
            doPost(httpRequest, httpResponse);
        } else {
            throw new RuntimeException("405 - Method Not Allowed");
        }
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            String responseBody = ResponseUtils.tryGetBodyFromFile("/register.html");
            String responseHeader = ResponseUtils.createResponseHeader(
                    ResponseUtils.HttpStatus.OK.getCode(),
                    httpResponse.getCharacterEncoding(),
                    responseBody.getBytes(StandardCharsets.UTF_8).length
            );

            PrintWriter writer = httpResponse.getWriter();
            writer.write(responseHeader);
            writer.write(responseBody);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serve register.html", e);
        }
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        String userId = httpRequest.getParameter("userId");
        String userPassword = httpRequest.getParameter("userPassword");
        String userEmail = httpRequest.getParameter("userEmail");

        // 사용자 정보 저장 로직 (예: 데이터베이스 저장 등)
        // 여기서는 단순히 이름만 InfoService로 전달하기 위해 리디렉션합니다.

        // 리디렉션을 위한 쿼리 파라미터 설정
        String redirectLocation = "/info.html?name=" + encodeURIComponent(userId);

        // /info.html로 리디렉션
        try {
            String responseHeader = "HTTP/1.0 302 Found\r\n" +
                                    "Location: " + redirectLocation + "\r\n" +
                                    "Content-Length: 0\r\n" +
                                    "\r\n";
            PrintWriter writer = httpResponse.getWriter();
            writer.write(responseHeader);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to redirect to /info.html", e);
        }
    }

    /**
     * URL 인코딩을 수행합니다.
     *
     * @param value 인코딩할 문자열
     * @return 인코딩된 문자열
     */
    private String encodeURIComponent(String value) {
        try {
            return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // UTF-8은 항상 지원되므로 이 예외는 발생하지 않습니다.
            return value;
        }
    }
}
