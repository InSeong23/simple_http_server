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

import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;
import com.nhnacademy.http.context.exception.ObjectNotFoundException;
import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.request.HttpRequestImpl;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.response.HttpResponseImpl;
import com.nhnacademy.http.service.HttpService;
import com.nhnacademy.http.service.IndexHttpService;
import com.nhnacademy.http.service.InfoHttpService;
import com.nhnacademy.http.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.URL;
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
    public void execute() {

        log.debug("method:{}", httpRequest.getMethod());
        log.debug("uri:{}", httpRequest.getRequestURI());
        log.debug("clinet-closed:{}", client.isClosed());

        HttpService httpService = null;
        Context context = ContextHolder.getApplicationContext();

        // TODO#7 requestURI()을 이용해서 Context에 등록된 HttpService를 실행 합니다.
        // 404에 대해서 대응할 수 있도록 코드를 작성 합니다.
        if (!ResponseUtils.isExist(httpRequest.getRequestURI()))
            httpService = (HttpService) context.getAttribute(ResponseUtils.DEFAULT_404);
        else {
            try {
                httpService = (HttpService) context.getAttribute(httpRequest.getRequestURI());
            } catch (ObjectNotFoundException e) {
                httpService = (HttpService) context.getAttribute(ResponseUtils.DEFAULT_404);
            }
        }
        // TODO#8 httpService.service() 호출 합니다. 호출시 예외 Method Not Allowd 관련 Exception이
        // 발생하면 httpService에 MethodNotAllowdService 객채의 service() method를 호출 합니다.
        // 405에 대응할 수 있도록 코드를 작성 합니다.
        try {
            httpService.service(httpRequest, httpResponse);
        } catch (RuntimeException e) {
            httpService = (HttpService) context.getAttribute(ResponseUtils.DEFAULT_405);
            httpService.service(httpRequest, httpResponse);
        }

        try {
            if (Objects.nonNull(client) && client.isConnected()) {
                client.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
