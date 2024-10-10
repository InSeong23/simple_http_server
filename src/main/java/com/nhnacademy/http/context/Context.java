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

package com.nhnacademy.http.context;

//TODO#1 Context Interface 입니다.
//Application이 구동되는 환경을 Context라고 합니다.
public interface Context {
    //Object를 등록합니다.
    void setAttribute(String name, Object object);
    //Object를 삭제합니다.
    void removeAttribute(String name);
    //Object를 얻습니다.
    Object getAttribute(String name);
}