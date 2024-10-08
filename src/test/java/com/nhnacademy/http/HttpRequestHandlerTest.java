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

import com.nhnacademy.http.channel.Executable;
import com.nhnacademy.http.channel.RequestChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class HttpRequestHandlerTest {

    @Test
    @DisplayName("RequestChannel = null")
    void constructorTest() {
        // TODO#106 RequestHandler 객체생성시 channel이 null 이면 IllegalArgumentException 발생하는지
        // 검증합니다.
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new HttpRequestHandler(null);
        });
    }

    @Test
    @DisplayName("producer & consumer")
    void run() {
        // TODO#107 requestChannel과 requestHandler 객체를 생성합니다.
        RequestChannel requestChannel = new RequestChannel();
        HttpRequestHandler requestHandler = new HttpRequestHandler(requestChannel);

        AtomicInteger counter = new AtomicInteger();

        // TODO#108 couter.incrementAndGet(); 호출하는 countExecutable 구현 합니다.
        Executable countExecutable = new Executable() {
            @Override
            public void execute() {
                // 구현
            }
        };

        // TODO#109 생산자 requestChannel 실행할 작업(countExecutable)을 1초에 한 번씩 총 5회 추가 합니다.
        Thread producer = new Thread(() -> {
            // 구현
        });
        producer.start();

        // TODO#110 requestHandler를 이용해서 consumer thread를 생성하고 실행 합니다.
        Thread consumer = null;

        // TODO#112 producer(생산자)의 작업이 끝나지 않았다면 테스트를 싱행하는 main Thread는 양보(대기) 합니다.

        log.debug("counter:{}", counter.get());

        Assertions.assertEquals(5, counter.get());
    }

}