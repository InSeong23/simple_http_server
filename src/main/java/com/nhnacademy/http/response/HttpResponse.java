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

import java.io.IOException;
import java.io.PrintWriter;

public interface HttpResponse {

    /*
    * * Returns a <code>PrintWriter</code> object that can send character text to the client. The
     * <code>PrintWriter</code> uses the character encoding returned by {@link #getCharacterEncoding}. If the response's
     * character encoding has not been specified as described in <code>getCharacterEncoding</code> (i.e., the method
     * just returns the default value <code>ISO-8859-1</code>), <code>getWriter</code> updates it to
     * <code>ISO-8859-1</code>.
    * */

    PrintWriter getWriter() throws IOException;

    /*
    * charset – a String specifying only the character set defined by IANA Character Sets
    * */
    void setCharacterEncoding(String charset);

    String getCharacterEncoding();

}
