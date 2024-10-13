package com.nhnacademy.http.request;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HttpRequestImpl implements HttpRequest {

    private final Socket client;

    private final Map<String, Object> headerMap = new HashMap<>();
    private final Map<String, Object> attributeMap = new HashMap<>();
    private final static String KEY_HTTP_METHOD = "HTTP-METHOD";
    private final static String KEY_QUERY_PARAM_MAP = "HTTP-QUERY-PARAM-MAP";
    private final static String KEY_REQUEST_PATH = "HTTP-REQUEST-PATH";
    private final static String HEADER_DELIMITER = ":";

    public HttpRequestImpl(Socket socket) {
        this.client = socket;
        initialize();
    }

    private void initialize() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String line;
            boolean isFirstLine = true;

            while ((line = bufferedReader.readLine()) != null) {
                log.debug("line:{}", line);

                if (isFirstLine) {
                    parseHttpRequestInfo(line);
                    isFirstLine = false;
                } else if (isEndLine(line)) {
                    break;
                } else {
                    parseHeader(line);
                }
            }

            // POST 요청일 경우 본문 읽기
            if ("POST".equalsIgnoreCase(getMethod())) {
                String contentLengthValue = getHeader("Content-Length");
                if (contentLengthValue != null) {
                    int contentLength = Integer.parseInt(contentLengthValue);
                    char[] bodyChars = new char[contentLength];
                    int read = bufferedReader.read(bodyChars, 0, contentLength);
                    if (read == contentLength) {
                        String body = new String(bodyChars);
                        parseBody(body);
                    } else {
                        log.warn("Expected content length: {}, but read: {}", contentLength, read);
                    }
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException("Failed to initialize HttpRequestImpl", ex);
        }
    }

    @Override
    public String getMethod() {
        return String.valueOf(headerMap.get(KEY_HTTP_METHOD));
    }

    @Override
    public String getParameter(String name) {
        Map<String, String> params = getParameterMap();
        return params.get(name);
    }

    @Override
    public Map<String, String> getParameterMap() {
        return (Map<String, String>) headerMap.get(KEY_QUERY_PARAM_MAP);
    }

    @Override
    public String getHeader(String name) {
        return String.valueOf(headerMap.get(name));
    }

    @Override
    public void setAttribute(String name, Object o) {
        attributeMap.put(name, o);
    }

    @Override
    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    @Override
    public String getRequestURI() {
        return String.valueOf(headerMap.get(KEY_REQUEST_PATH));
    }

    private boolean isEndLine(String s) {
        return Objects.isNull(s) || s.equals("") ? true : false;
    }

    private void parseHeader(String s) {
        String[] hStr = s.split(HEADER_DELIMITER, 2);
        if (hStr.length < 2) {
            return; // Invalid header line
        }
        String key = hStr[0].trim();
        String value = hStr[1].trim();

        if (Objects.nonNull(key) && key.length() > 0) {
            headerMap.put(key, value);
        }
    }

    private void parseHttpRequestInfo(String s) {
        String[] arr = s.split(" ");
        // http method parse
        if (arr.length > 0) {
            headerMap.put(KEY_HTTP_METHOD, arr[0]);
        }
        // query parameter parse
        if (arr.length > 1) {
            String requestURI = arr[1];
            int questionIndex = requestURI.indexOf("?");
            String httpRequestPath;

            Map<String, String> queryMap = new HashMap<>();

            if (questionIndex > 0) {
                httpRequestPath = requestURI.substring(0, questionIndex);
                String queryString = requestURI.substring(questionIndex + 1);
                parseQueryString(queryString, queryMap);
            } else {
                httpRequestPath = requestURI;
            }

            // path 설정
            headerMap.put(KEY_REQUEST_PATH, httpRequestPath);

            // queryMap 설정
            headerMap.put(KEY_QUERY_PARAM_MAP, queryMap);
        }
    }

    private void parseQueryString(String queryString, Map<String, String> queryMap) {
        if (Objects.nonNull(queryString) && !queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    log.debug("key:{}, value={}", key, value);
                    queryMap.put(key, value);
                }
            }
        }
    }

    private void parseBody(String body) {
        Map<String, String> queryMap = (Map<String, String>) headerMap.get(KEY_QUERY_PARAM_MAP);
        if (queryMap == null) {
            queryMap = new HashMap<>();
            headerMap.put(KEY_QUERY_PARAM_MAP, queryMap);
        }

        parseQueryString(body, queryMap);
    }
}
