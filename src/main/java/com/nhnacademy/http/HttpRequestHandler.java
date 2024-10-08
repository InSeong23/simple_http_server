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

import com.nhnacademy.http.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class HttpRequestHandler implements Runnable {

    private final RequestChannel requestChannel;

    public HttpRequestHandler(RequestChannel requestChannel) {
        if(Objects.isNull(requestChannel)){
            throw new IllegalArgumentException("requestChannel is null");
        }
        this.requestChannel = requestChannel;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Executable httpJob = requestChannel.getHttpJob();
                //httpJob 객체의 execute() method를 실행 합니다.
                httpJob.execute();
            } catch (IOException e) {
                // 상위 레벨의 다른 코드 또는 스레드가 이 스레드가 인터럽트 되었음을 인지 할 수 있습니다.
                if(e.getMessage().contains(InterruptedException.class.getName())){
                    Thread.currentThread().interrupt();
                }
                // 종료될 떄 필요한 코드가 있다면 작성 합니다.
                log.debug("RequestHandler error : {}",e.getMessage(),e);
            }
        }
    }
}
