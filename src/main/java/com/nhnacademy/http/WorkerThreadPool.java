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

import java.util.Objects;

public class WorkerThreadPool {
    private final int poolSize;

    private final static int DEFAULT_POOL_SIZE = 5;

    private final Thread[] workerThreads;
    private final RequestChannel requestChannel;

    public WorkerThreadPool(RequestChannel requestChannel) {
        this(DEFAULT_POOL_SIZE, requestChannel);
    }

    public WorkerThreadPool(int poolSize, RequestChannel requestChannel) {
        if (poolSize < 1)
            throw new IllegalArgumentException("poolSize < 1");
        if (Objects.isNull(requestChannel))
            throw new IllegalArgumentException("requestChannel is null");
        this.poolSize = poolSize;
        this.requestChannel = requestChannel;

        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(requestChannel);

        workerThreads = new Thread[poolSize];

        for (int i = 0; i < poolSize; i++) {
            workerThreads[i] = new Thread(httpRequestHandler);
            workerThreads[i].setName(String.format("thread-%d", i));
        }
    }

    public synchronized void start() {
        for (Thread thread : workerThreads) {
            thread.start();
        }
    }

    public synchronized void stop() {
        for (Thread thread : workerThreads) {
            if (thread.isAlive() || Objects.nonNull(thread))
                thread.interrupt();
        }

        for (Thread thread : workerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
