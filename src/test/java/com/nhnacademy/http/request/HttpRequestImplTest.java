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

package com.nhnacademy.http.request;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class HttpRequestImplTest {

    static HttpRequest request;

    static Socket client = Mockito.mock(Socket.class);

    @BeforeAll
    static void setUp() throws IOException {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("GET /index.html?id=marco&age=40&name=마르코 HTTP/1.1%s", System.lineSeparator()));
        sb.append(String.format("Host: localhost:8080%s", System.lineSeparator()));
        sb.append(String.format("Connection: keep-alive%s", System.lineSeparator()));
        sb.append(String.format("Cache-Control: max-age=0%s", System.lineSeparator()));
        sb.append(String.format("sec-ch-ua-platform: macOS%s", System.lineSeparator()));
        sb.append(String.format(
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36%s",
                System.lineSeparator()));
        sb.append(String.format(
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"));

        InputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());
        Mockito.when(client.getInputStream()).thenReturn(inputStream);
        request = new HttpRequestImpl(client);
    }

    @Test
    void constructor() {
        assertInstanceOf(HttpRequest.class, request);
    }

    @Test
    @DisplayName("getMethod() = GET")
    void getMethod() {
        // TODO#101 request method를 검증 합니다.
        String method = request.getMethod();
        Assertions.assertEquals("GET", method);
    }

    @Test
    @DisplayName("getParameterById : id=marco")
    void getParameterById() {
        // TODO#102 id queryString을 검증 합니다.
        String id = request.getParameter("id");
        Assertions.assertEquals("marco", id);
    }

    @Test
    @DisplayName("getParameterByName : name=마르코")
    void getParameterByName() {
        // TODO#103 name queryString을 검증 합니다.
        String name = request.getParameter("name");
        Assertions.assertEquals("마르코", name);
    }

    @Test
    @DisplayName("getParameterByAge : age=40")
    void getParameterByAge() {
        // TODO#104 age queryString을 검증 합니다.
        String age = request.getParameter("age");
        assertEquals(String.valueOf(40), age);
    }

    @Test
    void getParameterMap() {

        Map<String, Object> expected = new HashMap<>();
        expected.put("id", "marco");
        expected.put("age", "40");
        expected.put("name", "마르코");

        Map actual = request.getParameterMap();
        assertEquals(expected, actual);
    }

    @Test
    void getHeader() {
        Assertions.assertAll(() -> {
            assertEquals(request.getHeader("sec-ch-ua-platform"), "macOS");
            assertTrue(request.getHeader("User-Agent").contains("Mozilla/5.0"));
            assertTrue(request.getHeader("Host").contains("localhost"));
        });
    }

    @Test
    void attributeTest() {
        request.setAttribute("numberList", List.of(1, 2, 3, 4, 5));
        request.setAttribute("count", 1l);
        request.setAttribute("name", "엔에이치엔아카데미");
        long actual = (long) request.getAttribute("count");
        String nhnacademy = (String) request.getAttribute("name");
        List<Integer> numberList = (List<Integer>) request.getAttribute("numberList");
        Assertions.assertAll(() -> {
            assertEquals(1l, actual);
            assertEquals("엔에이치엔아카데미", nhnacademy);
            assertEquals(List.of(1, 2, 3, 4, 5), numberList);
        });
    }

    @Test
    @DisplayName("URI=/index.html")
    void getRequestURI() {
        // TODO#105 requestURI()를 검증 합니다.
        assertEquals("/index.html", request.getRequestURI());
    }

}