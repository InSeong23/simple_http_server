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

import com.nhnacademy.http.channel.HttpJob;
import com.nhnacademy.http.channel.RequestChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


@Slf4j
public class SimpleHttpServer {

    private final int port;
    private static final int DEFAULT_PORT=8080;

    private final RequestChannel requestChannel;

    public SimpleHttpServer(){
        this(DEFAULT_PORT);
    }
    private WorkerThreadPool workerThreadPool;

    public SimpleHttpServer(int port) {
        if(port<=0){
            throw new IllegalArgumentException(String.format("Invalid Port:%d",port));
        }
        this.port = port;
        //RequestChannel() 초기화 합니다.
        requestChannel = new RequestChannel();

        //workerThreadPool 초기화 합니다.
        workerThreadPool = new WorkerThreadPool(requestChannel);
    }

    public void start(){
        //workerThreadPool을 시작 합니다.
        workerThreadPool.start();

        try(ServerSocket serverSocket = new ServerSocket(this.port);){
            while(true){
                Socket client = serverSocket.accept();
                
                //Queue(requestChannel)에 HttpJob 객체를 배치 합니다.
                requestChannel.addHttpJob(new HttpJob(client));
            }
        }catch (IOException e){
            log.error("server error:{}",e);
        }
    }
}