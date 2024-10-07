/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.http;

import ch.qos.logback.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

@Slf4j
class SimpleHttpServerTest {

    static int TEST_PORT=9999;

    @BeforeAll
    static void beforeAllSetUp(){
        Thread thread = new Thread(()->{
            SimpleHttpServer simpleHttpServer = new SimpleHttpServer(TEST_PORT);
            try {
                simpleHttpServer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }catch (Exception e){
                log.debug("exit!!!");
            }
        });
        thread.start();
    }

    @Test
    @DisplayName("status code : 200 ok")
    void request1() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:%d",TEST_PORT)))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        log.debug("response:{}",response.body());

        //TODO#100 - response.statusCode() == 200 검증 합니다.

    }

    @Test
    @DisplayName("response: hello java")
    void request2() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:%d",TEST_PORT)))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());

        //TODO#101 - response.body() 'hello' or 'java' 문자열이 포함되었는지 검증 합니다.
        Assertions.assertAll(

        );
    }

    @Test
    @DisplayName("content-type")
    void request3() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:%d",TEST_PORT)))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        Optional<String> contentTypeOptional = response.headers().firstValue("Content-Type");
        String actual = contentTypeOptional.get().toLowerCase();
        log.debug("contentType:{}",actual);

        //TODO#102 contentType이 'text/html' 검증 합니다.


    }

    @Test
    @DisplayName("charset utf-8")
    void request4() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:%d",TEST_PORT)))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        Optional<String> contentTypeOptional = response.headers().firstValue("Content-Type");
        String actual = contentTypeOptional.get().toLowerCase();
        log.debug("contentType:{}",actual);

        //TODO#103 contentType header의 charset=utf-8 인지 검증 합니다.

    }


    @Test
    @DisplayName("Content-Length")
    void request5() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:%d",TEST_PORT)))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        Optional<String> contentLengthOptional = response.headers().firstValue("Content-Length");
        String actual = contentLengthOptional.get();

        log.debug("Content-Length:{}",actual);

        //TODO#104 content-Length 값이 존재 하는지 검증 합니다.

    }


    @AfterAll
    static void tearDown() throws InterruptedException {
       Thread.sleep(1000);
    }

}