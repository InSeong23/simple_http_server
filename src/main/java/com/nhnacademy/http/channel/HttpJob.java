package com.nhnacademy.http.channel;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CRL;
import java.util.Objects;

@Slf4j
public class HttpJob implements Executable {
    private final Socket client;
    private static final String CRLF = "\r\n";

    public HttpJob(Socket client) {
        if (Objects.isNull(client)) {
            throw new IllegalArgumentException("client Socket is null");
        }
        this.client = client;
    }

    public Socket getClient() {
        return client;
    }

    @Override
    public void execute() {

        StringBuilder requestBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter bufferdeWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            while (true) {
                String line = bufferedReader.readLine();
                requestBuilder.append(line);

                if (Objects.isNull(line) || line.length() == 0)
                    break;

            }
            StringBuilder responseBody = new StringBuilder();
            responseBody.append("<html>");
            responseBody.append("<body>");
            responseBody.append("<h1>");
            responseBody.append(String.format("{%s}:hello java", Thread.currentThread().getName()));
            responseBody.append("</h1>");
            responseBody.append("</body>");
            responseBody.append("</html>");

            StringBuilder responseHeader = new StringBuilder();
            responseHeader.append(String.format("HTTP/1.0 200 OK%s", CRLF));
            responseHeader.append(String.format("Server: HTTP server/0.1%s", CRLF));
            responseHeader.append(String.format("Content-type: text/html; charset=%s%s", "UTF-8", CRLF));
            responseHeader.append(String.format("Connection: Closed%s", CRLF));
            responseHeader.append(String.format("Content-Length:%d %s%s", responseBody.length(), CRLF, CRLF));

            bufferdeWriter.write(responseHeader.toString());
            bufferdeWriter.write(responseBody.toString());
            bufferdeWriter.flush();
            client.close();
        } catch (IOException e) {
            log.error("server error:{}", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
