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

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.CounterUtils;
import com.nhnacademy.http.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class InfoHttpService implements HttpService {
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        // Body-설정
        String responseBody = null;

        try {
            responseBody = ResponseUtils.tryGetBodyFromFile(httpRequest.getRequestURI());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String id = httpRequest.getParameter("id");
        String name = httpRequest.getParameter("name");
        name = URLDecoder.decode(name, StandardCharsets.UTF_8);
        String age = httpRequest.getParameter("age");

        log.debug("id:{}", id);
        log.debug("name:{}", name);
        log.debug("age:{}", age);

        responseBody = responseBody.replace("${id}", id);
        responseBody = responseBody.replace("${name}", name);
        responseBody = responseBody.replace("${age}", age);

        // TODO#10 CounterUtils.increaseAndGet()를 이용해서 context에 있는 counter 값을 증가시키고,
        // 반환되는 값을 info.html에 반영 합니다.
        // ${count} <-- counter 값을 치환 합니다.
        responseBody = responseBody.replace("${count}", String.valueOf((CounterUtils.increaseAndGet())));

        // Header-설정
        String responseHeader = ResponseUtils.createResponseHeader(200, "UTF-8", responseBody.getBytes().length);

        // PrintWriter 응답
        try (PrintWriter bufferedWriter = httpResponse.getWriter();) {
            bufferedWriter.write(responseHeader);
            bufferedWriter.write(responseBody);
            bufferedWriter.write("\n");
            bufferedWriter.flush();
            log.debug("body:{}", responseBody.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
