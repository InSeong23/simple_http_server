package com.nhnacademy.http;

import com.nhnacademy.http.channel.*;
import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;
import com.nhnacademy.http.service.InfoHttpService;
import com.nhnacademy.http.service.MethodNotAllowedService;
import com.nhnacademy.http.service.NotFoundHttpService;
import com.nhnacademy.http.service.RegisterService;
import com.nhnacademy.http.util.CounterUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SimpleHttpServer {

    private final int port;
    private static final int DEFAULT_PORT = 8080;

    private final RequestChannel requestChannel;
    private WorkerThreadPool workerThreadPool;

    public SimpleHttpServer() {
        this(DEFAULT_PORT);
    }

    public SimpleHttpServer(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException(String.format("Invalid Port:%d", port));
        }
        this.port = port;
        // RequestChannel 초기화
        requestChannel = new RequestChannel();

        // WorkerThreadPool 초기화
        workerThreadPool = new WorkerThreadPool(requestChannel);

        /*
         * Context에 HttpService Object 등록
         * - ex) context.setAttribute("/index.html",new IndexHttpService());
         * - register.html, info.html, 404.html, 405.html 을 등록 합니다.
         */
        Context context = ContextHolder.getApplicationContext();
        context.setAttribute("/register.html", new RegisterService());
        context.setAttribute("/info.html", new InfoHttpService());
        context.setAttribute("/404.html", new NotFoundHttpService());
        context.setAttribute("/405.html", new MethodNotAllowedService());

        /*
         * Counter 구현을 위해서 CounterUtils.CONTEXT_COUNTER_NAME 으로, 0L을 context에 등록
         */
        context.setAttribute(CounterUtils.CONTEXT_COUNTER_NAME, 0L);
    }

    public void start() {
        // WorkerThreadPool을 시작
        workerThreadPool.start();

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            log.info("Server started on port {}", port);
            while (true) {
                Socket client = serverSocket.accept();
                log.info("Accepted connection from {}", client.getInetAddress());
                requestChannel.addHttpJob(new HttpJob(client));
            }
        } catch (IOException e) {
            log.error("Server error: {}", e.getMessage(), e);
        }
    }
}
