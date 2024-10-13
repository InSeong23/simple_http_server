package com.nhnacademy.http.util;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResponseUtils {
    public static final String DEFAULT_404  = "/404.html";
    public static final String DEFAULT_405 = "/405.html";
    private static final String CRLF="\r\n";
    private ResponseUtils(){}

    public enum HttpStatus{
        OK(200,"OK"),
        NOT_FOUND(404, "Not Found"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        METHOD_NOT_ALLOWED(405,"Method Not Allowed");

        private final int code;
        private final String description;

        HttpStatus(int code, String description) {
            this.code = code;
            this.description=description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static HttpStatus getStatusFromCode(int code) {
            for (HttpStatus status : HttpStatus.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
            return INTERNAL_SERVER_ERROR;
        }
    }

    /**
     * /src/main/resources 하위에 filePath에 해당되는 파일이 존재하는지 체크 합니다.
     * @param filePath, filePath -> requestURL -> ex) /index.html
     * @return true or false
     */
    public static boolean isExist(String filePath){
        /*
           ex) filePath=/index.html 이면 /resources/index.html이 존재하면 true, 존재하지 않다면 false를 반환 합니다.
           ex) filePath=/ false를 반환 합니다.
        */
        if(filePath.equals("/")){
            return false;
        }
        URL url = ResponseUtils.class.getResource(filePath);
        return Objects.nonNull(url);
    }

    /**
     *
     * @param filePath , requestURI, ex) /index.html
     * @return String , index.html 파일을 읽고 String으로 반환
     * @throws IOException
     */
    public static String tryGetBodyFromFile(String filePath) throws IOException {
        /*  tryGetBodyFromFile 구현 합니다.
         * ex) filePath = /index.html -> /resources/index.html 파일을 읽어서 반환 합니다.
         * */

        StringBuilder responseBody = new StringBuilder();
        try(InputStream inputStream = ResponseUtils.class.getResourceAsStream(filePath);
            BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream,"UTF-8"))){
            String line;
            while((line = reader.readLine()) != null){
                responseBody.append(line).append("\n");
            }
        }
        return responseBody.toString();
    }

    /**
     *
     * @param httpStatusCode , 200 - OK
     * @param charset, default : UTF-8
     * @param contentLength, responseBody의 length
     * @return responseHeader를 String 반환
     */
    public static String createResponseHeader(int httpStatusCode, String charset, int contentLength ){
        /* responseHeader를 생성 합니다. 아래 header 예시를 참고

            - 200 OK
            HTTP/1.0 200 OK
            Server: HTTP server/0.1
            Content-type: text/html; charset=UTF-8
            Connection: Closed
            Content-Length:143

            - 404 Not Found
            HTTP/1.0 404 Not Found
            Server: HTTP server/0.1
            Content-type: text/html; charset=UTF-8
            Connection: Closed
            Content-Length:143

            - HttpStatusCode는 HttpStatus enum을 참고하여 구현 합니다.
        */

        StringBuilder responseHeader = new StringBuilder();
        responseHeader.append(String.format("HTTP/1.0 %d %s%s", httpStatusCode, HttpStatus.getStatusFromCode(httpStatusCode).getDescription(), CRLF));
        responseHeader.append(String.format("Server: HTTP server/0.1%s", CRLF));
        responseHeader.append(String.format("Content-type: text/html; charset=%s%s",charset, CRLF));
        responseHeader.append(String.format("Connection: Closed%s", CRLF));
        responseHeader.append(String.format("Content-Length: %d%s", contentLength, CRLF));
        responseHeader.append(CRLF); // 헤더와 본문을 구분하기 위한 빈 줄
        return responseHeader.toString();
    }
}
