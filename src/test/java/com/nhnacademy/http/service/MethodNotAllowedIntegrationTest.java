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

package com.nhnacademy.http.service;

import com.nhnacademy.http.SimpleHttpServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class MethodNotAllowedIntegrationTest {
    private static final int TEST_PORT=9999;
    static SimpleHttpServer simpleHttpServer;
    static Thread thread;
    @BeforeAll
    static void setUp() throws IOException {
        thread = new Thread(()->{
            simpleHttpServer = new SimpleHttpServer(TEST_PORT);
            simpleHttpServer.start();
        });
        thread.start();
    }


    @Test
    @DisplayName("doPost : 405 method not allowed , /index.html")
    void doPost1() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = String.format("http://localhost:%d/index.html",TEST_PORT);
        log.debug("url:{}",url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());

        log.debug("response:{}",response.body());

        //TODO#107- response.statusCode() ==  405 검증 합니다.

    }
    @Test
    @DisplayName("doPost : 405 method not allowed , /info.html")
    void doPost2() throws URISyntaxException, IOException, InterruptedException {
        //TODO#108 - /info.html은 doGet 구현 되어있습니다. POST 요청을 했을 때 response.statusCode() == 405인지 검증 합니다.

    }

    @AfterAll
    static void tearDown() throws InterruptedException {
        Thread.sleep(2000);
    }
}
