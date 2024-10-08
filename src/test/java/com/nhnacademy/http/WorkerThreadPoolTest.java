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

import com.nhnacademy.http.channel.RequestChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.function.Try;
import org.junit.platform.commons.util.ReflectionUtils;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkerThreadPoolTest {

    static WorkerThreadPool threadPool;
    static RequestChannel requestChannel;
    @BeforeAll
    static void beforeAllSetUp() {
        requestChannel = new RequestChannel();
        threadPool = new WorkerThreadPool(10,requestChannel);
        threadPool.start();
    }

    @Test
    @Order(1)
    @DisplayName("poolSize < 0")
    void constructorTest1(){
        //TODO#101 poolsize <0  IllegalArgumentException 발생하는지 검증 합니다.

    }

    @Test
    @Order(2)
    @DisplayName("runnable  parameter check ")
    void constructorTest2(){
        //TODO#102 runable parameter null 이면 IllegalArgumentException 발생하는지 검증 합니다.

    }

    @Test
    @Order(3)
    @DisplayName("thread-pool size : 10")
    void constructorTest3() throws Exception {
        Try<Object> readFieldValue = ReflectionUtils.tryToReadFieldValue(WorkerThreadPool.class, "workerThreads",threadPool);
        Thread[] workerThreads = (Thread[]) readFieldValue.get();

        //TODO#103 기본 생성자로 생성한  threadList poolSize가 10으로 생성되었는지 검증 합니다.

    }

    @Test
    @Order(4)
    @DisplayName("thread start, thread Status check : alive")
    void start() throws Exception {

        Try<Object> readFieldValue = ReflectionUtils.tryToReadFieldValue(WorkerThreadPool.class, "workerThreads",threadPool);
        Thread[] workerThreads = (Thread[]) readFieldValue.get();
        int aliveCount = 0;

        //TODO#104 workerThreads의 각각의 thread가 isAlive()면 aliveCount++ 될 수 있또록 구현


        log.debug("aliveCount:{}",aliveCount);
        Assertions.assertEquals(10,aliveCount);
    }

    @Test
    @Order(5)
    @DisplayName("thread stop, thread Status : TERMINATED")
    void stop() throws Exception {
        threadPool.stop();

        Try<Object> readFieldValue = ReflectionUtils.tryToReadFieldValue(WorkerThreadPool.class, "workerThreads",threadPool);
        Thread[] workerThreads = (Thread[]) readFieldValue.get();

        int terminatedCount = 0;

        //TODO#105 threadList의 각각의 thread의 상태가 TERMINATED이면 terminatedCount++ 될 수 있도록 구현 합니다.

        log.debug("terminatedCount:{}",terminatedCount);
        Assertions.assertEquals(10,terminatedCount);
    }

}