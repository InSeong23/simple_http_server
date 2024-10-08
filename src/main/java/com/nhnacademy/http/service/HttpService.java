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

/*TODO#1 Http Request, Http Response를 parameter를 인자로 받고 http 요청을 처리 하고 응답하는 역할을 합니다.
    - HttpService interface 이해 합니다.
 */
public interface HttpService {

    default void service(HttpRequest httpRequest, HttpResponse httpResponse){
        //httpRequest.getMethod()에 따라서 doGet() or doPost()를 호출 합니다.
        if(httpRequest.getMethod().equals("GET")){
            doGet(httpRequest, httpResponse);
        }else if(httpRequest.getMethod().equals("POST")){
            doPost(httpRequest, httpResponse);
        }
    }

    default void doGet(HttpRequest httpRequest, HttpResponse httpResponse){
        throw new RuntimeException("405 - Method Not Allowd");
    }

    default void doPost(HttpRequest httpRequest, HttpResponse httpResponse){
        throw new RuntimeException("405 - Method Not Allowd");
    }
}
