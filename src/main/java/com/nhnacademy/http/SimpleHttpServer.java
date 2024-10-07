package com.nhnacademy.http;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class SimpleHttpServer {

    private final int port;
    private static final int DEFAULT_PORT = 8080;

    private final AtomicLong atomicCounter;

    public SimpleHttpServer() {
        this(DEFAULT_PORT);
    }

    public SimpleHttpServer(int port) {
        // TODO#9 port <=0 이면 IllegalArgumentException 발생합니다. 적절한 Error Message를 작성하세요.
        if (port <= 0)
            throw new IllegalArgumentException();
        // TODO#10 port와 atomicCounter를 초기화 합니다.
        this.port = port;
        atomicCounter = new AtomicLong();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);) {

            HttpRequestHandler httpRequestHandlerA = new HttpRequestHandler();
            HttpRequestHandler httpRequestHandlerB = new HttpRequestHandler();

            // TODO#11 threadA를 생성하고 시작 합니다. thread-name : threadA 설정 합니다.
            Thread threadA = new Thread(httpRequestHandlerA, "threadA");
            threadA.start();

            // TODO#12 threadB를 생성하고 시작 합니다. thread-name: threadB 설정 합니다.
            Thread threadB = new Thread(httpRequestHandlerB, "threadB");
            threadB.start();
            while (true) {
                Socket client = serverSocket.accept();
                /*
                 * TODO#13 count값이 짝수이면 httpRequestHandlerA에 client를 추가 합니다.
                 * count값이 홀수라면 httpRequestHandlerB에 clinet를 추가 합니다.
                 */
                int count = (int) atomicCounter.incrementAndGet(); // ++과 비슷?
                if ((count % 2) == 0) {
                    httpRequestHandlerA.addRequest(client);
                } else if ((count % 2 == 1)) {
                    httpRequestHandlerB.addRequest(client);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
