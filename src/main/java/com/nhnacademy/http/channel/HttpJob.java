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

package com.nhnacademy.http.channel;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.request.HttpRequestImpl;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.response.HttpResponseImpl;
import com.nhnacademy.http.service.*;
import com.nhnacademy.http.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

@Slf4j
public class HttpJob implements Executable {

    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;

    private final Socket client;

    public HttpJob(Socket client) {
        this.httpRequest = new HttpRequestImpl(client);
        this.httpResponse = new HttpResponseImpl(client);
        this.client = client;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    @Override
    public void execute(){

        log.debug("method:{}", httpRequest.getMethod());
        log.debug("uri:{}", httpRequest.getRequestURI());
        log.debug("clinet-closed:{}",client.isClosed());

        HttpService httpService = null;

        /*TODO#6 RequestURI에 따른 HttpService를 생성 합니다.
            - httpService.service(httpRequest, httpResponse) 호출하면
            - service()에서 Request Method에 의해서 doGet or doPost를 호출 합니다
            - ex1) /test.html존재 하지 않는다면 NotFoundHttpService 를 httpService에 할당 합니다.
            - ex2) /index.html -> IndexHttpService 객체를 httpService에 할당 합니다.
            - ex3) /info.html -> InfoHttpService 객체를 httpService에 할당 합니다.
        */


        //TODO#7 httpService.service() 호출 합니다. 호출시 예외 Method Not Allowd 관련 Exception이 발생하면 httpService에 MethodNotAllowdService 객체를 생성해서 할당 합니다.


        //TODO#8 client 연결을 종료 합니다.


    }
}
