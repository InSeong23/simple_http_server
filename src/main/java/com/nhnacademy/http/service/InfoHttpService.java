package com.nhnacademy.http.service;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.CounterUtils;
import com.nhnacademy.http.util.ResponseUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class InfoHttpService implements HttpService {

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        String method = httpRequest.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            doGet(httpRequest, httpResponse);
        } else {
            throw new RuntimeException("405 - Method Not Allowed");
        }
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        // 쿼리 파라미터에서 'name' 추출
        String name = httpRequest.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "Unknown";
        }

        // 카운트 증가
        long count = CounterUtils.increaseAndGet();

        // info.html 템플릿 읽기 및 동적 대체
        try {
            String responseBody = ResponseUtils.tryGetBodyFromFile("/info.html");
            responseBody = responseBody.replace("${name}", sanitize(name));
            responseBody = responseBody.replace("${count}", String.valueOf(count));

            // 응답 헤더 생성
            String responseHeader = ResponseUtils.createResponseHeader(
                    ResponseUtils.HttpStatus.OK.getCode(),
                    httpResponse.getCharacterEncoding(),
                    responseBody.getBytes(StandardCharsets.UTF_8).length
            );

            // 응답 전송
            PrintWriter writer = httpResponse.getWriter();
            writer.write(responseHeader);
            writer.write(responseBody);
            writer.flush();
        } catch (IOException e) {
            // 에러 발생 시 500 Internal Server Error 응답
            String errorResponse = "<html><body><h1>500 Internal Server Error</h1></body></html>";
            String errorHeader = ResponseUtils.createResponseHeader(
                    ResponseUtils.HttpStatus.INTERNAL_SERVER_ERROR.getCode(),
                    httpResponse.getCharacterEncoding(),
                    errorResponse.getBytes(StandardCharsets.UTF_8).length
            );
            try {
                PrintWriter writer = httpResponse.getWriter();
                writer.write(errorHeader);
                writer.write(errorResponse);
                writer.flush();
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        // POST 요청은 허용되지 않음
        throw new RuntimeException("405 - Method Not Allowed");
    }

    /**
     * 입력된 문자열을 간단히 이스케이프 처리하여 XSS 방지
     *
     * @param input 사용자 입력
     * @return 이스케이프 처리된 문자열
     */
    private String sanitize(String input) {
        if (input == null) return "";
        return input.replaceAll("&", "&amp;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;");
    }
}
