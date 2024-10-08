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

package com.nhnacademy.http.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseImplTest {
    HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        Socket socket = Mockito.mock(Socket.class);
        httpResponse = new HttpResponseImpl(socket);
    }

    @Test
    @DisplayName("Socket is null")
    void constructor() {
        // TODO#106 socket null check, IllegalArgumentException이 발생 하는지 검증 합니다.
        assertThrows(IllegalArgumentException.class, () -> {
            new HttpResponseImpl(null);
        });
    }

    @Test
    @DisplayName("instance of PrintWriter")
    void getWriter() throws IOException {
        Assertions.assertInstanceOf(PrintWriter.class, httpResponse.getWriter());
    }

    @Test
    void setCharacterEncoding() {
        httpResponse.setCharacterEncoding("euc-kr");
        Assertions.assertEquals("euc-kr", httpResponse.getCharacterEncoding());
    }

    @Test
    @DisplayName("default Character Encoding : utf-8")
    void getCharacterEncoding() {
        // TODO#107 default getCharacterEncoding()이 'utf-8'인지 검증 합니다.
        Assertions.assertEquals("utf-8", httpResponse.getCharacterEncoding().toLowerCase());
    }
}